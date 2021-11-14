package com.example.routequest.routing.domain

/*
 *  Слой домена
 */

/*
 *  Просто захотел обявить свой монойд
 */
sealed class Route(val coordinates: Coordinates) {
    class Start(coordinates: Coordinates) :
        Route(coordinates)

    class Next(val prev: Route, coordinates: Coordinates) :
        Route(coordinates)


    fun <T> fold(init: T, accumulator: (acc: T, current: Route) -> T): T {
        tailrec fun iterator(
            acc: T,
            current: Route,
            accumulator: (acc: T, current: Route) -> T
        ): T =
            when (current) {
                is Next -> iterator(accumulator(acc, current), current.prev, accumulator)
                else -> accumulator(acc, current)
            }

        return iterator(init, this, accumulator)
    }

    companion object
}


tailrec fun Route.foreach(onEach: (Route) -> Unit): Unit =
    when (this) {
        is Route.Next -> {
            onEach(this)
            prev.foreach(onEach)
        }
        else -> onEach(this)
    }
