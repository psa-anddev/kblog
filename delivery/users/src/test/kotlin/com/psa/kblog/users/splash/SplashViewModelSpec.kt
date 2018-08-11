package com.psa.kblog.users.splash

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.entities.User
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.checklogin.CheckLoginResponse
import com.psa.kblog.users.checklogin.UserNotLoggedIn
import com.psa.kblog.users.splash.SessionStatus.LOGGED_IN
import com.psa.kblog.users.splash.SessionStatus.UNKNOWN_USER
import com.psa.kblog.utils.listeners.InstantTaskExecutorListener
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

class SplashViewModelSpec : ShouldSpec({
    should("be a view model") {
        SplashViewModel(Provider { mock<CheckLoginInput> {} }) should beInstanceOf(ViewModel::class)
    }

    should("check if there is a user logged in") {
        val checkLogin = mock<CheckLoginInput> { }
        val viewModel = SplashViewModel(Provider { checkLogin })

        viewModel.checkLogin()

        then(checkLogin).should().execute(isA(), eq(viewModel))
    }

    should("communicate the view that the user is logged in") {
        val viewModel = SplashViewModel(Provider { mock<CheckLoginInput> { } })

        viewModel.generateViewModel(
                CheckLoginResponse(User(id = "bb", firstName = "Bitte", lastName = "Bitte")))

        viewModel.sessionState.value shouldBe LOGGED_IN
    }

    should("communicate the view that the user is not logged in") {
        val viewModel = SplashViewModel(Provider { mock<CheckLoginInput> { } })

        viewModel.generateViewModel(UserNotLoggedIn(Throwable()))

        viewModel.sessionState.value shouldBe UNKNOWN_USER
    }
}) {
    override fun listeners(): List<TestListener> =
            super.listeners() + InstantTaskExecutorListener()
}
