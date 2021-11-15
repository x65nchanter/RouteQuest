package com.example.routequest.routing

import com.example.routequest.routing.data.DistanceResolverProvider
import com.example.routequest.routing.domain.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * Наивный способ считать длину
 * Алтернатива Direction API
 */
object HaversineDistanceProvider : DistanceResolverProvider {
    override fun provide(route: Route): DistanceResolver =
        DistanceResolver { Distance.Meters(route.length(::haversine)) }

    /*
     *  Допустим что длину маршрута можно вычесли суммированием дистанции между его координатами.
     *  Альтернатива вычислять длину на сервере а не на устройстве с ограничеными ресурсами
     *  сети и вычеслений.
     */
    private fun Route.length(comp: (cur: Coordinates, prev: Coordinates) -> Double) =
        fold(.0) { acc, cur ->
            when (cur) {
                is Route.Next -> acc + comp(cur.coordinates, cur.prev.coordinates)
                else -> acc
            }
        }

    /*
     * Допустим что земля это абсолютно гладкая сфера с единственным человеком на ней
     * Алтернатива вычислять растояние с учетем геометри и топологии
     */
    private fun haversine(cur: Coordinates, prev: Coordinates): Double {
        val curLatitudeRadian = cur.latitude * Math.PI / 180.0
        val prevLatitudeRadian = prev.latitude * Math.PI / 180.0

        val deltaLatitude = (prev.latitude - cur.latitude) * Math.PI / 180.0
        val deltaLongitude = (prev.longitude - cur.longitude) * Math.PI / 180.0
        val arc = sin(deltaLatitude / 2.0) * sin(deltaLatitude / 2.0) +
                cos(curLatitudeRadian) * cos(prevLatitudeRadian) *
                sin(deltaLongitude / 2.0) * sin(deltaLongitude / 2.0)

        return SolarSystem.PLANET_EARTH.radiusInMeters * 2.0 * atan2(
            sqrt(arc),
            sqrt(1.0 - arc)
        )
    }

    private enum class SolarSystem(val radiusInMeters: Double) {
        PLANET_EARTH(6.371e6)
    }
}