package com.lpfr3d.heythere.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.db_room.AppDataBase
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import com.lpfr3d.heythere.databinding.ActivityMainBinding
import com.lpfr3d.heythere.utils.horaAtualEmUTC

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(
                    mensagemRepositorio = MensagemRepositorio(
                        AppDataBase.getDatabse(
                            applicationContext
                        ).mensagemDao()
                    )
                ) as T
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lerHorarioDeSarida()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_top_salas,
                R.id.navigation_minhas_salas,
                R.id.navigation_buscar_sala,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            //Remove a bottom navigation dos seguintes fragmentos
            viewModel.fragmentoAtualSala.value = destination.id == R.id.navigation_sala
        }

        viewModel.conectarSocket()

    }

    private fun lerHorarioDeSarida() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        viewModel.horarioUltimoLogout =
            sharedPreferences.getString("horario_de_logout", null)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun salvarHorarioDeSaida() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("horario_de_logout", horaAtualEmUTC())
        }.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        println("DESTROY CHAMADO")
        salvarHorarioDeSaida()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        println("PAUSE CHAMADO")
        salvarHorarioDeSaida()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()
        println("STOP CHAMADO")
        salvarHorarioDeSaida()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}