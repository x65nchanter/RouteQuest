package com.example.routequest.routing.domain

/*
 *  Слой домена
 */

/*
 *  Просто захотел обявить свой монойд ничего особенного
 */
sealed class Route(val coordinates: Coordinates) {
    class Start(coordinates: Coordinates) :
        Route(coordinates)

    class Next(val prev: Route, coordinates: Coordinates) :
        Route(coordinates)
}

/*
 * Просто паттерн Итератор ничего интересного
 */
operator fun Route.iterator(): Iterator<Route> =
    object : Iterator<Route> {
        private var next: Route = this@iterator
        private var current: Route? = null
        override fun hasNext(): Boolean = current is Route.Next || current == null

        override fun next(): Route = when (next) {
            is Route.Next -> (next as Route.Next).also {
                next = it.prev
                current = it
            }
            is Route.Start -> (next as Route.Start).also {
                current = it
            }
        }
    }

/*
 * Шорт кат до фолда :d все равное его уже реолизовали для сиквенсов
 */
fun <T> Route.fold(init: T, operation: (acc: T, Route) -> T): T =
    iterator().asSequence().fold(init, operation)

/*
 * Бедный колтин смарт каст несправляется с подобными конверсиями
 */
fun Route.start(): Route.Start = fold(this) { _, next -> next } as Route.Start
