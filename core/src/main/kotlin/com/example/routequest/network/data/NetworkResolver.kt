package com.example.routequest.network.data

import com.example.routequest.network.domain.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkResolver {
    fun resolve(): Flow<NetworkStatus>
}