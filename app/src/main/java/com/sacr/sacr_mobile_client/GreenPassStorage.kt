package com.sacr.sacr_mobile_client

import android.content.Context

class GreenPassStorage {
    companion object {
        private const val invalidCode: String = "NOT VALID"
        private val greenPassLock = Object()

        private var abc = "ciao"

        var code: String? = null

        fun setGreenPass(ctx: Context, code: String) {
            abc = code
            synchronized(greenPassLock) {
                val preferences = ctx.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
                preferences.edit().putString("green_pass", code).commit()
            }
        }

        fun resetGreenPass(ctx: Context) {
            synchronized(greenPassLock) {
                val preferences = ctx.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
                preferences.edit().putString("green_pass", invalidCode).commit()
                code = null
            }
        }

        fun getGreenPass(ctx: Context): String {
            synchronized(greenPassLock) {
                val preferences = ctx.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
                code = preferences.getString("green_pass", null)
            }
            return code!!
        }
    }
}