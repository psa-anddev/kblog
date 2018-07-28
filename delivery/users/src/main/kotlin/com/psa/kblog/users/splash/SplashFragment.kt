package com.psa.kblog.users.splash

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.users.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SplashFragment : DaggerFragment(), LifecycleObserver {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    internal val viewModel: SplashViewModel by
    lazy { ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.splash, container)

    @OnLifecycleEvent(ON_START)
    private fun checkLogin() {
        viewModel.sessionState.observe({ lifecycle },
                { startActivity(Intent().apply {
                    data = Uri.parse("https://psa.com/blogs")
                    `package` = "com.psa.kblogs"
                }) })
        viewModel.checkLogin()
    }
}