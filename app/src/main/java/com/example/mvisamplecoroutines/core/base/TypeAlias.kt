package com.example.mvisamplecoroutines.core.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import java.util.function.BiFunction

typealias FLowTransformer<UpStream, DownStream> = (upstream: Flow<UpStream>) -> Flow<DownStream>
