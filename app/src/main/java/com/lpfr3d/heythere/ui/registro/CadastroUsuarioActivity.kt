package com.lpfr3d.heythere.ui.registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.Status
import com.lpfr3d.heythere.database.registro.RegistroModel
import com.lpfr3d.heythere.database.retrofit.RetrofitClient.instance
import com.lpfr3d.heythere.databinding.ActivityCadastroUsuarioBinding

class CadastroUsuarioActivity : AppCompatActivity() {

    private val binding: ActivityCadastroUsuarioBinding by viewBinding()
    private val viewModel by viewModels<CadastroUsuarioViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CadastroUsuarioViewModel(instance) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        binding.tietNome.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 2) {
                binding.tilNome.error = "Informe pelo menos 3 caracteres"
            } else if (text.length > 2) {
                binding.tilNome.error = null
            }
        }

        binding.btFinalizarCadastro.setOnClickListener {

            val x = RegistroModel(
                "Lassdfsddo",
                "luisdfdsz@gmail.com",
                "1234567",
                "2019-12-12",
                "M",
                "BRASIL"
            )
            viewModel.registrarUsuario(x).observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCESSO -> println("deu bom")
                        Status.ERRO -> println(resource.message)
                        Status.CARREGANDO -> println("loading")

                    }

                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        val sexo = resources.getStringArray(R.array.sexo)
        val arrayAdapterSexo = ArrayAdapter(this, R.layout.dropdown_item, sexo)
        binding.actvSexo.setAdapter(arrayAdapterSexo)
    }
}