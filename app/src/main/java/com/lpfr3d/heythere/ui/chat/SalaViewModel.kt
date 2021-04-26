package com.lpfr3d.heythere.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import kotlinx.coroutines.launch

class SalaViewModel(private val mensagemRepositorio: MensagemRepositorio) : ViewModel() {

    fun salvarMensagem(mensagem: MensagemEntidade){
        viewModelScope.launch {
            mensagemRepositorio.insert(mensagem)
        }
    }

}