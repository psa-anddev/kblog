package com.psa.kblog.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class BaseDeliveryModule {
    @Binds
    internal abstract fun bindViewModelFactory(
            factory: CustomViewModelFactory)
            : ViewModelProvider.Factory
}