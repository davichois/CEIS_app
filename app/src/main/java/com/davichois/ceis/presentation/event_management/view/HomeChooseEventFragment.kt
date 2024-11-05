package com.davichois.ceis.presentation.event_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.davichois.ceis.R
import com.davichois.ceis.databinding.FragmentHomeChooseEventBinding
import com.davichois.ceis.presentation.event_management.adapter.choose_event.ChooseEventAdapter
import com.davichois.ceis.presentation.event_management.dto_pru.Evento
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeChooseEventFragment : Fragment(R.layout.fragment_home_choose_event) {

    private var _binding: FragmentHomeChooseEventBinding? = null
    private val binding get() = _binding

    private var eventosList: List<Evento> = listOf(
        Evento(id = 1, tipo = "SESTONES_PARALELAS", nombre = "Apertura del Evento A"),
        Evento(id = 2, tipo = "SESTONES_PARALELAS", nombre = "Apertura del Evento B"),
        Evento(id = 3, tipo = "TALLERES", nombre = "Congreso A"),
        Evento(id = 4, tipo = "TALLERES", nombre = "Congreso B"),
        Evento(id = 5, tipo = "COLOQUIO", nombre = "Congreso C"),
        Evento(id = 6, tipo = "COLOQUIO", nombre = "Congreso D"),
        Evento(id = 7, tipo = "COLOQUIO", nombre = "Congreso E"),
        Evento(id = 8, tipo = "COLOQUIO", nombre = "Inicio del Proyecto A")
    )

    private val condiciones = mapOf(
        "SESTONES_PARALELAS" to 2,
        "TALLERES" to 2,
        "COLOQUIO" to 2
    )

    private val eventsChose = mutableListOf<Evento>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeChooseEventBinding.inflate(
            inflater,
            container,
            false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.statusBarColor = Color.rgb(237,241,253)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context)
        binding?.rvListEventChoose?.layoutManager = manager

        binding?.rvListEventChoose?.adapter = ChooseEventAdapter(eventosList) { evento, isChecked ->
            manejarSeleccion(evento, isChecked)
        }
    }

    private fun manejarSeleccion(evento: Evento, isChecked: Boolean) {
        if (isChecked) {
            if (puedeSeleccionar(evento)) {
                evento.isSelected = true
                eventsChose.add(evento)
            } else {
                evento.isSelected = false // Revertir selección si se excede el límite
            }
        } else {
            eventsChose.remove(evento)
            evento.isSelected = false
        }
        mostrarEventosSeleccionados()
    }

    private fun puedeSeleccionar(evento: Evento): Boolean {
        val conteoActual = eventsChose.count { it.tipo == evento.tipo }
        val limite = condiciones[evento.tipo] ?: return false
        return conteoActual < limite
    }

    private fun mostrarEventosSeleccionados() {
        Toast.makeText(requireActivity(), "Eventos seleccionados: ${eventsChose.size}", Toast.LENGTH_LONG).show()
    }

}