package com.psa.kblog.di

import com.psa.kblog.gateways.UsersGateway
import com.psa.kblog.users.checklogin.CheckLogin
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.login.Login
import com.psa.kblog.users.login.LoginInput
import com.psa.kblog.users.logout.Logout
import com.psa.kblog.users.logout.LogoutInput
import com.psa.kblog.users.register.Register
import com.psa.kblog.users.register.RegisterInput
import dagger.Module
import dagger.Provides

@Module
open class UsersModule {
    @Provides
    open fun provideLoginInteractor(usersGateway: UsersGateway): LoginInput =
            Login(usersGateway)

    @Provides
    open fun provideLogOutInteractor(usersGateway: UsersGateway): LogoutInput =
            Logout(usersGateway)

    @Provides
    open fun provideRegisterInteractor(usersGateway: UsersGateway): RegisterInput =
            Register(usersGateway)

    @Provides
    open fun provideCheckLoginInteractor(usersGateway: UsersGateway): CheckLoginInput =
            CheckLogin(usersGateway)
}