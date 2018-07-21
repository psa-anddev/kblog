package com.psa.kblog

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
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
}