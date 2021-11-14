package com.example.routequest.network

import com.example.routequest.network.data.NetworkResolver
import com.example.routequest.network.domain.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.beryukhov.reactivenetwork.ReactiveNetwork

class ReactiveNetworkResolver(private val reactiveNetwork: ReactiveNetwork) : NetworkResolver {
    override fun resolve(): Flow<NetworkStatus> =
        reactiveNetwork
            .observeInternetConnectivity()
            .flowOn(Dispatchers.IO)
            .map { if (it) NetworkStatus.AVAILABLE else NetworkStatus.NO_INTERNET }
}
