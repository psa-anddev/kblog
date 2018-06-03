package com.psa.kblog

import com.psa.kblog.di.DaggerGlobalComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class KBlog: DaggerApplication() {
    private val component: AndroidInjector<out DaggerApplication> by
    lazy { DaggerGlobalComponent.builder()
                .application(this)
                .build() }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = component
}