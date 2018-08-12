package com.psa.kblog.users

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.psa.kblog.di.CustomViewModelFactory
import com.psa.kblog.di.ViewModelKey
import com.psa.kblog.users.register.RegisterInput
import com.psa.kblog.users.register.RegisterOutput
import com.psa.kblog.users.register.RegisterRequest
import com.psa.kblog.users.registration.RegisterFragment
import com.psa.kblog.users.registration.RegistrationViewModel
import com.psa.kblog.users.splash.SplashFragment
import com.psa.kblog.users.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UsersDeliveryModule {
    @ContributesAndroidInjector
    abstract fun bindSplashFragment(): SplashFragment
    @ContributesAndroidInjector
    abstract fun bindRegisterFragment(): RegisterFragment

    @Binds
    internal abstract fun bindViewModelFactory(
            factory: CustomViewModelFactory)
            : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    internal abstract fun registrationViewModel(viewModel: RegistrationViewModel): ViewModel
}