package com.psa.kblog.di

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class MultiFeatureDaggerApplication<T>:
        Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject internal lateinit var multiFeatureInjector: MultiFeatureInjector<T>

    @Volatile
    private var shouldInject = true

    override fun activityInjector(): AndroidInjector<Activity> =
            multiFeatureInjector.activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
            multiFeatureInjector.supportFragmentInjector

    protected abstract fun applicationInjector(): AndroidInjector<out MultiFeatureDaggerApplication<T>>

    @Suppress("UNCHECKED_CAST")
    override fun onCreate() {
        super.onCreate()
        if (shouldInject) {
            synchronized(this) {
                val injector =
                        applicationInjector() as AndroidInjector<MultiFeatureDaggerApplication<T>>
                injector.inject(this)
                if (shouldInject)
                    throw IllegalStateException("Application context couldn't be injected")
            }
        }
    }

    @Inject
    internal fun setInjected() {
        shouldInject = false
    }
}