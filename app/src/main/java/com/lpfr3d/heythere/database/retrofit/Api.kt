package com.lpfr3d.heythere.database.retrofit


import com.lpfr3d.heythere.database.models.RegistroModel
import com.lpfr3d.heythere.database.models.SalaModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface Api {
    @POST("usuarios")
    suspend fun registrarUsuario(@Body registro: RegistroModel)

    @GET("salas")
    suspend fun listarSalas() : List<SalaModel>

}
