package com.example.mvisamplecoroutines.ui.fragment.login

import com.example.mvisamplecoroutines.core.IMviAction
import com.example.mvisamplecoroutines.core.IMviIntent
import com.example.mvisamplecoroutines.core.IMviResult
import com.example.mvisamplecoroutines.core.IMviState
import javax.xml.validation.Validator

sealed class LoginIntent : IMviIntent {
    data class SubmitLogin(val email: String, val password: String) : LoginIntent()
    object InitialIntent : LoginIntent()
}

sealed class LoginAction : IMviAction {
    data class LoginWithEmailPassword(val email: String, val password: String) : LoginAction()
    object Initial : LoginAction()
}

sealed class LoginResult : IMviResult {
    data class LoginSuccess(val userId: String) : LoginResult()
    data class LoginFailure(val error: String) : LoginResult()
    data class LoginInValidate(val validate: ValidatorError) : LoginResult()
    object Initial : LoginResult()
    object Loading : LoginResult()
}

data class LoginState(
    val validate: ValidatorError? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean? = null,
    val userId: String? = null
) : IMviState

class ValidatorError private constructor(
    val emailValidMessage: String = "",
    val passwordValidMessage: String = ""
) {
    companion object {
        fun checkValid(email: String, password: String) = ValidatorError(
            emailValidMessage = if (email.isEmpty()) "email is not empty" else "",
            passwordValidMessage = if (password.isEmpty()) "password is not empty" else ""
        )
    }
}
