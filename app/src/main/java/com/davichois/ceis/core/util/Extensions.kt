package com.davichois.ceis.core.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.auth0.android.jwt.JWT

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Fragment.hideKeyboard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.hideKeyboard()
    }
}

fun EditText.onTextChange(listener: (String)-> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            true
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            true
        }

    })
}

fun String.decodeJWT(property: String): String? {
    return try {
        val jwt = JWT(this)
        jwt.getClaim(property).asString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)