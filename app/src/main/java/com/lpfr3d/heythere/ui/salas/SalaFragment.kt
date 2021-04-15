package com.lpfr3d.heythere.ui.salas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
            sendMessage(lobbyChannel!!)
        }

        observarSockets(socket, layoutManager, adapterMensagem)

        socket.connect()
        val chatroom = socket.channel("salas:${idSala}")

        chatroom.on("join") {
            this.addText(layoutManager, "You joined the room", adapterMensagem)
        }

        chatroom.on("mensagem_criada") { message ->
            for ((key, value) in message.payload) {
                println("$key = $value")
            }
            val msg = message.payload["conteudo"].toString()
            val user = message.payload["usuario"].toString()
            val horario_mensagem = message.payload["h_envio"].toString()
            val nacionalidade = message.payload["nacionalidade"].toString()
            addText(layoutManager, "$user: $msg", adapterMensagem)
        }

        lobbyChannel = chatroom
        chatroom
            .join()
            .receive("ok") {
                this.addText(layoutManager, "Joined Channel", adapterMensagem)
            }
            .receive("error") {
                this.addText(
                    layoutManager,
                    "Failed to join channel: ${it.payload}",
                    adapterMensagem
                )
            }


    }

    private fun observarSockets(
        socket: Socket,
        layoutManager: LinearLayoutManager,
        adapter: MensagemAdapter
    ) {
        socket.onOpen {
            println("SOCKET ABERTO")
            this.addText(layoutManager, "Socket Opened", adapter)
        }

        socket.onClose {
            println("SOCKET FECHADO")
            this.addText(layoutManager, "Socket Opened", adapter)
        }

        socket.onError { throwable, response ->
            println("Socket Errored $response ${throwable.message}")
            this.addText(layoutManager, "Socket Opened", adapter)
        }
    }

    private fun addText(
        layoutManager: LinearLayoutManager,
        text: String,
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

    private fun sendMessage(lobbyChannel: Channel) {
        val hashmap = HashMap<String, Any>()
        hashmap["conteudo"] = binding.etCorpoMensagem.text.toString()

        lobbyChannel.push("nova_mensagem", hashmap)
            .receive("ok") { Log.d("TAG", "success $it") }
            .receive("erro ao enviar mensagem") { Log.d("TAG", "error $it") }

        binding.etCorpoMensagem.text.clear()
    }

}