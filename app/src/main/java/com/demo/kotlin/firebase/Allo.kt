package com.demo.kotlin.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast

class Allo {

    companion object {

        const val CUBE = "cube"
        const val CUBE_DEBUG = true

        const val CUBE_LINK = "link"
        const val CUBE_TOKEN = "token"

        fun i (message: String?) {
            if (CUBE_DEBUG) Log.i (CUBE, message!!)
        }
        fun t (context: Context?, message: String?) {
            if (CUBE_DEBUG) Toast.makeText (context, message, Toast.LENGTH_SHORT).show ()
        }
    }
}