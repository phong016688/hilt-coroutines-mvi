package com.example.mvisamplecoroutines.utils

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
fun NavController.onDestinationChangedEvents(): Flow<NavDestination> {
    return callbackFlow {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            offer(destination)
        }
        this@onDestinationChangedEvents.addOnDestinationChangedListener(listener)
        awaitClose { this@onDestinationChangedEvents.removeOnDestinationChangedListener(listener) }
    }
}

@ExperimentalCoroutinesApi
fun BottomNavigationView.onItemSelectedEvents(): Flow<MenuItem> {
    return callbackFlow {
        setOnNavigationItemSelectedListener { offer(it) }
        awaitClose { setOnNavigationItemSelectedListener(null) }
    }.catch { cause -> logDebug(cause.message.toString()) }
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun logDebug(s: String, tag: String = "###########") {
    Log.d(tag, s)
}

fun <T, R> Flow<T>.compose(transformer: Flow<T>.() -> Flow<R>): Flow<R> {
    return transformer(this)
}

@ExperimentalCoroutinesApi
fun TextInputEditText.onTextChangesEvents() = callbackFlow {
    val listener = this@onTextChangesEvents.addTextChangedListener {
        it?.let { editable -> offer(editable) }
    }
    awaitClose { this@onTextChangesEvents.removeTextChangedListener(listener) }
}.catch { logDebug(it.message.toString()) }

@ExperimentalCoroutinesApi
fun View.clicks() = callbackFlow {
    this@clicks.setOnClickListener {
        offer(Unit)
    }
    awaitClose { this@clicks.setOnClickListener(null) }
}.catch { logDebug(it.message.toString()) }