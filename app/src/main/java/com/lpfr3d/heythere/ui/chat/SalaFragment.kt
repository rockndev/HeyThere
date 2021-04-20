package com.lpfr3d.heythere.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lpfr3d.heythere.MensagemModel
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.databinding.FragmentSalaBinding
import org.phoenixframework.Channel
import org.phoenixframework.Socket


class SalaFragment : Fragment(R.layout.fragment_sala) {

    lateinit var binding: FragmentSalaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val socket =
            Socket("ws://darksalmon-flawless-mosasaur.gigalixirapp.com/socket/websocket")
        val adapterMensagem = MensagemAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        var lobbyChannel: Channel? = null

        val idSala = arguments?.getString("idSala")

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

        val chatroom = socket.channel("salas:${idSala}")

        chatroom.on("join") {
            println("Entrou na sala")
        }

        chatroom.on("mensagem_criada") { message ->
            for ((key, value) in message.payload) {
                println("$key = $value")
            }
            val msg = message.payload["conteudo"].toString()
            val user = message.payload["usuario"].toString()
            val horarioMensagem = message.payload["h_envio"].toString()
            val nacionalidade = message.payload["nacionalidade"].toString()
            addText(
                layoutManager,
                MensagemModel(nacionalidade, user, msg, horarioMensagem),
                adapterMensagem
            )
        }

        lobbyChannel = chatroom
        chatroom
            .join()
            .receive("ok") {
                println("Entrou no canal")
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

    private fun addText(
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

}