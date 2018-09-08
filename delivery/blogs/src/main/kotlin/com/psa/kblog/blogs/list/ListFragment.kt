package com.psa.kblog.blogs.list

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import androidx.navigation.fragment.findNavController
import com.psa.kblog.blogs.R
import com.psa.kblog.extensions.navigateToUsers
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.blogs_list.*
import kotlinx.android.synthetic.main.blogs_list.view.*
import javax.inject.Inject

class ListFragment : DaggerFragment(), LifecycleObserver {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }
    private val adapter: BlogsAdapter get() = blogs.adapter as BlogsAdapter

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
        lifecycle.addObserver(this)
        setHasOptionsMenu(true)
        view.create.setOnClickListener { findNavController().navigate(R.id.createBlogAction) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.blos_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            if (item?.itemId == R.id.logOut)
                viewModel.logOut().let { true }
            else
                super.onOptionsItemSelected(item)

    @OnLifecycleEvent(ON_START)
    fun loadBlogs() {
        viewModel.blogs.observe(this, Observer {
            adapter.add(it ?: emptyList())
        })
        viewModel.list()
    }

    @OnLifecycleEvent(ON_START)
    fun listenToErrors() {
        viewModel.errorMessage.observe(this, Observer {
            if (view != null && it != null)
                Snackbar.make(view as View, it, Snackbar.LENGTH_LONG).show()
        })
    }

    @OnLifecycleEvent(ON_START)
    fun listenToLogOut() {
        viewModel.logout.observe(this, Observer { navigateToUsers() })
    }
}