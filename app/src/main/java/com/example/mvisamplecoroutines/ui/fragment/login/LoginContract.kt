package com.example.mvisamplecoroutines.ui.fragment.login

import com.example.mvisamplecoroutines.core.IMviAction
import com.example.mvisamplecoroutines.core.IMviIntent
import com.example.mvisamplecoroutines.core.IMviResult
import com.example.mvisamplecoroutines.core.IMviState
import com.example.mvisamplecoroutines.domain.entity.User

sealed class LoginIntent : IMviIntent {
    data class SubmitLogin(val email: String, val password: String) : LoginIntent()
    data class EnterEmailField(val email: String) : LoginIntent()
    data class EnterPasswordField(val password: String) : LoginIntent()
    object Initial : LoginIntent()
}

fun LoginIntent.toLoginAction(): LoginAction {
    return when (this) {
        is LoginIntent.Initial -> LoginAction.Initial
        is LoginIntent.SubmitLogin -> LoginAction.LoginWithEmailPassword(email, password)
        is LoginIntent.EnterEmailField -> LoginAction.ValidateEmail(email)
        is LoginIntent.EnterPasswordField -> LoginAction.ValidatePassword(password)
    }
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
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val snackBarMessage: String? = null,

    val user: User? = null
) : IMviState

fun LoginState.reducer(result: LoginResult): LoginState {
    return when (result) {
        is LoginResult.LoginSuccess -> this.copy(
            isLoading = false,
            user = result.user,
            emailErrorMessage = null,
            passwordErrorMessage = null,
            snackBarMessage = null
        )
        is LoginResult.LoginFailure -> this.copy(
            isLoading = false,
            user = null,
            emailErrorMessage = null,
            passwordErrorMessage = null,
            snackBarMessage = result.error
        )
        is LoginResult.Loading -> this.copy(isLoading = true)
        is LoginResult.PasswordValid -> this.copy(passwordErrorMessage = null)
        is LoginResult.EmailValid -> this.copy(emailErrorMessage = null)
        is LoginResult.EmailInValid -> this.copy(emailErrorMessage = result.errorMessage)
        is LoginResult.PasswordInValid -> this.copy(passwordErrorMessage = result.errorMessage)
    }
}