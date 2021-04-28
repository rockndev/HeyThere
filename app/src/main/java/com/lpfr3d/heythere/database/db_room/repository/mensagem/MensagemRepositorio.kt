package com.lpfr3d.heythere.database.db_room.repository.mensagem

import com.lpfr3d.heythere.database.db_room.dao.MensagemDAO
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade

class MensagemRepositorio(private val mensagemDAO: MensagemDAO) {

    suspend fun insert(mensagem: MensagemEntidade): Long {
        return mensagemDAO.salvarMensagem(mensagem)
    }

    suspend fun inserirListaDeMensagens(mensagens: MutableList<MensagemEntidade>) {
        return mensagemDAO.salvarListaDeMensagens(mensagens)
    }

    suspend fun carregarMensagens(idSala: Int): MutableList<MensagemEntidade> {
        return mensagemDAO.carregarMensagens(idSala)
    }


}