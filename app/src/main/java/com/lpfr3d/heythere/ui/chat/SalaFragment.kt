package com.lpfr3d.heythere.ui.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lpfr3d.heythere.MensagemModel
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.db_room.AppDataBase
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import com.lpfr3d.heythere.databinding.FragmentSalaBinding
import com.lpfr3d.heythere.main.MainViewModel

class SalaFragment : Fragment(R.layout.fragment_sala) {

    private lateinit var viewModel: SalaViewModel
    val shar: MainViewModel by activityViewModels()
    lateinit var binding: FragmentSalaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalaBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDataBase.getDatabse(requireContext()).mensagemDao()
        val repositorio = MensagemRepositorio(dao)
        val factory = SalaVMFactory(repositorio)
        viewModel = ViewModelProvider(this, factory).get(SalaViewModel::class.java)


        val adapterMensagem = MensagemAdapter()
        shar.adapter = adapterMensagem
        val layoutManager = LinearLayoutManager(requireContext())
        shar.layoutManager = layoutManager
        val idSala = arguments?.getString("idSala")
        shar.idSalaQueOUsuarioEsta.value = idSala
        /**
         * Carregar mensagens do banco
         */
        carregarMensagensDoBancoParaARecycler(idSala!!.toInt(), layoutManager, adapterMensagem)

        layoutManager.stackFromEnd = true
        shar.recyclerView.value = binding.recyclerMensagens

        binding.run {
            recyclerMensagens.layoutManager = layoutManager
            recyclerMensagens.adapter = adapterMensagem
        }

        binding.btEnviarMensagem.setOnClickListener {
            val msg = binding.etCorpoMensagem.text.toString().trim()
            val hashmap = HashMap<String, Any>()
            hashmap["conteudo"] = msg

            shar.mapaDeCanais[idSala]!!.push("nova_mensagem",hashmap)
                .receive("ok") { Log.d("TAG", "success $it") }
                .receive("erro ao enviar mensag em") { Log.d("TAG", "error $it") }

            binding.etCorpoMensagem.text.clear()
        }

    }

    private fun carregarMensagensDoBancoParaARecycler(
        idSala: Int,
        layoutManager: LinearLayoutManager,
        adapter: MensagemAdapter
    ) {
        viewModel.coletarListaDeMensagensArmazenadasNoBancoLocal(idSala)
        viewModel.listaDeMensagensArmazenadasNoBancoLocal.observe(viewLifecycleOwner, { mensagens ->

            mensagens.forEach {
                adicionarMensagemNaTela(
                    layoutManager,
                    MensagemModel("BRA", it.remetente, it.conteudo, it.horario),
                    adapter
                )
            }
        })
    }

    private fun adicionarMensagemNaTela(
        layoutManager: LinearLayoutManager,
        text: MensagemModel,
        adapter: MensagemAdapter
    ) {
        activity?.runOnUiThread {
            adapter.add(text)
            layoutManager.smoothScrollToPosition(
                binding.recyclerMensagens,
                null,
                adapter.itemCount
            )
        }
    }

}