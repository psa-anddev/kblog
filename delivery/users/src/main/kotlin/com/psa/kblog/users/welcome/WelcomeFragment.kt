package com.psa.kblog.users.welcome

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.psa.kblog.users.R
import kotlinx.android.synthetic.main.welcome.*

class WelcomeFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.welcome, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logIn.setOnClickListener { findNavController().navigate(R.id.loginAction) }
        register.setOnClickListener { findNavController().navigate(R.id.registerAction) }
    }
}