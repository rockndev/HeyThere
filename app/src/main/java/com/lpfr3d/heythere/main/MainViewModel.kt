package com.lpfr3d.heythere.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lpfr3d.heythere.MensagemModel
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade
import com.lpfr3d.heythere.database.db_room.repository.mensagem.MensagemRepositorio
import com.lpfr3d.heythere.ui.chat.MensagemAdapter
import com.lpfr3d.heythere.utils.MinhasSalas
import com.lpfr3d.heythere.utils.RespostaPayloadEventoJoin
import com.lpfr3d.heythere.utils.horaAtualEmUTC
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.phoenixframework.Channel
import org.phoenixframework.Socket

class MainViewModel(private val mensagemRepositorio: MensagemRepositorio) : ViewModel() {

    var mapaDeCanais = mutableMapOf<String, Channel>()
    val fragmentoAtualSala = MutableLiveData<Boolean>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MensagemAdapter
    var recyclerView = MutableLiveData<RecyclerView>()
    var idSalaQueOUsuarioEsta = MutableLiveData<String>()
    var horarioUltimoLogout: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun conectarSocket() {
        val socket =
            Socket("ws://darksalmon-flawless-mosasaur.gigalixirapp.com/socket/websocket")
        observarSockets(socket)
        socket.connect()
        joinarCanais(socket)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun joinarCanais(socket: Socket) {
        val hashmap = HashMap<String, Any?>()
        println(horarioUltimoLogout)

        if (horarioUltimoLogout == null)
            hashmap["h_logout"] = null
        else
            hashmap["h_logout"] = horarioUltimoLogout

        MinhasSalas.minhasSalas.forEach { minhaSala ->
            val chatroom = socket.channel("salas:${minhaSala.id.toInt()}", hashmap)
            mapaDeCanais[minhaSala.id] = chatroom

            /**
             * Ao entrar na sala
             */
            chatroom
                .join()
                .receive("ok") { message ->
                    val listaDeNovasMensagens = mutableListOf<MensagemEntidade>()
                    val json = JSONObject(message.payload).toString()
                    val gson = Gson()
                    val jsonFormatado = gson.fromJson(json, RespostaPayloadEventoJoin::class.java)
                    jsonFormatado.response.mensagens.forEach {
                        listaDeNovasMensagens.add(
                            MensagemEntidade(
                                conteudo = it.conteudo,
                                remetente = it.usuario.nome,
                                horario = it.hEnvio,
                                sala = minhaSala.id.toInt(),
                                id = 0
                            )
                        )

                    }

                    salvarListaDeMensagens(listaDeNovasMensagens)
                }
                .receive("error") {
                    println("Erro ao tentar entrar no canal ${it.payload}")
                }

            chatroom.on("mensagem_criada") { message ->
                val msg = message.payload["conteudo"].toString()
                val user = message.payload["usuario"].toString()
                val horarioMensagem = message.payload["h_envio"].toString()
                val nacionalidade = message.payload["nacionalidade"].toString()

                salvarMensagemRecebida(
                    MensagemEntidade(
                        id = 0,
                        conteudo = msg,
                        remetente = user,
                        sala = minhaSala.id.toInt(),
                        horario = horaAtualEmUTC()
                    )
                )

                if (fragmentoAtualSala.value == true && minhaSala.id == idSalaQueOUsuarioEsta.value) {
                    val mensagem = MensagemModel(
                        corpoMensagem = msg,
                        horario = "10:00",
                        nacionalidade = "BRA",
                        nomeUsuario = user
                    )
                    adicionarMensagemNaTelaDoUsuario(
                        layoutManager, mensagem, adapter,
                        recyclerView.value!!
                    )
                }
            }

        }

    }

    private fun adicionarMensagemNaTelaDoUsuario(
        layoutManager: LinearLayoutManager,
        mensagem: MensagemModel,
        adapter: MensagemAdapter,
        recyclerView: RecyclerView
    ) {
        viewModelScope.launch {
            adapter.add(mensagem)
            layoutManager.smoothScrollToPosition(
                recyclerView,
                null,
                adapter.itemCount
            )
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

    /**
     * Salva a mensagem que é recebida por outros usuários durante o chat
     */
    private fun salvarMensagemRecebida(mensagem: MensagemEntidade) {
        viewModelScope.launch {
            println("Mensagem salva: \n$mensagem")
            mensagemRepositorio.insert(mensagem)
        }
    }

    /**
     * Salva lista de mensagens recebidas ao dar o join
     */
    private fun salvarListaDeMensagens(mensagens: MutableList<MensagemEntidade>) {
        viewModelScope.launch {
            mensagemRepositorio.inserirListaDeMensagens(mensagens)
        }
    }

}