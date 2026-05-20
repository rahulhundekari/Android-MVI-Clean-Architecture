package com.demo.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.demo.domain.util.NetworkMonitor
import com.demo.domain.util.NetworkState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class NetworkMonitorImpl(
    appContext: Context
) : NetworkMonitor {

    private val connectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var lastKnownStatusWasOffline = isNetworkAvailable().not()

    override val networkState: Flow<NetworkState> = callbackFlow {

        launch { send(createNetworkStatus(isNetworkAvailable())) }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkStatus = createNetworkStatus(true)
                launch { send(networkStatus) }
                lastKnownStatusWasOffline = false
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                val networkStatus = createNetworkStatus(false)
                launch { send(networkStatus) }
                lastKnownStatusWasOffline = true
            }

        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }


    private fun createNetworkStatus(isOnline: Boolean): NetworkState {
        val shouldRefresh = lastKnownStatusWasOffline && isOnline
        return NetworkState(isOnline = isOnline, shouldRefresh = shouldRefresh)
    }

    fun isNetworkAvailable(): Boolean {
        return connectivityManager.activeNetwork != null
    }
}