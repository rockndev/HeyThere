package com.lpfr3d.heythere.ui.buscar_salas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.models.SalaModel

class BuscarSalaAdapter(var fragment: Fragment) :
    RecyclerView.Adapter<BuscarSalaAdapter.ViewHolder>() {

    private var listaDeSalasEncontradas = listOf<SalaModel>()

    fun setListData(data: List<SalaModel>) {
        listaDeSalasEncontradas = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.buscar_sala_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listaDeSalasEncontradas[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(fragment.context, "${holder.tvNomeSala}", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return if (listaDeSalasEncontradas.isNotEmpty()) {
            listaDeSalasEncontradas.size
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