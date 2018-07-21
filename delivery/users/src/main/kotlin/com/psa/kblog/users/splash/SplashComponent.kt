package com.psa.kblog.users.splash

import com.psa.kblog.di.GlobalComponent
import dagger.Component
import dagger.android.AndroidInjector

@Component(modules = [(SplashModule::class)],
        dependencies = [(GlobalComponent::class)])
abstract class SplashComponent : AndroidInjector<SplashFragment> {

    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<SplashFragment>() {
        abstract fun globalComponent(component: GlobalComponent): Builder
    }



}