package com.psa.kblog.users.registration

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.extensions.navigateToBlogs
import com.psa.kblog.users.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.register.*
import javax.inject.Inject

class RegisterFragment: DaggerFragment(), LifecycleObserver {
    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory
    @Suppress("CAST_NEVER_SUCCEEDS")
    internal val viewModel: RegistrationViewModel by
    lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(RegistrationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register.setOnClickListener { viewModel.register(userId.editText?.text.toString(),
                firstName.editText?.text.toString(),
                 lastName.editText?.text.toString(),
                password.editText?.text.toString(),
                confirmPassword.editText?.text.toString()) }
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(ON_START)
    fun observeSuccess() {
        viewModel.registered
                .observe(this, Observer { navigateToBlogs() })
    }

    @OnLifecycleEvent(ON_START)
    fun observeUserIdErrors() {
        viewModel.idHint
                .observe(this, Observer {
                    userId.error = getString(R.string.userAlreadyExists)
                    userId.isErrorEnabled = true
                })
    }

    @OnLifecycleEvent(ON_START)
    fun observePasswordsDoNotMatch() {
        viewModel.confirmPasswordHint
                .observe(this, Observer {
                    confirmPassword.isErrorEnabled = true
                    confirmPassword.error = getString(R.string.passwordsDoNotMatch)
                })
    }

    @OnLifecycleEvent(ON_START)
    fun observeErrors() {
        viewModel.error
                .observe(this, Observer {
                    if (view != null && it != null)
                        Snackbar.make(view as View,
                                it, LENGTH_LONG)
                                .show()
                })
    }
}