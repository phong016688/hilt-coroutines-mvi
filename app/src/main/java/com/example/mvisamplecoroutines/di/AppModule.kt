package com.example.mvisamplecoroutines.di

import android.content.Context
import com.example.mvisamplecoroutines.BuildConfig
import com.example.mvisamplecoroutines.data.source.locale.preferences.SharePreferences
import com.example.mvisamplecoroutines.data.source.remote.service.RestFullApi
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @ApiKey
    @Provides
    fun provideApiKey() = BuildConfig.OMDb_API_KEY

    @BaseUrl
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return createInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @BaseUrl baseUrl: String,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): RestFullApi = retrofit.create(RestFullApi::class.java)


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = SharePreferences(context)

    private fun createInterceptor(): Interceptor {
        return Interceptor {
            val oldRequest = it.request()
            val newRequest = Request.Builder().apply {
                url(oldRequest.url)
                addHeader("Accept", "application/json")
                method(oldRequest.method, oldRequest.body)
            }.build()
            val response = it.proceed(newRequest)
            response
        }
    }
}