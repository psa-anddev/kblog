package android.support.v4.app

import android.content.Context
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.then
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec

class FragmentExtensionsSpec: ShouldSpec({
    should("attach a fragment") {
        val fragment = spy<Fragment>()
        val context = FragmentActivity()

        fragment.attach(context)
        fragment.mHost shouldNotBe null

        then(fragment).should().onAttach(context as Context)
    }
})
