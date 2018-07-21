package com.psa.kblog.users.splash

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.attach
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.spy
import com.psa.kblog.KBlog
import dagger.android.support.HasSupportFragmentInjector
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.specs.ShouldSpec

class SplashFragmentSpec: ShouldSpec({
    should("be a fragment") {
        SplashFragment() should beInstanceOf(Fragment::class)
    }

    should("have a support fragment injector") {
        SplashFragment() should beInstanceOf(HasSupportFragmentInjector::class)
    }

    should("inject dependencies upon attaching") {
        val fragment = SplashFragment()
        val appContext = KBlog().apply {
            onCreate()
        }
        val context = spy<FragmentActivity>()

        given(context.application).willReturn(appContext)
        given(context.applicationContext).willReturn(appContext)

        fragment.attach(context)
        fragment.viewModelFactory
    }
})
