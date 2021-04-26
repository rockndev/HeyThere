package com.lpfr3d.heythere.database.db_room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensagem")
data class MensagemEntidade(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val conteudo: String,
    val remetente: String,
    val sala: Int,
    val horario: String
)