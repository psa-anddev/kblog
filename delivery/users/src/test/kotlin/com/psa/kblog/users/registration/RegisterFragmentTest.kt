package com.psa.kblog.users.registration

import com.nhaarman.mockito_kotlin.mock
import com.psa.kblog.KBlog
import com.psa.kblog.di.*
import com.psa.kblog.users.register.RegisterInput
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

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
        fragment.viewModelFactory
    }
}

private val register = mock<RegisterInput> {  }

private class RegisterFragmentApplication: KBlog() {
    private val component: AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> by
    lazy {
        DaggerGlobalComponent.builder()
                .application(this)
                .baseDeliveryModule(BaseDeliveryModule(this))
                .usersModule(TestUsersModule(registerInteractor = register))
                .build()
    }

    override fun applicationInjector():
            AndroidInjector<out MultiFeatureDaggerApplication<GlobalComponent>> =
            component
}