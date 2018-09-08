package com.psa.kblog.blogs.list

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.blogs.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.blogs_list.view.*
import javax.inject.Inject

class ListFragment: DaggerFragment() {
    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.blogs_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.blogs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BlogsAdapter()
        }
    }
}