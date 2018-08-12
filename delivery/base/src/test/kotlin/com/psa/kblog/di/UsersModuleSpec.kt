package com.psa.kblog.di

import com.nhaarman.mockito_kotlin.mock
import com.psa.kblog.users.checklogin.CheckLogin
import com.psa.kblog.users.login.Login
import com.psa.kblog.users.logout.Logout
import com.psa.kblog.users.register.Register
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.specs.ShouldSpec

class UsersModuleSpec: ShouldSpec({
    should("return an interactor for log in") {
        UsersModule().provideLoginInteractor(mock {  }) should
                beInstanceOf(Login::class)
    }

    should("return an interactor for log out") {
        UsersModule().provideLogOutInteractor(mock { }) should
                beInstanceOf(Logout::class)
    }

    should("return a registration interactor") {
        UsersModule().provideRegisterInteractor(mock { }) should
                beInstanceOf(Register::class)
    }

    should("return the right check login use case") {
        UsersModule().provideCheckLoginInteractor(mock { }) should
                beInstanceOf(CheckLogin::class)
    }
})
