package com.lpfr3d.heythere.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lpfr3d.heythere.database.retrofit.RetrofitClient
import com.lpfr3d.heythere.databinding.ActivityLoginBinding
import com.lpfr3d.heythere.main.MainActivity
import com.lpfr3d.heythere.ui.registro.CadastroUsuarioActivity
import com.lpfr3d.heythere.utils.GerenciadorDeSessao
import com.lpfr3d.heythere.utils.Status
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var gerenciadorDeSessao: GerenciadorDeSessao
    private val viewModel by viewModels<LoginViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(RetrofitClient.getApiService(applicationContext)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btRegistrarUsuario.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }


        binding.btLogin.setOnClickListener {
            viewModel.login(
                binding.etLoginUsuario.text.toString().trim().toLowerCase(Locale.ROOT),
                binding.etLoginSenha.text.toString().trim()
            ).observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCESSO -> {
                            if (binding.cbSalvarLogin.isChecked) {
                                gerenciadorDeSessao.salvarTokenNaShared(resource.data!!.token)
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        Status.ERRO -> println(resource.message)
                        Status.CARREGANDO -> println("loading")
                    }
                }
            })
        }
    }

}