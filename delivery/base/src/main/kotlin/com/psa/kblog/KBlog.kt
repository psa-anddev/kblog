package com.psa.kblog

import com.psa.kblog.di.*
import dagger.android.AndroidInjector

class KBlog : MultiFeatureDaggerApplication<GlobalComponent>() {
    private val component: AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        DaggerGlobalComponent.builder()
                .application(this)
                .baseDeliveryModule(BaseDeliveryModule(this))
                .build()
    }

    override fun applicationInjector():
            AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}