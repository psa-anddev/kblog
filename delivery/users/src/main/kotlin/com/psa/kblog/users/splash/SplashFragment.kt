package com.psa.kblog.users.splash

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.support.v4.app.Fragment
import com.psa.kblog.KBlog
import com.psa.kblog.di.GlobalComponent
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SplashFragment : Fragment(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        supportFragmentInjector().inject(this)
        super.onAttach(context)
    }

    @Suppress("UNCHECKED_CAST")
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return DaggerSplashComponent.builder()
                .globalComponent((activity?.application as KBlog).component as GlobalComponent)
                .create(this)
                as AndroidInjector<Fragment>
    }
}