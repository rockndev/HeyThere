package com.lpfr3d.heythere.ui.chat

import com.google.gson.annotations.SerializedName

data class Response (
    val mensagens: List<Mensagen>
)

data class Mensagen (
    val conteudo: String,
    val usuario: Usuario,

    @SerializedName("h_envio")
    val hEnvio: String
)

data class Usuario (
    val nome: String,
    val nacionalidade: String
)