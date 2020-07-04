package com.example.mvisamplecoroutines.ui.fragment.login

sealed class LoginState {
    data class LoginFailure(
        val userNameErr: String,
        val passwordErr: String
    ) : LoginState()

    object LoginSuccess : LoginState()
}