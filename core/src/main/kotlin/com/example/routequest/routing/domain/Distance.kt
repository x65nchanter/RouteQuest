package com.example.routequest.routing.domain

sealed class Distance {
    data class Meters(val distance: Double): Distance()
    data class MetersWithSeconds(val distance: Double, val duration: Double): Distance()
}
