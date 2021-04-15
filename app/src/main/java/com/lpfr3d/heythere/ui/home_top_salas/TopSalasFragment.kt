package com.lpfr3d.heythere.ui.home_top_salas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.Status
import com.lpfr3d.heythere.database.models.SalaModel
import com.lpfr3d.heythere.database.retrofit.RetrofitClient
import com.lpfr3d.heythere.databinding.FragmentTopSalasBinding
import kotlin.math.abs


class TopSalasFragment : Fragment(R.layout.fragment_top_salas) {
    private val navController: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentTopSalasBinding
    private lateinit var adapterTopSalas: TopSalasAdapter

    private val viewModel by viewModels<TopSalasViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopSalasViewModel(RetrofitClient.instance) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopSalasBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listaTopSalas: ArrayList<SalaModel>

        viewModel.listarSalas().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCESSO -> {
                        listaTopSalas = resource.data as ArrayList<SalaModel>
                        popularAdapter(listaTopSalas)
                    }
                    Status.ERRO -> println(resource.message)
                    Status.CARREGANDO -> println("loading")
                }
            }
        })

    }

    private fun popularAdapter(lista: ArrayList<SalaModel>) {
        val listaTop5Salas = lista.take(5)
        adapterTopSalas = TopSalasAdapter(this)
        adapterTopSalas.setListData(listaTop5Salas)
        binding.viewPagerLocais.run {
            adapter = adapterTopSalas
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.run {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r: Float = 1 - abs(position)
                page.scaleY = (0.95f + r * 0.05f)
            }
        }
        binding.viewPagerLocais.setPageTransformer(compositePageTransformer)
    }

}