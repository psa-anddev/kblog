package com.psa.kblog

import android.support.v4.app.Fragment
import com.psa.kblog.di.DaggerGlobalComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class KBlog: DaggerApplication(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    private val component: AndroidInjector<out DaggerApplication> by
    lazy { DaggerGlobalComponent.builder()
                .application(this)
                .build() }

    override fun applicationInjector():
            AndroidInjector<out DaggerApplication> =
            component

    override fun supportFragmentInjector():
            AndroidInjector<Fragment> =
            supportFragmentInjector
}