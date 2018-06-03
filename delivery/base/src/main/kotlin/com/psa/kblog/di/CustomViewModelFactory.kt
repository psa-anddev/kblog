package com.psa.kblog.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class CustomViewModelFactory
@Inject constructor(private val viewModelsMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    :ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            viewModelsMap[modelClass]?.get() as T
}