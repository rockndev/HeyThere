package com.lpfr3d.heythere.ui.home_top_salas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.flaviofaria.kenburnsview.KenBurnsView
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.database.models.SalaModel
import com.lpfr3d.heythere.database.models.TopSalasModel
import com.squareup.picasso.Picasso

class TopSalasAdapter(var fragment: Fragment) : RecyclerView.Adapter<TopSalasAdapter.ViewHolder>() {

    private var listaDeTopSalas = listOf<SalaModel>()

    fun setListData(data: List<SalaModel>) {
        listaDeTopSalas = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.top_salas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listaDeTopSalas[position])
        holder.itemView.setOnClickListener {
            val navController = fragment.findNavController()
            val bundle = bundleOf("idSala" to holder.idSala)
            navController.navigate(R.id.navigation_sala,bundle)
        }
    }

    override fun getItemCount(): Int {
        return if (listaDeTopSalas.isNotEmpty()) {
            listaDeTopSalas.size
        } else {
            0
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kbvLocation = itemView.findViewById<KenBurnsView>(R.id.kbvTopSalas)
        var textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        var textLocation = itemView.findViewById<TextView>(R.id.textLocation)
        var idSala =""

        fun bindView(topSalasModel: SalaModel) {
            Picasso.get().load(topSalasModel.urlImagem).into(kbvLocation)
            textTitle.text = topSalasModel.quantidadeDePessoas.toString()
            textLocation.text = topSalasModel.nomeSala
            idSala = topSalasModel.id
        }
    }
}