package com.example.mvisamplecoroutines.core.base

import com.example.mvisamplecoroutines.core.IMviAction
import com.example.mvisamplecoroutines.core.IMviResult
import kotlinx.coroutines.flow.FlowCollector

interface MviActionProcessor<A : IMviAction, R : IMviResult> {
    fun transformProcessor(): suspend FlowCollector<R>.(value: A) -> Unit
}