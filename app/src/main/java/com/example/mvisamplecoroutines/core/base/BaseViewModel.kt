package com.example.mvisamplecoroutines.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvisamplecoroutines.core.*
import com.example.mvisamplecoroutines.utils.compose
import com.example.mvisamplecoroutines.utils.logDebug
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.LazyThreadSafetyMode.*

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<I : IMviIntent, S : IMviState, A : IMviAction, R : IMviResult>
    : ViewModel(), IMviViewModel<I, S> {

    private val intentsBC: BroadcastChannel<I> = BroadcastChannel(capacity = Channel.BUFFERED)

    private val viewStates: StateFlow<S> by lazy(NONE) { initViewStateFlow() }

    protected abstract fun initState(): S

    protected abstract val actionProcessor: FLowTransformer<A, R>

    protected abstract val intentFilter: FLowTransformer<I, I>

    protected abstract suspend fun actionFormIntent(intent: I): A

    protected abstract suspend fun reducer(previousState: S, result: R): S

    override suspend fun processIntents(intent: I) = intentsBC.send(intent)

    override fun states(): Flow<S> = viewStates

    private fun initViewStateFlow(): StateFlow<S> {
        return MutableStateFlow(initState()).apply {
            intentsBC.asFlow()
                .compose(intentFilter)
                .map(::actionFormIntent)
                .compose(actionProcessor)
                .scan(initState(), ::reducer)
                .onEach { value = it }
                .distinctUntilChanged()
                .catch { logDebug(it.message.toString()) }
                .launchIn(viewModelScope)
        }
    }
}
