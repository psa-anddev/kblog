package com.psa.kblog.blogs.create

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.psa.kblog.blogs.R
import com.psa.kblog.extensions.navigateToUsers
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.blogs_create.view.*
import javax.inject.Inject

class CreateFragment: DaggerFragment(), LifecycleObserver {
    @Inject internal lateinit var factory: ViewModelProvider.Factory
    private val viewModel: CreateViewModel by lazy {
        ViewModelProviders.of(this, factory).get(CreateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.blogs_create, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.create.setOnClickListener { viewModel.createBlog(view.title.editText?.text.toString(),
                view.text.editText?.text.toString()) }
    }

    @OnLifecycleEvent(ON_START)
    fun listenToBlogCreated() {
        viewModel.created.observe(this,
                Observer { findNavController().navigate(R.id.blogCreatedAction) })
    }

    @OnLifecycleEvent(ON_START)
    fun listenToLogOut() {
        viewModel.logOut.observe(this, Observer { navigateToUsers() })
    }

    @OnLifecycleEvent(ON_START)
    fun listenToError() {
        viewModel.errorMessage.observe(this,
                Observer {
                    if (view != null && it != null)
                    Snackbar.make(view as View, it, LENGTH_LONG).show()
                })
    }
}