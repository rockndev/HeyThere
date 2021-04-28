package com.lpfr3d.heythere.ui.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.lpfr3d.heythere.MensagemModel
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.db_room.AppDataBase
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import com.lpfr3d.heythere.databinding.FragmentSalaBinding
import com.lpfr3d.heythere.json.RespostaPayloadEventoJoin
import org.json.JSONObject
import org.phoenixframework.Channel
import org.phoenixframework.Socket
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class SalaFragment : Fragment(R.layout.fragment_sala) {
    private lateinit var viewModel: SalaViewModel

    lateinit var binding: FragmentSalaBinding
    lateinit var listaDeMensagens: MutableList<MensagemEntidade>

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

        val socket =
            Socket("ws://darksalmon-flawless-mosasaur.gigalixirapp.com/socket/websocket")
        val adapterMensagem = MensagemAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        var lobbyChannel: Channel? = null

        val idSala = arguments?.getString("idSala")
        viewModel.coletarFotoDoDia(idSala!!.toInt())
        viewModel.fotoDoDia.observe(viewLifecycleOwner, { mensagens ->
            listaDeMensagens = mensagens
        })

        layoutManager.stackFromEnd = true

        binding.run {
            recyclerMensagens.layoutManager = layoutManager
            recyclerMensagens.adapter = adapterMensagem
        }

        binding.btEnviarMensagem.setOnClickListener {
            enviarMensagemNoChat(lobbyChannel!!)
        }

        observarSockets(socket)
        socket.connect()

        val hashmap = HashMap<String, Any>()
        hashmap["h_logout"] = "2021-04-01T00:00:46"
        val chatroom = socket.channel("salas:${idSala}", hashmap)
        println(idSala)
        chatroom.on("mensagem_criada") { message ->
            for ((key, value) in message.payload) {
                println("$key = $value")
            }
            val msg = message.payload["conteudo"].toString()
            val user = message.payload["usuario"].toString()
            val horarioMensagem = message.payload["h_envio"].toString()
            val nacionalidade = message.payload["nacionalidade"].toString()
            viewModel.salvarMensagem(
                MensagemEntidade(
                    id = 0,
                    conteudo = msg,
                    remetente = user,
                    sala = 2,
                    horario = horaAtual()
                )
            )
            adicionarMensagemNaTela(
                layoutManager,
                MensagemModel(nacionalidade, user, msg, horarioMensagem),
                adapterMensagem
            )
        }

        lobbyChannel = chatroom


        chatroom
            .join()
            .receive("ok") { message ->
                val listaDeMensagens = mutableListOf<MensagemEntidade>()
                val json = JSONObject(message.payload).toString()
                val gson = Gson()
                val jsonFormatado = gson.fromJson(json, RespostaPayloadEventoJoin::class.java)
                jsonFormatado.response.mensagens.forEach {

                    listaDeMensagens.add(
                        MensagemEntidade(
                            conteudo = it.conteudo,
                            remetente = it.usuario.nome,
                            horario = it.hEnvio,
                            sala = idSala.toInt(),
                            id = 0
                        )
                    )

                    adicionarMensagemNaTela(
                        layoutManager,
                        MensagemModel("BRA", it.usuario.nome, it.conteudo, it.hEnvio),
                        adapterMensagem
                    )
                }

                viewModel.salvarListaDeMensagens(listaDeMensagens)

            }
            .receive("error") {
                println("Erro ao tentar entrar no canal ${it.payload}")
            }

    }

    private fun observarSockets(
        socket: Socket
    ) {
        socket.onOpen {
            println("Socket aberto")
        }

        socket.onClose {
            println("Socket fechado")
        }

        socket.onError { throwable, response ->
            println("Socket erro ${throwable.message} $response")
        }
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

    private fun enviarMensagemNoChat(lobbyChannel: Channel) {
        val hashmap = HashMap<String, Any>()
        hashmap["conteudo"] = binding.etCorpoMensagem.text.toString()

        lobbyChannel.push("nova_mensagem", hashmap)
            .receive("ok") { Log.d("TAG", "success $it") }
            .receive("erro ao enviar mensagem") { Log.d("TAG", "error $it") }

        binding.etCorpoMensagem.text.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun horaAtual(): String {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())
    }

}