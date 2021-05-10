package com.lpfr3d.heythere.database.retrofit


import com.lpfr3d.heythere.database.models.RegistroModel
import com.lpfr3d.heythere.database.models.SalaModel
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body registro: RegistroModel)

    @GET("api/salas")
    suspend fun listarSalas(): List<SalaModel>

    @GET("api/salas/buscar?nome")
    fun buscarSalas(@Query("nome") local: String): Call<List<SalaModel>>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("senha") senha: String,
        @Field("tipo") tipo: String
    ): RespostaLogin


}
