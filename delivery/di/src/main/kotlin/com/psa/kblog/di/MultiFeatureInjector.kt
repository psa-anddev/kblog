package com.psa.kblog.di

import android.app.Activity
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MultiFeatureInjector<T>  @Inject constructor(
        baseActivityInjector: DispatchingAndroidInjector<Activity>,
        baseSupportFragmentInjector: DispatchingAndroidInjector<Fragment>,
        private val featureInjectorProvider: ManifestFeatureInjectorProvider<T>
) {
    private val injectors = mutableListOf<FeatureAndroidInjector>(BaseFeatureInjector(
        baseActivityInjector, baseSupportFragmentInjector
    ))

    val activityInjector: AndroidInjector<Activity> = AndroidInjector {
        inject(it, FeatureAndroidInjector::activityInjector)
    }
    val supportFragmentInjector: AndroidInjector<Fragment> = AndroidInjector {
        inject(it, FeatureAndroidInjector::supportFragmentInjector)
    }

    private fun<T> inject(it: T,
                          injectorRef: FeatureAndroidInjector.() -> DispatchingAndroidInjector<T>) {

        for(injector in injectors)
            if (injector.injectorRef().maybeInject(it))
                return

        for(injector in findNewInjectors())
            if (injector.injectorRef().maybeInject(it))
                return

        injectors[0].injectorRef().inject(it)
    }

    private fun findNewInjectors(): List<FeatureAndroidInjector> =
            featureInjectorProvider.checkForNewInjectors().apply {
                injectors.addAll(this)
            }
}
