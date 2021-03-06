package com.psa.kblog.di

import android.app.Application
import com.psa.kblog.KBlog
import com.psa.kblog.blogs.create.CreateBlogInput
import com.psa.kblog.blogs.list.ListInput
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.login.LoginInput
import com.psa.kblog.users.logout.LogoutInput
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
    val logIn: LoginInput
    val logOut: LogoutInput
    val list: ListInput
    val create: CreateBlogInput
}