package com.psa.kblog.users.login

import android.support.design.widget.TextInputLayout
import android.widget.Button
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.KBlog
import com.psa.kblog.di.DaggerGlobalComponent
import com.psa.kblog.di.GlobalComponent
import com.psa.kblog.di.MultiFeatureDaggerApplication
import com.psa.kblog.di.TestUsersModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import kotlinx.android.synthetic.main.login.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(application = LoginFragmentApplication::class)
class LoginFragmentTest {
    @Test
    fun `should be a dagger fragment`() {
        LogInFragment() should beInstanceOf(DaggerFragment::class)
    }

    @Test
    fun `should have a user name input field`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)
        fragment.userId should beInstanceOf(TextInputLayout::class)
    }

    @Test
    fun `should have a password input field`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)
        fragment.password should beInstanceOf(TextInputLayout::class)
    }

    @Test
    fun `should have a log in button`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)
        fragment.logIn should beInstanceOf(Button::class)
    }

    @Test
    fun `should issue log in request on tapping log in`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.userId.editText?.setText(UUID.randomUUID().toString())
        fragment.password.editText?.setText(UUID.randomUUID().toString())
        fragment.logIn.performClick()

        then(loginInteractor).should().execute(
                LoginRequest(fragment.userId?.editText?.text?.toString() ?: "",
                        fragment.password?.editText?.text?.toString() ?: ""),
                fragment.viewModel)
    }

    @Test
    fun `should go to blogs activity on successful log in`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(LoginResponse())

        shadowOf(fragment.activity).nextStartedActivity shouldNotBe null
    }

    @Test
    fun `should not navigate to blogs if there was a logged in user already`() {
        val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(UserAlreadyLoggedIn())

        shadowOf(fragment.activity).nextStartedActivity shouldBe null
    }

    @Test
    fun `should not navigate to blogs if the username or the password are incorrect`() {
                val fragment = LogInFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(LoginFailed(Throwable()))

        shadowOf(fragment.activity).nextStartedActivity shouldBe null
    }
}

val loginInteractor: LoginInput = mock {  }

private class LoginFragmentApplication: KBlog() {
    private val component: AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        DaggerGlobalComponent.builder()
                .application(this)
                .usersModule(TestUsersModule(logInInteractor = loginInteractor))
                .build()
    }

    override fun applicationInjector():
            AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}
