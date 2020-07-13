package com.example.mvisamplecoroutines.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IMviViewModel<I : IMviIntent, S : IMviState> {
    suspend fun processIntent(intent: I)
    fun states(): StateFlow<S>
}