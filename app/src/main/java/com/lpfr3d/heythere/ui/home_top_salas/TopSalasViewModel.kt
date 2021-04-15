package com.lpfr3d.heythere.ui.home_top_salas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lpfr3d.heythere.Resource
import com.lpfr3d.heythere.database.retrofit.Api
import kotlinx.coroutines.Dispatchers

class TopSalasViewModel(private val service: Api) : ViewModel() {
    fun listarSalas() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = service.listarSalas()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Falha"))
        }
    }
}