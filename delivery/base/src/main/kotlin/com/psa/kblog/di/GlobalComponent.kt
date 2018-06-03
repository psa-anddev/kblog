package com.psa.kblog.di

import android.app.Application
import com.psa.kblog.KBlog
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(GatewaysModule::class),
    (UsersModule::class), (BlogsModule::class),
    (AndroidSupportInjectionModule::class)])
interface GlobalComponent: AndroidInjector<KBlog> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): GlobalComponent
    }
}