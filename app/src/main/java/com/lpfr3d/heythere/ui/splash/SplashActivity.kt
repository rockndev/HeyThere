package com.lpfr3d.heythere.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lpfr3d.heythere.databinding.ActivitySplashBinding
import com.lpfr3d.heythere.main.MainActivity
import com.lpfr3d.heythere.ui.login.LoginActivity
import com.lpfr3d.heythere.utils.GerenciadorDeSessao

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var gerenciadorDeSessao: GerenciadorDeSessao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        gerenciadorDeSessao = GerenciadorDeSessao(this)
        animarSplashScreen()

    }

    private fun animarSplashScreen() {
        binding.lottieFoguete.run {
            alpha = 0f
            animate().setDuration(3000).alpha(1f).withEndAction {

                if (verificarSeExisteUsuarioLogado()) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }

    private fun verificarSeExisteUsuarioLogado(): Boolean {
        return gerenciadorDeSessao.recuperarTokenNaShared() != null
    }

}