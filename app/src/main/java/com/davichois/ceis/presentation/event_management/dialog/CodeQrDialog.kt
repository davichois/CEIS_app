package com.davichois.ceis.presentation.event_management.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.davichois.ceis.databinding.DialogCodeQrBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class CodeQrDialog(
    private val codeQr: String
): DialogFragment() {

    private lateinit var binding: DialogCodeQrBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCodeQrBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                codeQr,
                BarcodeFormat.QR_CODE,
                500,
                500
            )

            binding.ivQrCodeImage.setImageBitmap(bitmap)

        }catch (e: Exception){
            e.printStackTrace()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}
