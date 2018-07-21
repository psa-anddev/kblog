package com.psa.kblog.users.splash

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.psa.kblog.di.CustomViewModelFactory
import com.psa.kblog.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SplashModule {
    @ContributesAndroidInjector
    abstract fun bindSplashFragment(): SplashFragment

    @Binds
    internal abstract fun bindViewModelFactory(
            factory: CustomViewModelFactory)
            : ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel
}