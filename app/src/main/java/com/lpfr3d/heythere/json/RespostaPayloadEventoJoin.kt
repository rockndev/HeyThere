package com.lpfr3d.heythere.json


import com.google.gson.annotations.SerializedName

data class RespostaPayloadEventoJoin(
    var response: Response,
    var status: String
)

data class Mensagen(
    var conteudo: String,
    @SerializedName("h_envio")
    var hEnvio: String,
    var usuario: Usuario
)

data class Response(
    var mensagens: List<Mensagen>,
    var sala: Sala,
    var usuarios: List<UsuarioX>
)

data class Sala(
    var nome: String,
    @SerializedName("qtd_pessoas")
    var qtdPessoas: String
)

data class Usuario(
    var nacionalidade: String,
    var nome: String
)

data class UsuarioX(
    var usuario: UsuarioXX
)

data class UsuarioXX(
    var nacionalidade: String,
    var nome: String
)