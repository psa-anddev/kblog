package com.psa.kblog

import com.psa.kblog.di.*
import dagger.android.AndroidInjector

class KBlog : MultiFeatureDaggerApplication<GlobalComponent>() {
    val component: AndroidInjector<MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        @Suppress("UNCHECKED_CAST")
        DaggerGlobalComponent.builder()
                .application(this)
                .baseDeliveryModule(BaseDeliveryModule(this))
                .build() as AndroidInjector<MultiFeatureDaggerApplication<GlobalComponent>>
    }

    override fun applicationInjector():
            AndroidInjector<MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}