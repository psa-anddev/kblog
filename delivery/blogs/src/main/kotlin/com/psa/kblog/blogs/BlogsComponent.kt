package com.psa.kblog.blogs

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.GlobalComponent
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(BlogsDeliveryModule::class),
    (AndroidSupportInjectionModule::class)],
        dependencies = [(GlobalComponent::class)])
abstract class BlogsComponent: FeatureAndroidInjector {
    @Component.Builder
    abstract class Builder {
        abstract fun globalComponent(component: GlobalComponent)
        abstract fun build(): BlogsComponent
    }
}