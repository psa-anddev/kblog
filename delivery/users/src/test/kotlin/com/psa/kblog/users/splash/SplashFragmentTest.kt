package com.psa.kblog.users.splash

import android.support.v4.app.Fragment
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.KBlog
import com.psa.kblog.di.*
import com.psa.kblog.entities.User
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.checklogin.CheckLoginResponse
import com.psa.kblog.users.checklogin.UserNotLoggedIn
import dagger.android.AndroidInjector
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
@Config(application = SplashFragmentApplication::class)
class SplashFragmentTest {
    @Before
    fun setUp() {

    }

    @Test
    fun `should be a fragment`() {
        SplashFragment() should beInstanceOf(Fragment::class)
    }

    @Test
    fun `should inject dependencies`() {
        val fragment = SplashFragment()
        SupportFragmentController.setupFragment(fragment)
        fragment.viewModelFactory shouldNotBe null

    }


    @Test
    fun `should check whether the user is logged in`() {
        val fragment = SplashFragment()

        SupportFragmentController.setupFragment(fragment)

        then(checkLogin).should().execute(isA(), eq(fragment.viewModel))
    }

    @Test
    fun `should go to blogs`() {
        val fragment = SplashFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel
                .generateViewModel(CheckLoginResponse(User("1", "One", "One")))

        val intent = shadowOf(fragment.activity).nextStartedActivity
        intent shouldNotBe null
    }

    @Test
    fun `should go to user options screen`() {
        val fragment = SplashFragment()
        SupportFragmentController.setupFragment(fragment)

        fragment.viewModel.generateViewModel(UserNotLoggedIn(Throwable()))

        shadowOf(fragment.activity).nextStartedActivity shouldBe null
    }
}

private val checkLogin = mock<CheckLoginInput> { }

private class SplashFragmentApplication : KBlog() {
    private val component: AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        DaggerGlobalComponent.builder()
                .application(this)
                .baseDeliveryModule(BaseDeliveryModule(this))
                .usersModule(TestUsersModule(checkLoginInteractor = checkLogin))
                .build()
    }

    override fun applicationInjector():
            AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}
