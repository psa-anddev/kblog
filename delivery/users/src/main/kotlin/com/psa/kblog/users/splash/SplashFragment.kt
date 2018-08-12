package com.psa.kblog.users.splash

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.psa.kblog.extensions.navigateToBlogs
import com.psa.kblog.users.R
import com.psa.kblog.users.splash.SessionStatus.LOGGED_IN
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
            inflater.inflate(R.layout.splash, container, false)

    @OnLifecycleEvent(ON_START)
    private fun checkLogin() {
        viewModel.sessionState.observe({ lifecycle },
                {
                    if (it == LOGGED_IN)
                        navigateToBlogs()
                    else
                        try {
                            findNavController().navigate(R.id.welcomeAction)
                        } catch (ex: Throwable) {
                            ex.printStackTrace()
                        }
                })
        viewModel.checkLogin()
    }
}