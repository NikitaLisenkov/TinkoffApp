package com.example.app.presentation.network_listener

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
// TODO fix blinking
class NetworkListener @Inject constructor(
    private val manager: ConnectivityManager
) {

    private val _isConnectedFlow = MutableStateFlow<Boolean>(false)
    val isConnectedFlow: StateFlow<Boolean> = _isConnectedFlow

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnectedFlow.value = true
        }

        override fun onLost(network: Network) {
            _isConnectedFlow.value = false
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            val isConnected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            _isConnectedFlow.value = isConnected
        }
    }

    fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        manager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregisterNetworkCallback() {
        manager.unregisterNetworkCallback(networkCallback)
    }

}