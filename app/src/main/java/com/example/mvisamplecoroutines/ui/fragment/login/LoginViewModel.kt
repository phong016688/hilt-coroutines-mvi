package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import com.example.mvisamplecoroutines.core.base.BaseViewModel
import com.example.mvisamplecoroutines.core.base.FLowTransformer
import com.example.mvisamplecoroutines.domain.repository.Repository
import com.example.mvisamplecoroutines.utils.Validator
import com.example.mvisamplecoroutines.utils.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val validator: Validator
) : BaseViewModel<LoginIntent, LoginState, LoginAction, LoginResult>() {

    override fun initState(): LoginState = LoginState()

    override val actionProcessor: FLowTransformer<LoginAction, LoginResult> = { flow ->
        flow.flatMapConcat { action ->
            logDebug(action::class.java.simpleName)
            when (action) {
                is LoginAction.Initial -> emptyFlow()
                is LoginAction.LoginWithEmailPassword -> login(action.email, action.password)
                is LoginAction.ValidateEmail -> validateEmail(action.email)
                is LoginAction.ValidatePassword -> validatePassword(action.password)
            }
        }
    }

    override val intentFilter: FLowTransformer<LoginIntent, LoginIntent> = { intents ->
        merge(
            intents.filterIsInstance<LoginIntent.Initial>().take(1),
            intents.filterNot { it is LoginIntent.Initial }
        )
    }

    override suspend fun actionFormIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.Initial -> LoginAction.Initial
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
                user = result.user
            )
            is LoginResult.LoginFailure -> previousState.copy(
                emailValidateErrorMessage = "",
                passwordValidateErrorMessage = "",
                errorMessage = result.error,
                isLoading = false,
                user = null
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

    private fun login(email: String, password: String): Flow<LoginResult> =
        flow<LoginResult> { emit(LoginResult.LoginSuccess(repository.login(email, password))) }
            .onStart { emit(LoginResult.Loading) }
            .catch { emit(LoginResult.LoginFailure(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    private fun validateEmail(email: String): Flow<LoginResult> {
        val result = if (validator.validateEmail(email).isEmpty()) {
            LoginResult.EmailValid
        } else {
            LoginResult.EmailInValid(validator.validateEmail(email))
        }
        return flowOf(result).flowOn(Dispatchers.IO)
    }

    private fun validatePassword(password: String): Flow<LoginResult> {
        val result = if (validator.validatePassword(password).isEmpty()) {
            LoginResult.PasswordValid
        } else {
            LoginResult.PasswordInValid(validator.validatePassword(password))
        }
        return flowOf(result).flowOn(Dispatchers.IO)
    }

}