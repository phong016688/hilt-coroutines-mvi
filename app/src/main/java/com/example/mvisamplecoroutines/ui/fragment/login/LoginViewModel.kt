package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import com.example.mvisamplecoroutines.core.base.BaseViewModel
import com.example.mvisamplecoroutines.core.base.FLowTransformer
import com.example.mvisamplecoroutines.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@ExperimentalCoroutinesApi
@FlowPreview
class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository
) : BaseViewModel<LoginIntent, LoginState, LoginAction, LoginResult>() {

    override val actionProcessor: FLowTransformer<LoginAction, LoginResult> = { action ->
        action.flatMapConcat {
            when (it) {
                is LoginAction.InitialUiAction -> flowOf(LoginResult.InitialResult)
                is LoginAction.NoAction -> flowOf(LoginResult.NoResult)
                is LoginAction.ClickLoginEmailPassword -> flow {
                    emit(LoginResult.LoginSuccess(repository.login()))
                }
                else -> flowOf(LoginResult.NoResult)
            }
        }
    }
    override val intentFilter: FLowTransformer<LoginIntent, LoginIntent> = { intents ->
        merge(
            intents.filterIsInstance<LoginIntent.InitialIntent>().take(1),
            intents.filterNot { it is LoginIntent.InitialIntent }
        )
    }


    override fun initState(): LoginState = LoginState.Initial

    override suspend fun actionFormIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.InitialIntent -> LoginAction.InitialUiAction
            is LoginIntent.LoginWithEmailPassword -> LoginAction.ClickLoginEmailPassword(
                intent.email,
                intent.password
            )
            else -> LoginAction.NoAction
        }
    }

    override suspend fun reducer(previousState: LoginState, result: LoginResult): LoginState {
        return when (result) {
            is LoginResult.InitialResult -> previousState
            is LoginResult.LoginSuccess -> LoginState.LoginSuccess
            is LoginResult.LoginFailure -> LoginState.LoginFailure(result.errorMessage)
            else -> LoginState.LoginSuccess
        }
    }
}