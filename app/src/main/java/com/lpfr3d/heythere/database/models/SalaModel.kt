package com.lpfr3d.heythere.database.models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class SalaModel(
    @SerializedName("nome") var nomeSala: String,
    @SerializedName("qtd_pessoas") var quantidadeDePessoas: Int,
    @SerializedName("url_imagem") var urlImagem: String,
    @SerializedName("id") var id: String

)
