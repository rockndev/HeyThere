package com.lpfr3d.heythere.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.lpfr3d.heythere.database.models.SalaModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun horaAtualEmUTC(): String {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
}

object MinhasSalas {
    val sala1 = SalaModel(
        nomeSala = "Nova York",
        quantidadeDePessoas = 100,
        urlImagem = "",
        id = "6"
    )

    val sala2 = SalaModel(
        nomeSala = "Paris",
        quantidadeDePessoas = 50,
        urlImagem = "",
        id = "4"
    )

    val minhasSalas = listOf(sala1, sala2)
}

