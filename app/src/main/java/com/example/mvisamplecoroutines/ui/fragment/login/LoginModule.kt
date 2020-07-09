package com.example.mvisamplecoroutines.ui.fragment.login

import com.example.mvisamplecoroutines.utils.Validator
import com.example.mvisamplecoroutines.utils.ValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@InstallIn(LoginFragment::class)
abstract class LoginModule {
    @Binds
    abstract fun bindValidator(validatorImpl: ValidatorImpl): Validator
}