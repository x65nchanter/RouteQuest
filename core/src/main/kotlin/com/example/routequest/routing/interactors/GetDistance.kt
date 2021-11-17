package com.example.routequest.routing.interactors

import com.example.routequest.routing.data.DistanceResolver
import com.example.routequest.routing.data.invoke
import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route

class GetDistance(private val resolver: DistanceResolver) {
    suspend fun execute(route: Route): Distance =
        resolver(route)
}