package com.example.routequest.routing.domain

fun interface DistanceResolver {
    suspend operator fun invoke(): Distance
}