package com.psa.kblog.blogs

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.psa.kblog.blogs.create.CreateFragment
import com.psa.kblog.blogs.create.CreateViewModel
import com.psa.kblog.blogs.list.ListFragment
import com.psa.kblog.blogs.list.ListViewModel
import com.psa.kblog.di.CustomViewModelFactory
import com.psa.kblog.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class BlogsDeliveryModule {
    @ContributesAndroidInjector
    abstract fun bindListFragment(): ListFragment
    @ContributesAndroidInjector
    abstract fun bindCreateFragment(): CreateFragment

    @Binds
    internal abstract fun bindViewModelFactory(
            factory: CustomViewModelFactory)
    : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    internal abstract fun listViewModel(viewModel: ListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateViewModel::class)
    internal abstract fun createViewModel(viewModel: CreateViewModel) : ViewModel
}