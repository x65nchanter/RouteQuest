package com.example.routequest.routing.data

import com.example.routequest.routing.domain.Route

interface RouteRepository {
    suspend fun getRoute(): Route
}