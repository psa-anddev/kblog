package com.psa.kblog.blogs

import com.psa.kblog.di.FeatureAndroidInjector
import com.psa.kblog.di.FeatureComponentBuilder
import com.psa.kblog.di.GlobalComponent

class BlogsComponentBuilder: FeatureComponentBuilder<GlobalComponent> {
    override fun createComponent(component: GlobalComponent):
            FeatureAndroidInjector {
        return DaggerBlogsComponent.builder()
                .globalComponent(component)
                .build()

    }
}