package com.lpfr3d.heythere.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lpfr3d.heythere.MensagemModel
import com.lpfr3d.heythere.R

class MensagemAdapter : RecyclerView.Adapter<MensagemAdapter.ViewHolder>() {

    private var messages: MutableList<MensagemModel> = mutableListOf()

    fun add(message: MensagemModel) {
        messages.add(message)
        notifyItemInserted(messages.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.mensagem_item2, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNomeUsuario.text = "${messages[position].nomeUsuario}: "
        holder.tvCorpoMensagem.text = messages[position].corpoMensagem
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomeUsuario: TextView = itemView.findViewById(R.id.tvNomeUsuario)
        val tvCorpoMensagem: TextView = itemView.findViewById(R.id.tvCorpoMensagem)
    }

}
