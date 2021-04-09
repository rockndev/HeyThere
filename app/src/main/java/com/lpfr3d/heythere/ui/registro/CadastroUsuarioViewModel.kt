package com.lpfr3d.heythere.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.lpfr3d.heythere.Resource
import com.lpfr3d.heythere.database.retrofit.Api
import com.lpfr3d.heythere.database.registro.RegistroModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CadastroUsuarioViewModel(private val service : Api) : ViewModel() {


    fun registrarUsuario(registro : RegistroModel)= liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data=service.registrarUsuario(registro)))
        }catch (exception: Exception){
            emit(Resource.error(data=null,message = exception.message?:"Error occured"))
        }
    }
}