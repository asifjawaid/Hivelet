package com.celeritassolutions.hivelet.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log

class SP(context: Context) {
    var preferences: SharedPreferences

    internal var editor: SharedPreferences.Editor

    init {
        SP.context = context
        preferences = context.getSharedPreferences("utildata", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun savedata(key: String, `val`: String) {
        editor.putString(key, `val`).commit()
    }

    fun getdata(key: String): String {
        val value = preferences.getString(key, "")
        if (value!!.isEmpty()) {
            Log.i(TAG, "$key not found.")
            return ""
        }
        return value
    }

    companion object {
        internal lateinit var context: Context
        val TAG = "SP"
    }
}