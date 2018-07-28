package com.psa.kblog.users.splash

import android.support.v4.app.Fragment
import com.psa.kblog.KBlog
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(application = KBlog::class)
class SplashFragmentTest {
    @Test
    fun `should be a fragment`() {
        SplashFragment() should beInstanceOf(Fragment::class)
    }

    @Test
    fun `should inject dependencies`() {
        val fragment = SplashFragment()
        startFragment(fragment)
    }
}
