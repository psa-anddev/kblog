package com.psa.kblog.users.splash

import android.arch.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SplashFragment : DaggerFragment() {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
}