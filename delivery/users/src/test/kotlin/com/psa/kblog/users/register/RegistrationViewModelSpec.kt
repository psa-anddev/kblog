package com.psa.kblog.users.register

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.users.R
import com.psa.kblog.utils.listeners.InstantTaskExecutorListener
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

class RegistrationViewModelSpec : ShouldSpec({
    should("issue registration request") {
        val registrationInteractor = mock<RegisterInput> { }
        val viewModel = RegistrationViewModel(Provider { registrationInteractor })

        forAll(Gen.string(),
                Gen.string(),
                Gen.string(),
                Gen.string(),
                Gen.string()) { id, firstName, lastName, password, confirmPassword ->
            viewModel.register(id = id,
                    firstName = firstName,
                    lastName = lastName,
                    password = password,
                    confirmPassword = confirmPassword)

            then(registrationInteractor).should()
                    .execute(
                            RegisterRequest(id, firstName, lastName, password, confirmPassword),
                            viewModel)
            true
        }
    }

    should("handle a successful registration") {
        val viewModel = RegistrationViewModel(Provider { mock<RegisterInput>() })

        viewModel.generateViewModel(RegisterResponse())

        viewModel.registered.value shouldBe true
    }

    should("be a view model") {
        RegistrationViewModel(Provider { mock<RegisterInput> { }}) should
                beInstanceOf<ViewModel>()
    }

    should("handle registration within session") {
        val viewModel = RegistrationViewModel(Provider { mock<RegisterInput>() })

        viewModel.generateViewModel(RegistrationWithSessionStarted())

        viewModel.error.value shouldBe R.string.sessionIsStarted
    }

    should("handle duplicated user") {
        val viewModel = RegistrationViewModel(Provider { mock<RegisterInput>() })

        viewModel.generateViewModel(DuplicatedUser())

        viewModel.idHint.value shouldBe R.string.userAlreadyExists
    }

    should("handle password do not match") {
        val viewModel = RegistrationViewModel(Provider { mock<RegisterInput>() })

        viewModel.generateViewModel(PasswordsDoNotMatch())

        viewModel.confirmPasswordHint.value shouldBe R.string.passwordsDoNotMatch
    }

    should("handle registration failure") {
        val viewModel = RegistrationViewModel(Provider { mock<RegisterInput>() })

        viewModel.generateViewModel(RegistrationFailed(Throwable()))

        viewModel.error.value shouldBe R.string.registrationError
    }
}) {
    override fun listeners(): List<TestListener> =
            super.listeners() + InstantTaskExecutorListener()
}
