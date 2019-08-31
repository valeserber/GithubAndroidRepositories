package com.valeserber.githubchallenge.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi


class ConnectivityLiveData internal constructor(private val connectivityManager: ConnectivityManager)
    : LiveData<Boolean>() {

    constructor(application: Application) : this(application.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager)

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            postValue(true)
        }

        override fun onLost(network: Network?) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}