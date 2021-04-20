package com.lpfr3d.heythere.ui.buscar_salas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lpfr3d.heythere.database.models.SalaModel
import com.lpfr3d.heythere.database.retrofit.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarSalaViewModel(private val service: Api) : ViewModel() {
    val listaDeSalasEncontradas = MutableLiveData<List<SalaModel>>()

    fun buscarSalas(local: String): LiveData<List<SalaModel>> {
        val call = service.buscarSalas(local)
        call.enqueue(object : Callback<List<SalaModel>> {
            override fun onResponse(
                call: Call<List<SalaModel>>,
                response: Response<List<SalaModel>>
            ) {
                if (response.isSuccessful) {
                    listaDeSalasEncontradas.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<SalaModel>>, t: Throwable) {
                println("erro" + t.message)
            }

        })
        return listaDeSalasEncontradas
    }
}