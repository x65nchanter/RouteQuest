package com.example.routequest.routing.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class RouteTest {
    @Test
    fun fold_on_start_apply_accumulator_one_time() {
        val init = 42.0
        val start = Route.Start(Coordinates(.1,.0))
        val accumulator: (Double, Route) -> Double = { acc, cur ->
            acc * cur.coordinates.longitude
        }
        runBlocking {
            val oneTime = accumulator(init, start)
            val foldResult = start.fold(init, accumulator)
            assertEquals(oneTime, foldResult, .0)
        }
    }

    @Test
    fun fold_on_next_start_apply_accumulator_two_time() {
        val init = 42.0
        val start = Route.Start(Coordinates(.1,.0))
        val next = Route.Next(start, Coordinates(.2,.0))
        val accumulator: (Double, Route) -> Double = { acc, cur ->
            acc * cur.coordinates.longitude
        }
        runBlocking {
            val twoTime = accumulator(accumulator(init, start), next)
            val foldResult = next.fold(init, accumulator)
            assertEquals(twoTime, foldResult, .0)
        }
    }
}