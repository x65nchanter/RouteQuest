package com.example.routequest.routing

import com.cocoahero.android.geojson.FeatureCollection
import com.cocoahero.android.geojson.GeoJSON
import com.cocoahero.android.geojson.LineString
import com.example.routequest.di.AssistedViewModelFactory
import com.example.routequest.di.ViewModelKey
import com.example.routequest.routing.data.DistanceResolver
import com.example.routequest.routing.data.RouteRepository
import com.example.routequest.routing.domain.Coordinates
import com.example.routequest.routing.domain.Route
import com.example.routequest.routing.interactors.GetDistance
import com.example.routequest.routing.interactors.GetRoute
import com.example.routequest.routing.interactors.Interactor
import com.example.routequest.routing.ui.RoutingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
object RoutingModule {
    @Provides
    @IntoMap
    @ViewModelKey(RoutingViewModel::class)
    fun routingViewModelFactory(factory: RoutingViewModel.Factory):
            AssistedViewModelFactory<*, *> = factory

    @Provides
    fun routingInteractor(
        routeRepository: RouteRepository,
        distanceResolver: DistanceResolver
    ): Interactor =
        Interactor(GetRoute(routeRepository), GetDistance(distanceResolver))

    @Provides
    fun routeRepository(): RouteRepository =
        RouteRepositoryStub

    @Provides
    fun distanceResolver(): DistanceResolver =
        HaversineDistanceResolver
}

/*
 * Стаб некого сервера с которого мы берем маршрут в виде GeoJson
 * Естественно когда предоставять этот сервер тут будет и использование Retrofit
 */
object RouteRepositoryStub : RouteRepository {
    private val geoJson =
        ("{" +
                "  \"type\": \"FeatureCollection\"," +
                "  \"features\": [" +
                "    {" +
                "      \"type\": \"Feature\"," +
                "      \"properties\": {}," +
                "      \"geometry\": {" +
                "        \"type\": \"LineString\"," +
                "        \"coordinates\": [" +
                "          [" +
                "            44.516666," +
                "            48.700001" +
                "          ]," +
                "          [" +
                "            37.6173," +
                "            55.7558" +
                "          ]," +
                "          [" +
                "            30.308611," +
                "            59.937500" +
                "          ]" +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}").let { GeoJSON.parse(it) } as FeatureCollection

    override suspend fun getRoute(): Route =
        mutableListOf<Coordinates>().apply {
            geoJson.features.forEach { feature ->
                if (feature.geometry.type == "LineString") {
                    (feature.geometry as LineString).positions.forEach { position ->
                        add(Coordinates(position.longitude, position.latitude))
                    }
                }
            }
        }.asRoute()

    /*
     * Обратный порядок обхода полезен для алгоритмов поиска оптимальных путей
     */
    private fun List<Coordinates>.asRoute(): Route =
        fold(Route.Start(first()) as Route) { route, point ->
            Route.Next(route, point)
        }
}
