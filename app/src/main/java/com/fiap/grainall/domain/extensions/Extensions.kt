package com.fiap.grainall.domain.extensions

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Fragment.hideKeyboard() {
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}
fun Fragment.onBackPress() {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        requireActivity().finish()
    }
}

fun View.snackbar(
    mensagem: String,
    duracao: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(
    this,
    mensagem,
    duracao
).show()



