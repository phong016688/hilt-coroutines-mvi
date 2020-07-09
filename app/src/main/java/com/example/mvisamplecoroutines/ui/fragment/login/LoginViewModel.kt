package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import com.example.mvisamplecoroutines.core.base.BaseViewModel
import com.example.mvisamplecoroutines.core.base.FLowTransformer
import com.example.mvisamplecoroutines.domain.repository.Repository
import com.example.mvisamplecoroutines.utils.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.lang.Exception


@ExperimentalCoroutinesApi
@FlowPreview
class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val validator: Validator
) : BaseViewModel<LoginIntent, LoginState, LoginAction, LoginResult>() {

    override fun initState(): LoginState = LoginState()

    override val actionProcessor: FLowTransformer<LoginAction, LoginResult> = { flow ->
        flow.flatMapConcat { action ->
            when (action) {
                is LoginAction.Initial -> flowOf(LoginResult.Loading)
                is LoginAction.LoginWithEmailPassword -> login(action.email, action.password)
                is LoginAction.ValidateEmail -> validateEmail(action.email)
                is LoginAction.ValidatePassword -> validatePassword(action.password)
            }
        }
    }

    override val intentFilter: FLowTransformer<LoginIntent, LoginIntent> = { intents ->
        merge(
            intents.filterIsInstance<LoginIntent.InitialIntent>().take(1),
            intents.filterNot { it is LoginIntent.InitialIntent }
        )
    }

    override suspend fun actionFormIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.InitialIntent -> LoginAction.Initial
            is LoginIntent.EnterEmailField -> LoginAction.ValidateEmail(email = intent.email)
            is LoginIntent.EnterPasswordField -> LoginAction.ValidatePassword(password = intent.password)
            is LoginIntent.SubmitLogin -> LoginAction.LoginWithEmailPassword(
                email = intent.email,
                password = intent.password
            )
        }
    }

    override suspend fun reducer(previousState: LoginState, result: LoginResult): LoginState {
        return when (result) {
            is LoginResult.Loading -> previousState.copy(isLoading = true)
            is LoginResult.LoginSuccess -> previousState.copy(
                emailValidateErrorMessage = "",
                passwordValidateErrorMessage = "",
                errorMessage = "",
                isLoading = false,
                userId = result.userId
            )
            is LoginResult.LoginFailure -> previousState.copy(
                emailValidateErrorMessage = "",
                passwordValidateErrorMessage = "",
                errorMessage = result.error,
                isLoading = false,
                userId = ""
            )
            is LoginResult.EmailValid -> previousState.copy(
                emailValidateErrorMessage = "",
                isLoading = false
            )
            is LoginResult.PasswordValid -> previousState.copy(
                passwordValidateErrorMessage = "",
                isLoading = false
            )
            is LoginResult.EmailInValid -> previousState.copy(
                emailValidateErrorMessage = result.errorMessage,
                isLoading = false
            )
            is LoginResult.PasswordInValid -> previousState.copy(
                passwordValidateErrorMessage = result.errorMessage,
                isLoading = false
            )
        }
    }

    private fun login(email: String, password: String): Flow<LoginResult> = flow {
        emit(LoginResult.Loading)
        val result = try {
            LoginResult.LoginSuccess(repository.login(email, password))
        } catch (ex: Exception) {
            LoginResult.LoginFailure(ex.message.toString())
        }
        emit(result)
    }.flowOn(Dispatchers.IO + Job())

    private fun validateEmail(email: String): Flow<LoginResult> = flow {
        val result = if (validator.validateEmail(email).isEmpty()) {
            LoginResult.EmailValid
        } else {
            LoginResult.EmailInValid(validator.validateEmail(email))
        }
        emit(result)
    }

    private fun validatePassword(password: String): Flow<LoginResult> = flow {
        val result = if (validator.validatePassword(password).isEmpty()) {
            LoginResult.PasswordValid
        } else {
            LoginResult.PasswordInValid(validator.validatePassword(password))
        }
        emit(result)
    }

}