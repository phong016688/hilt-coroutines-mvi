package com.example.mvisamplecoroutines.ui.fragment.login

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@InstallIn(FragmentComponent::class)
class LoginModule {
    @Provides
    @FragmentScoped
    fun providerStringTest(): String = "123456789"
}