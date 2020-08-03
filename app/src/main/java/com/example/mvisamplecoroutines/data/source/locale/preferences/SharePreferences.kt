package com.example.mvisamplecoroutines.data.source.locale.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.*
import androidx.core.content.edit
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class SharePreferences(context: Context) :
    ConcurrentHashMap<OnSharedPreferenceChangeListener, Unit>() {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SharedPreferencesKey.KEY_PREFERENCES,
        Context.MODE_PRIVATE
    )

    inline fun <reified T> get(key: String): T {
        return sharedPreferences.run {
            when (T::class.java) {
                String::class.java -> getString(key, "") as T
                Boolean::class.java -> getBoolean(key, false) as T
                Float::class.java -> getFloat(key, 0f) as T
                Int::class.java -> getInt(key, 0) as T
                Long::class.java -> getLong(key, 0) as T
                else -> Gson().fromJson(getString(key, ""), T::class.java)
            }
        }
    }

    fun <T> put(key: String, data: T) {
        sharedPreferences.edit {
            when (data) {
                is String -> putString(key, data)
                is Boolean -> putBoolean(key, data)
                is Float -> putFloat(key, data)
                is Int -> putInt(key, data)
                is Long -> putLong(key, data)
                else -> putString(key, Gson().toJson(data))
            }
            apply()
        }
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
        super.clear()
    }

    fun clearKeys(vararg keys: String) {
        sharedPreferences.edit {
            keys.forEach { remove(it) }
            apply()
        }
    }

    inline fun <reified T> observeKey(key: String): Observable<T> {
        return Observable.create<Unit> { emitter ->
            val listener = OnSharedPreferenceChangeListener { _, keyChange ->
                if (keyChange == key)
                    emitter.onNext(Unit)
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            this[listener] = Unit
            emitter.setCancellable {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
                this.remove(listener)
            }
        }
            .startWithItem(Unit)
            .map { get<T>(key) }
    }
}