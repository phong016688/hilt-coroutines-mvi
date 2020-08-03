package com.example.mvisamplecoroutines.utils

import android.widget.TextView
import javax.inject.Inject

interface Validator {
    fun validateEmail(email: String): String
    fun validatePassword(password: String): String
}

class ValidatorImpl @Inject constructor() : Validator {
    override fun validateEmail(email: String): String {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return when {
            email.isEmpty() -> "This field is required"
            !email.matches(emailPattern.toRegex()) -> "Email invalid"
            else -> ""
        }
    }

    override fun validatePassword(password: String): String {
        return when {
            password.isEmpty() -> "This field is required"
            password.length < 6-> "Password required length > 6"
            else -> ""
        }
    }

}