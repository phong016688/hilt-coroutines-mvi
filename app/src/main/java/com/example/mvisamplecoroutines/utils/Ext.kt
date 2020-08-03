package com.example.mvisamplecoroutines.utils

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
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

inline fun <T, R> Flow<T>.compose(transformer: Flow<T>.() -> Flow<R>): Flow<R> {
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

@ExperimentalCoroutinesApi
fun <T> Flow<T>.replay(count: Int): Flow<T> {
    require(count >= 0) { "Drop count should be non-negative, but had $count" }
    val lastValue = this@replay.drop(1)
    return this@replay.onStart {
        emit(lastValue.first())
    }.catch {
        if (it is NoSuchElementException)
            logDebug("not require first value")
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}