package com.example.routequest.network.interactors

import com.example.routequest.network.data.NetworkResolver
import com.example.routequest.network.domain.NetworkStatus
import kotlinx.coroutines.flow.Flow

class GetInternetConnectivity(
    private val networkResolver: NetworkResolver
) {
    fun execute(): Flow<NetworkStatus> = networkResolver.resolve()
}