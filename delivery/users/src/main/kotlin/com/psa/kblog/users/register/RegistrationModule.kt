package com.psa.kblog.users.register

import com.psa.kblog.di.CustomViewModelFactory
import com.psa.kblog.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RegistrationModule {
    @ContributesAndroidInjector
    abstract fun bindRegistrationFragment(): RegisterFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: CustomViewModelFactory)

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    internal abstract fun registrationViewModel(viewModel: RegistrationViewModel)
}