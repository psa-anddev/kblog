package com.psa.kblog.blogs.list

import android.support.v7.widget.util.SortedListAdapterCallback
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ListCallbackTest {
    private val callback = ListCallback(mock {})

    @Test
    fun `should be a callback`() {
        callback should beInstanceOf(SortedListAdapterCallback::class)
    }

    @Test
    fun `should return false when null is passed as first argument to areItemsTheSame`() {
        forAll { title: String, text: String ->
            !callback.areItemsTheSame(null,
                    RenderedBlog(title, text))
        }
    }

    @Test
    fun `should return false when null is passed as second argument to areItemsTheSame`() {
        forAll { title: String, text: String ->
            !callback.areItemsTheSame(RenderedBlog(title, text),
                    null)
        }
    }

    @Test
    fun `should items not be equal if they have different titles`() {
        val values = table(headers("p0", "p1"),
                row(RenderedBlog("A", "a"),
                        RenderedBlog("B", "b")),
                row(RenderedBlog("Header", "Text"),
                        RenderedBlog("Titular", "Text")))

        forAll(values) { blog1, blog2 ->
            callback.areItemsTheSame(blog1, blog2) shouldBe false
        }
    }

    @Test
    fun `should items be equal if they have the same title`() {
        forAll { title: String, text1: String, text2: String ->
            callback.areItemsTheSame(
                    RenderedBlog(title, text1),
                    RenderedBlog(title, text2))
        }
    }

    @Test
    fun `should compare to 0 if both elements are null`() {
        callback.compare(null, null) shouldBe 0
    }

    @Test
    fun `should compare to -1 if first element is null`() {
        forAll { title: String, text: String ->
            callback.compare(null, RenderedBlog(title, text)) == -1
        }
    }

    @Test
    fun `should compare to 1 if second element is null`() {
        forAll { title: String, text: String ->
            callback.compare(RenderedBlog(title, text), null) == 1
        }
    }

    @Test
    fun `should compare the titles if none of the elements are null`() {
        forAll { title1: String, text1: String,
                 title2: String, text2: String ->
            callback.compare(RenderedBlog(title1, text1),
                    RenderedBlog(title2, text2)) ==
                    title1.compareTo(title2)
        }
    }

    @Test
    fun `should be the same contents for null elements`() {
        callback.areContentsTheSame(null, null) shouldBe true
    }

    @Test
    fun `should not be the same contents for a first null and a second not null`() {
        forAll { title: String, text: String ->
            !callback.areContentsTheSame(null,
                    RenderedBlog(title, text))
        }
    }

    @Test
    fun `should not be the same contents for a first not null and a second null element`() {
        forAll { title: String, text: String ->
            !callback.areContentsTheSame(
                    RenderedBlog(title, text), null)
        }
    }

    @Test
    fun `should follow blogs equality to determine whether the contents are the same`() {
        forAll { title1: String, text1: String,
                 title2: String, text2: String ->
            val blog1 = RenderedBlog(title1, text1)
            val blog2 = RenderedBlog(title2, text2)

            callback.areContentsTheSame(blog1, blog2) == (blog1 == blog2)
        }
    }
}