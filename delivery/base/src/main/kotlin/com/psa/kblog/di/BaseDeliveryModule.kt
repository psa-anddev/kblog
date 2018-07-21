package com.psa.kblog.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class BaseDeliveryModule(private val context: Context) {
    @Provides
    fun provideContext():Context = context
}