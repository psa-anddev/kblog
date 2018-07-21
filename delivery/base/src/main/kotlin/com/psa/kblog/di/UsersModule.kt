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
class UsersModule {
    @Provides
    fun provideLoginInteractor(usersGateway: UsersGateway): LoginInput =
            Login(usersGateway)

    @Provides
    fun provideLogOutInteractor(usersGateway: UsersGateway): LogoutInput =
            Logout(usersGateway)

    @Provides
    fun provideRegistrationInteractor(usersGateway: UsersGateway): RegisterInput =
            Register(usersGateway)

    @Provides
    fun provideCheckLoginInteractor(usersGateway: UsersGateway): CheckLoginInput =
            CheckLogin(usersGateway)
}