package com.lpfr3d.heythere.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lpfr3d.heythere.database.models.RegistroModel
import com.lpfr3d.heythere.database.retrofit.Api
import com.lpfr3d.heythere.utils.Resource
import kotlinx.coroutines.Dispatchers

class CadastroUsuarioViewModel(private val service: Api) : ViewModel() {

    fun registrarUsuario(registro: RegistroModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = service.registrarUsuario(registro)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occured"))
        }
    }
}