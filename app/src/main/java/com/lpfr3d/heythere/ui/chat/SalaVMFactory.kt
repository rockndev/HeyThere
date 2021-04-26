package com.lpfr3d.heythere.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio

class SalaVMFactory(private val repository: MensagemRepositorio): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SalaViewModel::class.java)){
            return SalaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}