package com.davichois.ceis.presentation.ticket_management.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentTicketEventGeneralBinding
import com.davichois.ceis.presentation.ticket_management.view_model.TicketManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class TicketEventGeneralFragment : Fragment(R.layout.fragment_ticket_event_general) {

    private var _binding: FragmentTicketEventGeneralBinding? = null
    private val binding get() = _binding

    private lateinit var ticketLayout: ConstraintLayout

    // Injection view model
    private val ticketManagementViewModel: TicketManagementViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTicketEventGeneralBinding.inflate(
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

        ticketManagementViewModel.getUserForDNI()
        lifecycleScope.launch {
            ticketManagementViewModel.uiStateUser.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentTEG?.visibility = View.GONE
                        binding?.loadShimmerTEG?.visibility = View.VISIBLE
                        //Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Success -> {
                        binding?.contentTEG?.visibility = View.VISIBLE
                        binding?.loadShimmerTEG?.visibility = View.GONE
                        binding?.namePerson?.text = state.data.name
                        binding?.nickName?.text = state.data.nickname
                        "#${state.data.code}".also { binding?.codeReserved?.text = it }

                        val avatar = when (state.data.gender) {
                            "M" -> "betoo"
                            "F" -> "andrea"
                            else -> "andrea"
                        }
                        requireContext().resources?.let {
                            binding?.riveAnimationAvatar?.setRiveResource(
                                it.getIdentifier(avatar, "raw", requireContext().packageName)
                            )
                        }

                        ticketLayout = binding?.clBadgeTicket!!
                    }
                    is Resource.Error -> {
                        binding?.contentTEG?.visibility = View.VISIBLE
                        binding?.loadShimmerTEG?.visibility = View.GONE
                        // Toast.makeText(requireActivity(), "error", Toast.LENGTH_LONG).show()
                    }

                    Resource.PreLoad -> {
                        println("error ticket")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding?.shareTicket?.setOnClickListener {
            val bitmap = createBitmapFromView(ticketLayout)
            val imageUri = saveBitmapToFile(bitmap, requireActivity())
            if (imageUri != null) {
                shareImage(requireActivity(), imageUri)
            }
        }
        binding?.backView?.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context): Uri? {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ticketCEISconf.png")
        return try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun shareImage(context: Context, imageUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir boletita de pago"))
    }

}