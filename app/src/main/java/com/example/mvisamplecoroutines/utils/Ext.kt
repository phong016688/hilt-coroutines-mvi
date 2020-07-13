package com.example.mvisamplecoroutines.utils

import android.content.Context
import android.hardware.input.InputManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.hideKeyBoard() {
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun <T> Flow<T>.replay(count: Int): Flow<T> {
    require(count >= 0) { "Drop count should be non-negative, but had $count" }
    return flow {
        var index = 0
        val listValueReplay = arrayListOf<T>()
        this@replay.collect {
            emit(it)
            if (index++ + count >= this@replay.count()) listValueReplay.add(it)
        }
        listValueReplay.asFlow().collect {
            emit(it)
        }
    }
}