package com.psa.kblog.users.registration

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.KBlog
import com.psa.kblog.di.*
import com.psa.kblog.users.R
import com.psa.kblog.users.register.*
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController
import kotlinx.android.synthetic.main.register.*
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
@Config(application = RegisterFragmentApplication::class)
class RegisterFragmentTest {
    @Test
    fun `should be a dagger fragment`() {
        RegisterFragment() should beInstanceOf(DaggerFragment::class)
    }

    @Test
    fun `should inject dependencies`() {
        val fragment = RegisterFragment()

        SupportFragmentController.setupFragment(fragment)

        fragment.viewModelFactory shouldNotBe null
    }

    @Test
    fun `should call registration when registration button is tapped`() {
        val fragment = RegisterFragment()
        SupportFragmentController.setupFragment(fragment)

        forAll(Gen.string(), Gen.string(), Gen.string(), Gen.string(), Gen.string()) {
            id, firstName, lastName, password, confirmPassword ->
            fragment.userId.editText?.setText(id)
            fragment.firstName.editText?.setText(firstName)
            fragment.lastName.editText?.setText(lastName)
            fragment.password.editText?.setText(password)
            fragment.confirmPassword.editText?.setText(confirmPassword)
            fragment.register.performClick()

            then(register).should().execute(
                    RegisterRequest(
                            id,
                            firstName,
                            lastName,
                            password,
                            confirmPassword),
                    fragment.viewModel)
            true
        }
    }

    @Test
    fun `should go to blogs activity`() {
        val fragment = RegisterFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(RegisterResponse())

        val intent = shadowOf(fragment.activity).nextStartedActivity
        intent shouldNotBe null
        intent?.`package` shouldBe "com.psa.kblog.app"
        intent?.data.toString() shouldBe "kblogs://blogs"
    }

    @Test
    fun `should show duplicated user error`() {
        val fragment = RegisterFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(DuplicatedUser())

        fragment.userId.error shouldBe
                fragment.activity?.getString(R.string.userAlreadyExists)
        fragment.userId.isErrorEnabled shouldBe true
    }

    @Test
    fun `should show passwords do not match`() {
        val fragment = RegisterFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(PasswordsDoNotMatch())

        fragment.confirmPassword.isErrorEnabled shouldBe true
        fragment.confirmPassword.error shouldBe
                fragment.getString(R.string.passwordsDoNotMatch)
    }
}

private val register = mock<RegisterInput> {  }

private class RegisterFragmentApplication: KBlog() {
    private val component: AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        DaggerGlobalComponent.builder()
                .application(this)
                .usersModule(TestUsersModule(registerInteractor = register))
                .build()
    }

    override fun applicationInjector():
            AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}