package com.example.medibank.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

/*
    Parent Class for other fragments
 */
open class BaseFragment : Fragment() {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.isActiveNetworkMetered
    }

}
