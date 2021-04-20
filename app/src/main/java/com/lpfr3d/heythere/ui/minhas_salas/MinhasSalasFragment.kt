package com.lpfr3d.heythere.ui.minhas_salas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lpfr3d.heythere.R
import com.lpfr3d.heythere.databinding.FragmentMinhasSalasBinding


class MinhasSalasFragment : Fragment(R.layout.fragment_minhas_salas) {

    lateinit var binding: FragmentMinhasSalasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMinhasSalasBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}