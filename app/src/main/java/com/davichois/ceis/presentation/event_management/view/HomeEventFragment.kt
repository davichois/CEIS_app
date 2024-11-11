package com.davichois.ceis.presentation.event_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentHomeEventBinding
import com.davichois.ceis.databinding.ViewBottomOptionAppBinding
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.presentation.event_management.adapter.choose_event.ChooseEventAdapter
import com.davichois.ceis.presentation.event_management.adapter.events.EventsAdapter
import com.davichois.ceis.presentation.event_management.view_model.EventManagementViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeEventFragment : Fragment(R.layout.fragment_home_event) {

    private var _binding: FragmentHomeEventBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val eventManagementViewModel: EventManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeEventBinding.inflate(
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

        binding?.generateTicket?.setOnClickListener {
            val action = HomeEventFragmentDirections.actionHomeEventFragmentToTicketEventGeneralFragment()
            findNavController().navigate(action)
        }
        binding?.llHeaderHome?.setOnClickListener {
            showDialogOptionApp()
        }

        eventManagementViewModel.getEventForChooseAndGeneralDay("13")

        lifecycleScope.launch {
            eventManagementViewModel.uiStateListEventChooseAndGeneralDay.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentHE?.visibility = View.GONE
                        binding?.loadShimmerHE?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        binding?.contentHE?.visibility = View.VISIBLE
                        binding?.loadShimmerHE?.visibility = View.GONE
                        // Toast.makeText(requireActivity(), "correct ${state.data.size}", Toast.LENGTH_SHORT).show()
                        initRecyclerView(eventList = state.data)
                    }
                    is Resource.Error -> {
                        binding?.contentHE?.visibility = View.VISIBLE
                        binding?.loadShimmerHE?.visibility = View.GONE
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
        binding?.rvListEvent?.layoutManager = manager

        binding?.rvListEvent?.adapter = EventsAdapter(eventList) { onSelectedEvent(it) }
    }

    private fun onSelectedEvent(eventId: String){
        val action = HomeEventFragmentDirections.actionHomeEventFragmentToDetailsChosenEventFragment(eventCode = eventId)
        findNavController().navigate(action)
        Toast.makeText(requireActivity(), eventId, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogOptionApp() {
        val optionBottomDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.view_bottom_option_app, null)

        val bindingDialog = ViewBottomOptionAppBinding.bind(view)

        bindingDialog.btnCloseSession.setOnClickListener {
            optionBottomDialog.dismiss()
        }

        optionBottomDialog.setCancelable(true)
        optionBottomDialog.setContentView(view)

        optionBottomDialog.show()
    }

}