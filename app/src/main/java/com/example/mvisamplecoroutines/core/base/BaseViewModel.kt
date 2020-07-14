package com.example.mvisamplecoroutines.core.base

import androidx.lifecycle.ViewModel
import com.example.mvisamplecoroutines.core.*
import com.example.mvisamplecoroutines.utils.compose
import com.example.mvisamplecoroutines.utils.logDebug
import com.example.mvisamplecoroutines.utils.replay
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException
import kotlin.LazyThreadSafetyMode.*

@InternalCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<I : IMviIntent, S : IMviState, A : IMviAction, R : IMviResult>
    : ViewModel(), IMviViewModel<I, S> {

    // save states when view destroyed and recreate
    private val intentsBC: BroadcastChannel<I> = BroadcastChannel(capacity = Channel.BUFFERED)

    private val viewStates: Flow<S> by lazy(NONE) { initViewStateFlow() }

    protected abstract fun initState(): S

    protected abstract val actionProcessor: FLowTransformer<A, R>

    protected abstract val intentFilter: FLowTransformer<I, I>

    protected abstract suspend fun actionFormIntent(intent: I): A

    protected abstract suspend fun reducer(previousState: S, result: R): S

    //chi emit value initial dau tien(luc tao view lan dau tien ke ca luc xoay mh)
    override suspend fun processIntent(intent: I) = intentsBC.send(intent)

    override fun states(): Flow<S> = viewStates

    private fun initViewStateFlow(): Flow<S> {
        return intentsBC.asFlow()
            .compose(intentFilter)
            .map(::actionFormIntent)
            .compose(actionProcessor)
            .scan(initState(), ::reducer)
            .distinctUntilChanged()
            .replay(1)
            .catch { logDebug(it.message.toString()) }
    }

    override fun onCleared() {
        intentsBC.cancel()
        super.onCleared()
    }
}
