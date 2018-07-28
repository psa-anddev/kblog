package com.psa.kblog.users.splash

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.GlobalComponent
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(SplashModule::class), (AndroidSupportInjectionModule::class)],
        dependencies = [(GlobalComponent::class)])
abstract class SplashComponent : FeatureAndroidInjector {

    @Component.Builder
    abstract class Builder {
        abstract fun globalComponent(component: GlobalComponent): Builder
        abstract fun build(): SplashComponent
    }



}