package com.lpfr3d.heythere.database.db_room.repository.mensagem

import com.lpfr3d.heythere.database.db_room.dao.MensagemDAO
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade

class MensagemRepositorio(private val mensagemDAO: MensagemDAO) {

    suspend fun insert(mensagem: MensagemEntidade):Long{
        return mensagemDAO.salvarMensagem(mensagem)
    }

}