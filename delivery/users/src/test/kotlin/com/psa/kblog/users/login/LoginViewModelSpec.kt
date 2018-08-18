package com.psa.kblog.users.login

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.users.R
import com.psa.kblog.utils.listeners.InstantTaskExecutorListener
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

class LoginViewModelSpec: ShouldSpec({
    should("be a view model") {
        val interactor = mock<LoginInput> {  }
        LoginViewModel(Provider { interactor }) should
                beInstanceOf(ViewModel::class)
    }

    should("execute log in") {
        forAll {userName: String, password: String ->
            val interactor = mock<LoginInput> {  }
            val viewModel = LoginViewModel(Provider { interactor })

            viewModel.login(userName, password)
            then(interactor).should()
                    .execute(LoginRequest(userName, password),
                            viewModel)
            true
        }
    }

    should("communicate the view that the log in was successful") {
        val viewModel = LoginViewModel(Provider { mock<LoginInput> { }})

        viewModel.generateViewModel(LoginResponse())

        viewModel.success.value shouldBe true
    }

    should("communicate log in error when a user is already logged in") {
        val viewModel = LoginViewModel(Provider { mock<LoginInput> { }})

        viewModel.generateViewModel(UserAlreadyLoggedIn())

        viewModel.success.value shouldBe false
        viewModel.error.value shouldBe R.string.loginError
    }

    should("communicate the user that he typed the wrong user or password") {
        val viewModel = LoginViewModel(Provider { mock<LoginInput> { }})

        viewModel.generateViewModel(LoginFailed(Throwable()))

        viewModel.success.value shouldBe false
        viewModel.error.value shouldBe R.string.wrongUserOrPassword
    }
}) {
    override fun listeners(): List<TestListener> =
            super.listeners() + InstantTaskExecutorListener()
}
