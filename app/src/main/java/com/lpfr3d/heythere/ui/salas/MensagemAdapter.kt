package com.lpfr3d.heythere.ui.salas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lpfr3d.heythere.R

class MensagemAdapter : RecyclerView.Adapter<MensagemAdapter.ViewHolder>() {

    private var messages : MutableList<String> = mutableListOf()

    fun add(message :String){
        messages.add(message)
        notifyItemInserted(messages.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mensagem_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int  = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.label.text = messages[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val label : TextView = itemView.findViewById(R.id.item_message_label)
    }

}
