package com.psa.kblog

import dagger.android.support.HasSupportFragmentInjector
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldNotBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class KBlogTest {
    @Test
    fun `should be the application context`() {
        RuntimeEnvironment.application should
                beInstanceOf(KBlog::class)
    }

    @Test
    fun `should be able to inject support fragments`() {
        RuntimeEnvironment.application should beInstanceOf(HasSupportFragmentInjector::class)
    }

    @Test
    fun `should provide a support fragment injector`() {
        val context = RuntimeEnvironment.application as KBlog

        context.supportFragmentInjector() shouldNotBe null
    }
}