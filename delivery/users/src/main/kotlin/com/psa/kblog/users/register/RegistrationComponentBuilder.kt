package com.psa.kblog.users.register

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.FeatureComponentBuilder
import com.psa.kblog.di.GlobalComponent

@Suppress("unused")
class RegistrationComponentBuilder : FeatureComponentBuilder<GlobalComponent>  {
    override fun createComponent(component: GlobalComponent): FeatureAndroidInjector =
            DaggerRegistrationComponent.builder()
                    .globalComponent(component)
                    .build()
}