package com.psa.kblog.di

import com.nhaarman.mockito_kotlin.mock
import com.psa.kblog.users.checklogin.CheckLogin
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.login.Login
import com.psa.kblog.users.login.LoginInput
import com.psa.kblog.users.logout.Logout
import com.psa.kblog.users.logout.LogoutInput
import com.psa.kblog.users.register.Register
import com.psa.kblog.users.register.RegisterInput
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class TestUsersModuleSpec: ShouldSpec({
    should("be a users module") {
        TestUsersModule() should beInstanceOf(UsersModule::class)
    }

    should("provide the default log in interactor") {
        TestUsersModule().provideLoginInteractor(mock { }) should
                beInstanceOf(Login::class)
    }

    should("provide the log in interactor that I pass") {
        val logIn = mock<LoginInput> {  }
        TestUsersModule(logInInteractor = logIn)
                .provideLoginInteractor(mock { }) shouldBe logIn
    }

    should("provide the default log out interactor") {
        TestUsersModule().provideLogOutInteractor(mock { }) should
                beInstanceOf(Logout::class)
    }

    should("provide the log out interactor that I pass") {
        val logOut = mock<LogoutInput> {  }
        TestUsersModule(logOutInteractor= logOut)
                .provideLogOutInteractor(mock { }) shouldBe logOut
    }

    should("provide the default registration interactor") {
        TestUsersModule().provideRegisterInteractor(mock { }) should
                beInstanceOf(Register::class)
    }

    should("provide the registration interactor I pass") {
        val registerInteractor = mock<RegisterInput> {  }
        TestUsersModule(registerInteractor = registerInteractor)
                .provideRegisterInteractor(mock { }) shouldBe registerInteractor
    }

    should("provide the default check log in interactor") {
        TestUsersModule().provideCheckLoginInteractor(mock { }) should
                beInstanceOf(CheckLogin::class)
    }

    should("provide the check log in interactor that I pass") {
        val checkLogin = mock<CheckLoginInput> {}
        TestUsersModule(checkLoginInteractor = checkLogin)
                .provideCheckLoginInteractor(mock {}) shouldBe checkLogin
    }
})
