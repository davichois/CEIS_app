package com.davichois.ceis

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.davichois.ceis.databinding.FragmentHelpZoneBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HelpZoneFragment : Fragment(R.layout.fragment_help_zone) {

    private var _binding: FragmentHelpZoneBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpZoneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.statusBarColor = Color.rgb(237, 241, 253)

        // Obtencion de fecha de evento y guardado
        // Array con fechas específicas (año, mes, día) CEIS
        val datesArray = arrayOf(
            Calendar.getInstance().apply { set(2024, 10, 4) },
            Calendar.getInstance().apply { set(2024, 10, 13) },
            Calendar.getInstance().apply { set(2024, 10, 15) },
            Calendar.getInstance().apply { set(2024, 10, 16) }
        )
        val currentTime = Calendar.getInstance()
        // Formateador de tiempo
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        for (date in datesArray) {
            if (currentTime.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                currentTime.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                currentTime.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
            ) {
                val formattedDate = dateFormat.format(date.time)
                Toast.makeText(requireActivity(), "Bienvenido ${formattedDate.split("/")[0]}!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
