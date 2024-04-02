package com.joshi.mvvmwithjetpackcompose.model.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.http.UrlRequest
import com.joshi.mvvmwithjetpackcompose.SingletoneHolder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityMonitor private constructor(context: Context) : ConnectivityObserver {

    companion object : SingletoneHolder<ConnectivityMonitor, Context>(::ConnectivityMonitor)

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectivityObserver.Status.Available) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectivityObserver.Status.UnAvailable) }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        if (connectivityManager.activeNetwork == null)
            launch { send(ConnectivityObserver.Status.UnAvailable) }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }.distinctUntilChanged()


}