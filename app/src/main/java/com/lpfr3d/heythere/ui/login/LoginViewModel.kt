package com.lpfr3d.heythere.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lpfr3d.heythere.database.retrofit.Api
import com.lpfr3d.heythere.utils.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val service: Api) : ViewModel() {

    fun login(email: String, senha: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = service.login(email, senha, "BASICO")))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Falha"))
            }
        }

}