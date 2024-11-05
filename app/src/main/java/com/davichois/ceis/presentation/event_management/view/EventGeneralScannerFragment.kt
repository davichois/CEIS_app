package com.davichois.ceis.presentation.event_management.view

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.davichois.ceis.R
import com.davichois.ceis.databinding.FragmentEventGeneralScannerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventGeneralScannerFragment : Fragment(R.layout.fragment_event_general_scanner) {

    private var _binding: FragmentEventGeneralScannerBinding? = null
    private val binding get() = _binding

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventGeneralScannerBinding.inflate(
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

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.CAMERA
            ) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                123
            )
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        val scannerView = binding?.scannerView
        val activity = requireActivity()

        codeScanner = scannerView?.let { CodeScanner(activity, it) }!!
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                if (it.text.startsWith("KSG")) {
                    Toast.makeText(activity, "HOLI", Toast.LENGTH_SHORT).show()
                } else {
                    activity.runOnUiThread {
                        Toast.makeText(activity, "Intenta de nuevo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

}