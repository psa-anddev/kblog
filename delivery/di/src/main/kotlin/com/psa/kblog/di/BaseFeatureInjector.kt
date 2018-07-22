package com.psa.kblog.di

import android.app.Activity
import android.support.v4.app.Fragment
import dagger.android.DispatchingAndroidInjector

class BaseFeatureInjector(override val activityInjector: DispatchingAndroidInjector<Activity>,
                          override val supportFragmentInjector: DispatchingAndroidInjector<Fragment>)
    :FeatureAndroidInjector
