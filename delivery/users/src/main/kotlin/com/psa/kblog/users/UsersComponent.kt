package com.psa.kblog.users

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.GlobalComponent
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(UsersDeliveryModule::class), (AndroidSupportInjectionModule::class)],
        dependencies = [(GlobalComponent::class)])
abstract class UsersComponent : FeatureAndroidInjector {

    @Component.Builder
    abstract class Builder {
        abstract fun globalComponent(component: GlobalComponent): Builder
        abstract fun build(): UsersComponent
    }



}