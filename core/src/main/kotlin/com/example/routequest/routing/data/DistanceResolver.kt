package com.example.routequest.routing.data

import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route

fun interface DistanceResolver {
    suspend fun resolve(route: Route): Distance
}