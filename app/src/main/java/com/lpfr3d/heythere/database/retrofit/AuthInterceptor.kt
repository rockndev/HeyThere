package com.lpfr3d.heythere.database.retrofit

import android.content.Context
import com.lpfr3d.heythere.utils.GerenciadorDeSessao
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val gerenciadorDeSessao = GerenciadorDeSessao(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        gerenciadorDeSessao.recuperarTokenNaShared()?.let {
            println("TOKEN 1 : $it")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}