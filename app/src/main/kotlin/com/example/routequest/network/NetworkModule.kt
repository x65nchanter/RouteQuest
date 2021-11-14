package com.example.routequest.network

import com.example.routequest.di.AssistedViewModelFactory
import com.example.routequest.di.ViewModelKey
import com.example.routequest.network.data.NetworkResolver
import com.example.routequest.network.interactors.GetInternetConnectivity
import com.example.routequest.routing.HaversineDistanceResolver
import com.example.routequest.routing.RouteRepositoryStub
import com.example.routequest.routing.data.DistanceResolver
import com.example.routequest.routing.data.RouteRepository
import com.example.routequest.routing.domain.*
import com.example.routequest.routing.interactors.GetDistance
import com.example.routequest.routing.interactors.GetRoute
import com.example.routequest.routing.interactors.Interactor
import com.example.routequest.routing.ui.RoutingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.beryukhov.reactivenetwork.ReactiveNetwork

@Module
@InstallIn(SingletonComponent::class)
object RoutingModule {
    @Provides
    fun reactiveNetwork(): ReactiveNetwork =
        ReactiveNetwork()

    @Provides
    fun networkResolver(reactiveNetwork: ReactiveNetwork): NetworkResolver =
        ReactiveNetworkResolver(reactiveNetwork)

    @Provides
    fun getInternetConnectivity(networkResolver: NetworkResolver): GetInternetConnectivity =
        GetInternetConnectivity(networkResolver)
}
