package com.psa.kblog.di

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class MultiFeatureDaggerApplication<T>:
        Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Suppress("MemberVisibilityCanBePrivate")
    @Inject internal lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Suppress("MemberVisibilityCanBePrivate")
    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Volatile
    private var shouldInject = true

    override fun activityInjector(): AndroidInjector<Activity> =
            activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
            supportFragmentInjector

    protected abstract fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<T>>

    override fun onCreate() {
        super.onCreate()
        if (shouldInject) {
            synchronized(this) {
                applicationInjector().inject(this)
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