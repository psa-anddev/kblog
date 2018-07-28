package com.psa.kblog.di

import com.psa.kblog.gateways.UsersGateway
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.login.LoginInput
import com.psa.kblog.users.logout.LogoutInput
import com.psa.kblog.users.register.RegisterInput

class TestUsersModule(private val logInInteractor: LoginInput? = null,
                      private val logOutInteractor: LogoutInput? = null,
                      private val registerInteractor: RegisterInput? = null,
                      private val checkLoginInteractor: CheckLoginInput? = null):
        UsersModule() {
    override fun provideLoginInteractor(usersGateway: UsersGateway): LoginInput =
            logInInteractor ?: super.provideLoginInteractor(usersGateway)

    override fun provideLogOutInteractor(usersGateway: UsersGateway): LogoutInput =
            logOutInteractor ?: super.provideLogOutInteractor(usersGateway)

    override fun provideRegistrationInteractor(usersGateway: UsersGateway): RegisterInput =
            registerInteractor ?: super.provideRegistrationInteractor(usersGateway)

    override fun provideCheckLoginInteractor(usersGateway: UsersGateway): CheckLoginInput =
            checkLoginInteractor ?: super.provideCheckLoginInteractor(usersGateway)
}