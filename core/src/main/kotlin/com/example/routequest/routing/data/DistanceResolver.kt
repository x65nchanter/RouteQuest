package com.example.routequest.routing.data

import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.DistanceProvider
import com.example.routequest.routing.domain.Route

fun interface DistanceResolver {
    fun resolve(route: Route): DistanceProvider
}

suspend operator fun DistanceResolver.invoke(route: Route): Distance = resolve(route)()