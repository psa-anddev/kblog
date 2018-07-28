package com.psa.kblog.users.splash

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.FeatureComponentBuilder
import com.psa.kblog.di.GlobalComponent

@Suppress("unused")
class SplashComponentBuilder: FeatureComponentBuilder<GlobalComponent> {
    override fun createComponent(component: GlobalComponent): FeatureAndroidInjector =
            DaggerSplashComponent.builder()
                    .globalComponent(component)
                    .build()
}