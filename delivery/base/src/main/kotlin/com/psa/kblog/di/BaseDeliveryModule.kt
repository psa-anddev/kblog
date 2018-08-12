package com.psa.kblog.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class BaseDeliveryModule {
    @Binds
    abstract fun context(application: Application): Context
}