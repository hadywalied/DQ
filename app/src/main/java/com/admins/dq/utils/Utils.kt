package com.admins.dq.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object Utils {

    fun checkforNetwork(mContext: Context): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected
    }

}