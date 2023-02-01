package com.example.androidproject.utils

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.example.androidproject.R

fun createFieldTextWatcher(field: EditText, function: () -> Unit) {
    field.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) { return function() }
    })
}

fun fieldResetError(field: EditText) {
    field.error = null
    field.setBackgroundResource(R.drawable.auth_field_bg)
}

fun fieldSetError(res: Resources, field: EditText, error: String) {
    val icon = ResourcesCompat.getDrawable(res, R.drawable.warning, null)

    icon?.setBounds(
        0, 0,
        icon.intrinsicWidth,
        icon.intrinsicHeight
    )

    field.setError(error, icon)

    field.setBackgroundResource(R.drawable.auth_field_bg_error)
}