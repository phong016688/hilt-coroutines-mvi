package com.example.mvisamplecoroutines.di

import com.example.mvisamplecoroutines.data.repository.RepositoryImpl
import com.example.mvisamplecoroutines.domain.repository.Repository
import com.example.mvisamplecoroutines.utils.Validator
import com.example.mvisamplecoroutines.utils.ValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}