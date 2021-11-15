package com.example.routequest.network

import com.example.routequest.network.data.NetworkResolver
import com.example.routequest.network.interactors.GetInternetConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
