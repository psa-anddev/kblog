package com.psa.kblog.di

import android.app.Application
import com.psa.kblog.KBlog
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.register.RegisterInput
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [(GatewaysModule::class),
    (UsersModule::class), (BlogsModule::class),
    (AndroidSupportInjectionModule::class),
    (BaseDeliveryModule::class)])
interface GlobalComponent: AndroidInjector<KBlog> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder
        fun usersModule(module: UsersModule): Builder

        fun build(): GlobalComponent
    }

    val checkLogin: CheckLoginInput
    val register: RegisterInput
}