package com.psa.kblog.di

import android.app.Application
import com.nhaarman.mockito_kotlin.*
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec

class MultiFeatureDaggerApplicationSpec : ShouldSpec({


    should("be an application") {
        val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {

        }
        val app = object : MultiFeatureDaggerApplication<Any>() {
            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }

        }
        app should beInstanceOf(Application::class)
    }

    should("have an activity injector") {
        val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {

        }
        val app = object : MultiFeatureDaggerApplication<Any>() {
            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }

        }
        app should beInstanceOf(HasActivityInjector::class)
    }

    should("have a support fragment injector") {
        val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {

        }
        val app = object : MultiFeatureDaggerApplication<Any>() {
            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }

        }
        app should beInstanceOf(HasSupportFragmentInjector::class)
    }

    should("inject dependencies on the first time the application is created") {
        val app = object : MultiFeatureDaggerApplication<Any>() {
            val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {
                on { inject(any()) } doAnswer { setInjected() }
            }

            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }
        }
        app.onCreate()

        then(app.injector).should().inject(app)
    }

    should("not try to inject dependencies on the second time on create is called") {
        val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {

        }
        val app = object : MultiFeatureDaggerApplication<Any>() {
            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }

        }
        given { injector.inject(app) }.will { app.setInjected() }
        app.onCreate()
        app.onCreate()

        then(injector).should().inject(app)
    }

    should("throw an illegal state exception if it cannot inject") {
        val injector = mock<AndroidInjector<MultiFeatureDaggerApplication<Any>>> {

        }
        val app = object : MultiFeatureDaggerApplication<Any>() {
            override fun applicationInjector(): AndroidInjector<MultiFeatureDaggerApplication<Any>> {
                return injector
            }

        }
        val exception = shouldThrow<IllegalStateException> {
            app.onCreate()
        }

        exception.message shouldBe "Application context couldn't be injected"

        then(injector).should().inject(app)
    }
})
