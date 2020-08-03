package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.annotation.CheckResult
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvisamplecoroutines.domain.entity.User
import com.example.mvisamplecoroutines.domain.repository.Repository
import com.example.mvisamplecoroutines.utils.Validator
import com.example.mvisamplecoroutines.utils.logDebug
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.rx3.rxObservable
import java.util.concurrent.TimeUnit

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val validator: Validator
) : ViewModel() {
    private val viewStateSubject = BehaviorSubject.createDefault<LoginState>(LoginState())

    private val intentSubject = PublishSubject.create<LoginIntent>()

    fun viewStates(): BehaviorSubject<LoginState> = viewStateSubject

    private val intentFilter = ObservableTransformer<LoginIntent, LoginIntent> { intents ->
        intents.publish { shared ->
            Observable.merge(
                shared.ofType(LoginIntent.Initial::class.java).take(1),
                shared.filter { it !is LoginIntent.Initial }
            )
        }
    }

    private val processor = ObservableTransformer<LoginAction, LoginResult> { actions ->
        actions
            .flatMap { action -> handleAction(action) }
            .onErrorReturn { LoginResult.LoginFailure(it.message.toString()) }
    }


    init {
        intentSubject
            .compose(intentFilter)
            .map { it.toLoginAction() }
            .compose(processor)
            .scan(LoginState(), LoginState::reducer)
            .subscribeOn(Schedulers.io())
            .subscribe(viewStateSubject)
    }

    @CheckResult
    fun processIntents(intents: Observable<LoginIntent>): Disposable {
        return intents.subscribe { intentSubject.onNext(it) }
    }

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

    private fun handleAction(action: LoginAction): Observable<LoginResult> {
        return when (action) {
            is LoginAction.Initial -> Observable.just(LoginResult.Loading)
            is LoginAction.ValidateEmail -> validateEmail(action.email).asObservable()
            is LoginAction.ValidatePassword -> validatePassword(action.password).asObservable()
            is LoginAction.LoginWithEmailPassword -> rxObservable<LoginResult> rxCallSuspend@{
                val user = repository.login(email = action.email, password = action.password)
                send(LoginResult.LoginSuccess(user = user))
            }.startWithItem(LoginResult.Loading)
        }
    }
}