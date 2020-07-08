package com.example.mvisamplecoroutines.ui.fragment.login

import com.example.mvisamplecoroutines.core.IMviAction
import com.example.mvisamplecoroutines.core.IMviIntent
import com.example.mvisamplecoroutines.core.IMviResult
import com.example.mvisamplecoroutines.core.IMviState

sealed class LoginState : IMviState {
    data class LoginFailure(val err: String) : LoginState()
    object LoginSuccess : LoginState()
    object Initial : LoginState()
}

sealed class LoginIntent : IMviIntent {
    data class LoginWithEmailPassword(val email: String, val password: String) : LoginIntent()
    object LoginWithFacebook : LoginIntent()
    object LoginWithGoogle : LoginIntent()
    object InitialIntent : LoginIntent()
}

sealed class LoginAction : IMviAction {
    object InitialUiAction : LoginAction()
    data class ClickLoginEmailPassword(val email: String, val password: String) : LoginAction()
    object ClickLoginGoogle : LoginAction()
    object NoAction : LoginAction()
}

sealed class LoginResult : IMviResult {
    object InitialResult : LoginResult()
    object NoResult : LoginResult()
    data class LoginSuccess(val data: String) : LoginResult()
    data class LoginFailure(val errorMessage: String) : LoginResult()
}

