package com.psa.kblog.users.splash

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import android.arch.lifecycle.Lifecycle.Event.ON_RESUME
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.*
import com.psa.kblog.entities.User
import com.psa.kblog.users.checklogin.CheckLoginInput
import com.psa.kblog.users.checklogin.CheckLoginResponse
import com.psa.kblog.users.checklogin.UserNotLoggedIn
import com.psa.kblog.users.splash.SessionStatus.*
import io.kotlintest.Description
import io.kotlintest.Spec
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
        var actual: SessionStatus? = null


        val lifecycleOwner: LifecycleOwner = mock { }

        val lifecycle = LifecycleRegistry(lifecycleOwner)
        given(lifecycleOwner.lifecycle)
                .willReturn(lifecycle)

        lifecycle.handleLifecycleEvent(ON_RESUME)
        viewModel.sessionState.observe(lifecycleOwner,
                Observer<SessionStatus> { actual = it })

        viewModel.generateViewModel(
                CheckLoginResponse(User(id = "bb", firstName = "Bitte", lastName = "Bitte")))
        actual shouldBe LOGGED_IN
    }

    should("communicate the view that the user is not logged in") {
        val viewModel = SplashViewModel(Provider { mock<CheckLoginInput> { } })
        var actual: SessionStatus? = null


        val lifecycleOwner: LifecycleOwner = mock { }

        val lifecycle = LifecycleRegistry(lifecycleOwner)
        given(lifecycleOwner.lifecycle)
                .willReturn(lifecycle)

        lifecycle.handleLifecycleEvent(ON_RESUME)
        viewModel.sessionState.observe(lifecycleOwner,
                Observer<SessionStatus> { actual = it })

        viewModel.generateViewModel(UserNotLoggedIn(Throwable()))
        actual shouldBe UNKNOWN_USER
    }
}) {
    override fun listeners(): List<TestListener> {
        return super.listeners() + InstantTaskExecutorExtension()
    }
}

class InstantTaskExecutorExtension : TestListener {
    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean = true

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    override fun afterSpec(description: Description, spec: Spec) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        super.afterSpec(description, spec)
    }
}
