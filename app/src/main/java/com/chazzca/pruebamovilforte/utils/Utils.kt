package com.chazzca.pruebamovilforte.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Utils{
    companion object{
        //val urlBase = "http://10.0.2.2:8080"
        val urlBase = "http://apps01.forteinnovation.mx:8590"

        val TOKEN_KEY = "TOKEN_KEY"
        val USER_KEY = "USER_KEY"

        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }

}


