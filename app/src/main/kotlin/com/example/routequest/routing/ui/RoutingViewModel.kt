package com.example.routequest.routing.ui

import com.airbnb.mvrx.*
import com.example.routequest.di.AssistedViewModelFactory
import com.example.routequest.di.hiltMavericksViewModelFactory
import com.example.routequest.network.domain.NetworkStatus
import com.example.routequest.network.interactors.GetInternetConnectivity
import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.interactors.Interactor
import com.example.routequest.routing.domain.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class RoutingViewModel @AssistedInject constructor(
    interactor: Interactor,
    getInternetConnectivity: GetInternetConnectivity,
    @Assisted initialState: RoutingState
) : MavericksViewModel<RoutingState>(initialState) {

    init {
        getInternetConnectivity.execute()
            .map { status ->
                if (status == NetworkStatus.AVAILABLE) {
                    val route = interactor.getRoute()
                    val distance = interactor.getDistance(route)
                    StateEffect.RouteAndDistance(route, distance)
                } else {
                    StateEffect.IsOffline
                }
            }
            .execute { status ->
                when (status) {
                    is Success -> status().run { reduce() }
                    is Loading -> copy(isLoading = true)
                    is Fail -> copy(
                        //TODO: Exception mapping
                        errorMessage = status.error.localizedMessage
                    )
                    else -> this
                }
            }
    }

    sealed class StateEffect {
        object IsOffline: StateEffect()
        data class RouteAndDistance(val route: Route, val distance: Distance): StateEffect()

        fun RoutingState.reduce(): RoutingState =
            when (this@StateEffect) {
                is IsOffline -> copy(isOnline = false, isLoading = false)
                is RouteAndDistance -> copy(
                    route = this@StateEffect.route,
                    distance = this@StateEffect.distance,
                    isOnline = true,
                    isLoading = false,
                )
            }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RoutingViewModel, RoutingState> {
        override fun create(state: RoutingState): RoutingViewModel
    }

    companion object :
        MavericksViewModelFactory
        <RoutingViewModel, RoutingState> by hiltMavericksViewModelFactory()
}

data class RoutingState(
    val route: Route? = null,
    val distance: Distance? = null,
    val isOnline: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) : MavericksState
