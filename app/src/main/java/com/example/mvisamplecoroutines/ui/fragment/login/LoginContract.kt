package com.example.mvisamplecoroutines.ui.fragment.login

import com.example.mvisamplecoroutines.core.IMviAction
import com.example.mvisamplecoroutines.core.IMviIntent
import com.example.mvisamplecoroutines.core.IMviResult
import com.example.mvisamplecoroutines.core.IMviState
import com.example.mvisamplecoroutines.domain.entity.User
import javax.xml.validation.Validator

sealed class LoginIntent : IMviIntent {
    data class SubmitLogin(val email: String, val password: String) : LoginIntent()
    data class EnterEmailField(val email: String) : LoginIntent()
    data class EnterPasswordField(val password: String) : LoginIntent()
    object Initial : LoginIntent()
}

sealed class LoginAction : IMviAction {
    object Initial : LoginAction()
    data class LoginWithEmailPassword(val email: String, val password: String) : LoginAction()
    data class ValidateEmail(val email: String) : LoginAction()
    data class ValidatePassword(val password: String) : LoginAction()
}

sealed class LoginResult : IMviResult {
    data class LoginSuccess(val user: User) : LoginResult()
    data class LoginFailure(val error: String) : LoginResult()
    object EmailValid : LoginResult()
    object PasswordValid : LoginResult()
    data class EmailInValid(val errorMessage: String) : LoginResult()
    data class PasswordInValid(val errorMessage: String) : LoginResult()
    object Loading : LoginResult()
}

data class LoginState(
    val emailValidateErrorMessage: String = "",
    val passwordValidateErrorMessage: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val user: User? = null
) : IMviState
