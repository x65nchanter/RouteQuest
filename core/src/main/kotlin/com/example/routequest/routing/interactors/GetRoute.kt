package com.example.routequest.routing.interactors

import com.example.routequest.routing.data.RouteRepository
import com.example.routequest.routing.domain.Route

class GetRoute(
    private val routeRepository: RouteRepository
) {
    suspend fun execute(): Route =
        routeRepository.getRoute()
}