package com.psa.kblog.users.login

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.extensions.navigateToBlogs
import com.psa.kblog.users.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject

class LogInFragment : DaggerFragment(), LifecycleObserver {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    internal val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logIn.setOnClickListener {
            viewModel.login(userId.editText?.text.toString(),
                    password.editText?.text.toString())
        }
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(ON_START)
    fun handleSuccessfulLogin() {
        viewModel.success
                .observe(this,
                        Observer { if (it == true) navigateToBlogs() })
    }

    @Suppress("USELESS_CAST")
    @OnLifecycleEvent(ON_START)
    fun handleErrors() {
        viewModel.error
                .observe(this, Observer {
                    if (view != null && it != null)
                        Snackbar.make(view as View, it as Int, Snackbar.LENGTH_LONG).show()
                })
    }
}