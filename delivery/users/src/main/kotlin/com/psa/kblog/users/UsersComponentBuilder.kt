package com.psa.kblog.users

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.FeatureComponentBuilder
import com.psa.kblog.di.GlobalComponent

@Suppress("unused")
class UsersComponentBuilder: FeatureComponentBuilder<GlobalComponent> {
    override fun createComponent(component: GlobalComponent): FeatureAndroidInjector =
            DaggerUsersComponent.builder()
                    .globalComponent(component)
                    .build()
}