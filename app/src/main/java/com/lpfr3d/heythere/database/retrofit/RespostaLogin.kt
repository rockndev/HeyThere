package com.lpfr3d.heythere.database.retrofit

import com.google.gson.annotations.SerializedName
import com.lpfr3d.heythere.database.models.SalaModel

data class RespostaLogin(
    @SerializedName("token")
    var token: String,
    @SerializedName("salas")
    var listaDeSalasDoUsuarui:List<SalaModel>
)

