package com.example.routequest.routing

import com.example.routequest.routing.domain.Coordinates
import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class HaversineDistanceProviderTest {
    @Test
    fun distance_between_moscow_and_volgograd() {
        val maxDelta = 250.0
        val moscow = Coordinates(37.6173,55.7558)
        val volgograd = Coordinates(44.516666,48.700001)
        val moscowVolgogradDistanceKilometer = 970.0
        val route = Route.Next(Route.Start(moscow), volgograd)
        runBlocking {
            val distanceKilometer =
                (HaversineDistanceProvider.provide(route)() as Distance.Meters).distance / 1000.0
            assertEquals(moscowVolgogradDistanceKilometer, distanceKilometer, maxDelta)
        }
    }

    @Test
    fun distance_between_volgograd_and_saint_petersburg() {
        val maxDelta = 250.0
        val volgograd = Coordinates(44.516666,48.700001)
        val petersburg = Coordinates(30.308611,59.937500)
        val volgogradPetersburgDistanceKilometer = 1680.9
        val route = Route.Next(Route.Start(volgograd), petersburg)
        runBlocking {
            val distanceKilometer =
                (HaversineDistanceProvider.provide(route)() as Distance.Meters).distance / 1000.0
            assertEquals(volgogradPetersburgDistanceKilometer, distanceKilometer, maxDelta)
        }
    }

    @Test
    fun route_length_from_moscow_to_volgograd_to_saint_petersburg() {
        val maxDelta = 250.0
        val moscow = Coordinates(37.6173,55.7558)
        val volgograd = Coordinates(44.516666,48.700001)
        val petersburg = Coordinates(30.308611,59.937500)
        val routeLengthKilometer = 970.0+1680.9
        val route = Route.Next(Route.Next(Route.Start(moscow), volgograd), petersburg)
        runBlocking {
            val distanceKilometer =
                (HaversineDistanceProvider.provide(route)() as Distance.Meters).distance / 1000.0
            assertEquals(routeLengthKilometer, distanceKilometer, maxDelta)
        }
    }
}