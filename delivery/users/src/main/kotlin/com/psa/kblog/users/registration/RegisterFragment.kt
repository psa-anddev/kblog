package com.psa.kblog.users.registration

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.users.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RegisterFragment: DaggerFragment() {
    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.register, container, false)
}