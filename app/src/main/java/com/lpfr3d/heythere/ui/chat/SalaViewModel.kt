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

    private val _listaDeMensagensArmazenadasNoBancoLocal = MutableLiveData<MutableList<MensagemEntidade>>()
    val listaDeMensagensArmazenadasNoBancoLocal: LiveData<MutableList<MensagemEntidade>>
        get() = _listaDeMensagensArmazenadasNoBancoLocal

    fun coletarListaDeMensagensArmazenadasNoBancoLocal(idSala: Int) {
        viewModelScope.launch {
            try {
                _listaDeMensagensArmazenadasNoBancoLocal.postValue(mensagemRepositorio.carregarMensagens(idSala))
            } catch (e: Exception) {
                Log.d("Erro de Serviço", e.message.toString())
            }
        }
    }


}