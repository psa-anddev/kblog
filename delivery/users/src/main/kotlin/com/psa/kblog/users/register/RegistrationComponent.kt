package com.psa.kblog.users.register

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.GlobalComponent
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(RegistrationModule::class),
    (AndroidSupportInjectionModule::class)],
        dependencies = [(GlobalComponent::class)])
abstract class RegistrationComponent: FeatureAndroidInjector {

    @Component.Builder
    abstract class Builder {
        abstract fun globalComponent(component: GlobalComponent): Builder
        abstract fun build(): RegistrationComponent
    }
}