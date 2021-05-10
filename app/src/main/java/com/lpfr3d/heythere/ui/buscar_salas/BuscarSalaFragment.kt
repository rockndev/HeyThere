package com.lpfr3d.heythere.ui.buscar_salas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.retrofit.RetrofitClient
import com.lpfr3d.heythere.databinding.FragmentBuscarSalaBinding


class BuscarSalaFragment : Fragment(R.layout.fragment_buscar_sala) {

    lateinit var binding: FragmentBuscarSalaBinding
    private lateinit var adapterSolicitacoes: BuscarSalaAdapter
    private val viewModel by viewModels<BuscarSalaViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return BuscarSalaViewModel(RetrofitClient.getApiService(context!!)) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuscarSalaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterSolicitacoes = BuscarSalaAdapter(this)
        binding.recyclerBuscarSala.layoutManager = LinearLayoutManager(context)
        binding.recyclerBuscarSala.adapter = adapterSolicitacoes
        adapterSolicitacoes.notifyDataSetChanged()

        binding.ivBuscarSala.setOnClickListener {
            viewModel.buscarSalas(binding.etBuscarSala.text.trim().toString())
                .observe(viewLifecycleOwner, {
                    println(it)
                    adapterSolicitacoes.setListData(it)
                    adapterSolicitacoes.notifyDataSetChanged()
                })
        }
    }

}