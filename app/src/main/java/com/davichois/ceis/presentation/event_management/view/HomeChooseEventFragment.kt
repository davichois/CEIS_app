package com.davichois.ceis.presentation.event_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentHomeChooseEventBinding
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.presentation.event_management.adapter.choose_event.ChooseEventAdapter
import com.davichois.ceis.presentation.event_management.view_model.EventManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HomeChooseEventFragment : Fragment(R.layout.fragment_home_choose_event) {

    private var _binding: FragmentHomeChooseEventBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val eventManagementViewModel: EventManagementViewModel by viewModels()

    private val eventsChose = mutableListOf<EventModel>()

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

        val currentTime = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime.time)
        val dayCurrent = formattedDate.split("/")[0].trim()

        // TODO: days change
        eventManagementViewModel.getEventForChooseForDay(dayCurrent)

        binding?.rvListEventChoose?.itemAnimator = null

        when(dayCurrent){
            "12" -> {
                binding?.infoDate?.text = "DIA <01/>"
            }
            "13" -> {
                binding?.infoDate?.text = "DIA <02/>"
            }
            "14" -> {
                binding?.infoDate?.text = "DIA <03/>"
            }
            "15" -> {
                binding?.infoDate?.text = "DIA <04/>"
            }
            else -> binding?.infoDate?.text = "NOT DAY"
        }

        binding?.btnBooking?.setOnClickListener {
            eventManagementViewModel.postAllBookings(events = eventsChose)
            /*
            if (eventsChose.isNotEmpty()) {
                eventManagementViewModel.postAllBookings(
                    codes = eventsChose.map { it.code },
                    onComplete = {
                        findNavController().navigate(
                            HomeChooseEventFragmentDirections.actionHomeChooseEventFragmentToHomeEventFragment()
                        )
                    }
                )
            } else {
                Toast.makeText(context, "Por favor, selecciona al menos un evento.", Toast.LENGTH_SHORT).show()
            }
             */
        }

        lifecycleScope.launch {
            eventManagementViewModel.uiStateEventListReturn.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentHCE?.visibility = View.GONE
                        binding?.loadShimmerHCE?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        findNavController().navigate(
                            HomeChooseEventFragmentDirections.actionHomeChooseEventFragmentToHomeEventFragment()
                        )
                    }
                    is Resource.Error -> {
                        binding?.contentHCE?.visibility = View.VISIBLE
                        binding?.loadShimmerHCE?.visibility = View.GONE
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                        println(state.message)
                    }

                    Resource.PreLoad -> {
                        println("start in app")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            eventManagementViewModel.uiStateListEventChooseDay.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentHCE?.visibility = View.GONE
                        binding?.loadShimmerHCE?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        binding?.contentHCE?.visibility = View.VISIBLE
                        binding?.loadShimmerHCE?.visibility = View.GONE
                        // Toast.makeText(requireActivity(), "correct ${state.data.size}", Toast.LENGTH_SHORT).show()

                        val filteredEventList = state.data.filter { it.quota > 0 }
                        // initRecyclerView(eventList = filteredEventList)
                        initRecyclerView(eventList = filteredEventList)
                    }
                    is Resource.Error -> {
                        binding?.contentHCE?.visibility = View.VISIBLE
                        binding?.loadShimmerHCE?.visibility = View.GONE
                        // Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    Resource.PreLoad -> {
                        println("start in app")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun initRecyclerView(eventList: List<EventModel>) {
        val manager = LinearLayoutManager(context)
        binding?.rvListEventChoose?.layoutManager = manager

        binding?.rvListEventChoose?.adapter = ChooseEventAdapter(eventList) { evento, isChecked ->
            manageSelection(evento, isChecked)
        }

    }

    private fun manageSelection(evento: EventModel, isChecked: Boolean) {
        if (isChecked){
            eventsChose.add(evento)
            evento.isSelected = true
        } else {
            eventsChose.remove(evento)
            evento.isSelected = false
        }
        viewEventsSelected()
    }

    private fun viewEventsSelected() {
        Toast.makeText(requireActivity(), "Eventos seleccionados: ${eventsChose.size}", Toast.LENGTH_LONG).show()
    }

}