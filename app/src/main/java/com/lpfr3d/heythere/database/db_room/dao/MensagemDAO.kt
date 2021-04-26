package com.lpfr3d.heythere.database.db_room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade

@Dao
interface MensagemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarMensagem(mensagem : MensagemEntidade) : Long
}