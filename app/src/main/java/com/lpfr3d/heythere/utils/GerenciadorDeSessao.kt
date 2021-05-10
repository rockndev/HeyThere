package com.lpfr3d.heythere.utils

import android.content.Context
import android.content.SharedPreferences
import com.lpfr3d.heythere.R

class GerenciadorDeSessao(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun salvarTokenNaShared(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun recuperarTokenNaShared(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}