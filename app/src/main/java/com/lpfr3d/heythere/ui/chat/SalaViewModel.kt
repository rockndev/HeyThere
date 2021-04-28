package com.lpfr3d.heythere.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import kotlinx.coroutines.launch

class SalaViewModel(private val mensagemRepositorio: MensagemRepositorio) : ViewModel() {

    fun salvarMensagem(mensagem: MensagemEntidade) {
        viewModelScope.launch {
            mensagemRepositorio.insert(mensagem)
        }
    }

    fun salvarListaDeMensagens(mensagens: MutableList<MensagemEntidade>) {
        viewModelScope.launch {
            mensagemRepositorio.inserirListaDeMensagens(mensagens)
        }
    }

    private val _fotoDoDia = MutableLiveData<MutableList<MensagemEntidade>>()
    val fotoDoDia: LiveData<MutableList<MensagemEntidade>>
        get() = _fotoDoDia

    fun coletarFotoDoDia(idSala: Int) {
        viewModelScope.launch {
            try {
                _fotoDoDia.postValue(mensagemRepositorio.carregarMensagens(idSala))
            } catch (e: Exception) {
                Log.d("Erro de Serviço", e.message.toString())
            }
        }
    }

}