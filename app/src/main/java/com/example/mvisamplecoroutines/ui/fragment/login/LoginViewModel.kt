package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import com.example.mvisamplecoroutines.core.base.BaseViewModel
import com.example.mvisamplecoroutines.core.base.FLowTransformer
import com.example.mvisamplecoroutines.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.lang.Exception


@ExperimentalCoroutinesApi
@FlowPreview
class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository
) : BaseViewModel<LoginIntent, LoginState, LoginAction, LoginResult>() {

    override fun initState(): LoginState = LoginState()

    override val actionProcessor: FLowTransformer<LoginAction, LoginResult> = { flow ->
        flow.flatMapConcat { action ->
            when (action) {
                is LoginAction.Initial -> flowOf(LoginResult.Initial)
                is LoginAction.LoginWithEmailPassword -> login(action.email, action.password)
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
            is LoginIntent.SubmitLogin -> LoginAction.LoginWithEmailPassword(
                email = intent.email,
                password = intent.password
            )
        }
    }

    override suspend fun reducer(previousState: LoginState, result: LoginResult): LoginState {
        return when (result) {
            is LoginResult.Initial -> previousState.copy(
                validate = null,
                errorMessage = null,
                isLoading = null,
                userId = null
            )
            is LoginResult.LoginSuccess -> previousState.copy(
                validate = null,
                errorMessage = null,
                isLoading = false,
                userId = result.userId
            )
            is LoginResult.LoginFailure -> previousState.copy(
                validate = null,
                errorMessage = result.error,
                isLoading = false,
                userId = null
            )
            is LoginResult.LoginInValidate -> previousState.copy(
                validate = result.validate,
                errorMessage = null,
                isLoading = false,
                userId = null
            )
            is LoginResult.Loading -> previousState.copy(
                validate = null,
                errorMessage = null,
                isLoading = true,
                userId = null
            )
        }
    }

    fun login(email: String, password: String): Flow<LoginResult> = flow {
        emit(LoginResult.Loading)
        val result = if (email.isNotEmpty() && password.isNotEmpty()) {
            try {
                LoginResult.LoginSuccess(repository.login(email, password))
            } catch (ex: Exception) {
                LoginResult.LoginFailure(ex.message.toString())
            }
        } else {
            LoginResult.LoginInValidate(ValidatorError.checkValid(email, password))
        }
        emit(result)
    }.flowOn(Dispatchers.IO + Job())
}