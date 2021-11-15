package com.example.routequest.routing.data

import com.example.routequest.routing.domain.DistanceResolver
import com.example.routequest.routing.domain.Route

fun interface DistanceResolverProvider {
    fun provide(route: Route): DistanceResolver
}