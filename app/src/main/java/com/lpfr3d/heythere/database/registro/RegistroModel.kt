package com.lpfr3d.heythere.database.registro

import retrofit2.http.Field

data class RegistroModel(
    @Field("nome") var nome: String,
    @Field("email")var email: String,
    @Field("senha")var senha: String,
    @Field("data_nascimento")var data_nascimento: String,
    @Field("sexo")var sexo: String,
    @Field("nacionalide")var nacionalidade: String
)
