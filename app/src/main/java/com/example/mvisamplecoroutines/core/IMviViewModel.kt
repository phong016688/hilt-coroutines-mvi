package com.example.mvisamplecoroutines.core

import kotlinx.coroutines.flow.Flow

interface IMviViewModel<I : IMviIntent, S : IMviState> {
    suspend fun processIntents(intent: I)
    fun states(): Flow<S>
}