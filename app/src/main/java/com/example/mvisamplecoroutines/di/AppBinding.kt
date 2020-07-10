package com.example.mvisamplecoroutines.di

import com.example.mvisamplecoroutines.utils.Validator
import com.example.mvisamplecoroutines.utils.ValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBinding {
    @Binds
    abstract fun bindValidator(validatorImpl: ValidatorImpl): Validator
}