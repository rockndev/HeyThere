package com.lpfr3d.heythere.ui.registro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.gistech.libs.countrieskit.LibCK
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.models.RegistroModel
import com.lpfr3d.heythere.database.retrofit.RetrofitClient
import com.lpfr3d.heythere.databinding.ActivityCadastroUsuarioBinding
import com.lpfr3d.heythere.ui.login.LoginActivity
import com.lpfr3d.heythere.utils.Status
import java.text.SimpleDateFormat
import java.util.*

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var dataDeNascimentoFormatoUniversal: String
    var formatUniversal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val binding: ActivityCadastroUsuarioBinding by viewBinding()
    private val viewModel by viewModels<CadastroUsuarioViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CadastroUsuarioViewModel(RetrofitClient.getApiService(applicationContext)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        selecionarDataDeNascimento()

        binding.tietNome.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 2) {
                binding.tilNome.error = "Informe pelo menos 3 caracteres"
            } else if (text.length > 2) {
                binding.tilNome.error = null
            }
        }

        finalizarCadastro()

    }

    private fun finalizarCadastro() {
        binding.btFinalizarCadastro.setOnClickListener {

            val nome = binding.tietNome.text.toString().trim()
            val email = binding.tietEmail.text.toString().trim().toLowerCase(Locale.ROOT)
            val senha = binding.tietSenha.text.toString().trim()
            val dataDeNascimento = dataDeNascimentoFormatoUniversal
            val codigoDoPais = recuperarCodigoDoPais(binding.actvPais.text.toString().trim())
            val sexo = selecionarSexo()

            val x = RegistroModel(
                nome,
                email,
                senha,
                dataDeNascimento,
                sexo.toString(),
                codigoDoPais
            )

            viewModel.registrarUsuario(x).observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCESSO -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        Status.ERRO -> println(resource.message)
                        Status.CARREGANDO -> println("loading")

                    }

                }
            })
        }
    }

    private fun recuperarCodigoDoPais(nomePais: String): String {
        val lick = LibCK.getCountriesList(this)
        var codigoDoPais = ""
        lick.forEach {
            if (nomePais == it.countryName)
                codigoDoPais = it.countryCode
        }
        return codigoDoPais
    }

    private fun selecionarSexo(): Char {
        return if (binding.actvSexo.text.toString() == "Masculino")
            'M'
        else
            'F'
    }

    private fun selecionarDataDeNascimento() {
        binding.etCadastroDataNascimento.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datepicker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Dialog,
                    { datePicker, i, i2, i3 ->
                        val selectDate = Calendar.getInstance()
                        selectDate.set(Calendar.YEAR, i)
                        selectDate.set(Calendar.MONTH, i2)
                        selectDate.set(Calendar.DAY_OF_MONTH, i3)
                        val date = formatDate.format(selectDate.time)
                        dataDeNascimentoFormatoUniversal = formatUniversal.format(selectDate.time)
                        binding.etCadastroDataNascimento.setText(date)
                    },
                    getDate.get(Calendar.YEAR),
                    getDate.get(Calendar.MONTH),
                    getDate.get(Calendar.DAY_OF_MONTH)
                )
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            datepicker.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val sexo = resources.getStringArray(R.array.sexo)
        val arrayAdapterSexo = ArrayAdapter(this, R.layout.dropdown_item, sexo)
        binding.actvSexo.setAdapter(arrayAdapterSexo)

        val lick = LibCK.getCountriesList(this)
        val a = mutableListOf<String>()
        lick.forEach {
            a.add(it.countryName)
        }

        val arrayAdapterPaises = ArrayAdapter(this, android.R.layout.simple_list_item_1, a)
        binding.actvPais.threshold = 0
        binding.actvPais.setAdapter(arrayAdapterPaises)
    }

}