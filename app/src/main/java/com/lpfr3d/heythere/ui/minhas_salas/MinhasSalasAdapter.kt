package com.lpfr3d.heythere.ui.minhas_salas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.models.SalaModel

class MinhasSalasAdapter(var fragment: Fragment) :
    RecyclerView.Adapter<MinhasSalasAdapter.ViewHolder>() {

    private var listasDeMinhasSalas = listOf<SalaModel>()

    fun setListData(data: List<SalaModel>) {
        listasDeMinhasSalas = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.minhas_salas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listasDeMinhasSalas[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(fragment.context, "Sala clickada", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return if (listasDeMinhasSalas.isNotEmpty()) {
            listasDeMinhasSalas.size
        } else {
            0
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNomeSala = itemView.findViewById<TextView>(R.id.tvNomeSala)
        var tvQuantidadeDePessoas = itemView.findViewById<TextView>(R.id.tvQuantidadeDePessoas)

        fun bindView(topSalasModel: SalaModel) {
            tvNomeSala.text = topSalasModel.nomeSala
            tvQuantidadeDePessoas.text = "Usuários: " + topSalasModel.quantidadeDePessoas.toString()
        }
    }
}