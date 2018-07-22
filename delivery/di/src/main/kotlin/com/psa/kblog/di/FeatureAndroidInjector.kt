package com.psa.kblog.di

import android.app.Activity
import android.support.v4.app.Fragment
import dagger.android.DispatchingAndroidInjector

interface FeatureAndroidInjector {
    val activityInjector: DispatchingAndroidInjector<Activity>
    val supportFragmentInjector: DispatchingAndroidInjector<Fragment>
}