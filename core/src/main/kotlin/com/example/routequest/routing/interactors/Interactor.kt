package com.example.routequest.routing.interactors

import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route

class Interactor(
    private val getRoute: GetRoute,
    private val getDistance: GetDistance,
) {
    suspend fun getRoute(): Route = getRoute.execute()

    suspend fun getDistance(route: Route): Distance = getDistance.execute(route)
}