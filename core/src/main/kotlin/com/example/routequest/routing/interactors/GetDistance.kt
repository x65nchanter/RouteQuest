package com.example.routequest.routing.interactors

import com.example.routequest.routing.data.DistanceResolverProvider
import com.example.routequest.routing.domain.DistanceResolver
import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route

class GetDistance(private val provider: DistanceResolverProvider) {
    /*
     * Бедьняжка котлин не видет тут сайд эффектов :(
     */
    suspend fun execute(route: Route): Distance =
        provider.provide(route)()
}