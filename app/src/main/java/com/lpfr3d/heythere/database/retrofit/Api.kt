package com.lpfr3d.heythere.database.retrofit


import com.lpfr3d.heythere.database.registro.RegistroModel
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST


interface Api {
    @POST("usuarios")
    suspend fun registrarUsuario(@Body registro : RegistroModel)

}
