package com.psa.kblog.blogs.list

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import com.psa.kblog.blogs.list.BlogViewHolder.*
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import kotlinx.android.synthetic.main.blogs_item.view.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BlogsAdapterTest {
    private val adapter = BlogsAdapter()
    private val parent by lazy {
        val activity = Robolectric.setupActivity(Activity::class.java)
        ConstraintLayout(activity)
    }

    @Test
    fun `should be an adapter`() {
        adapter should beInstanceOf(RecyclerView.Adapter::class)
    }

    @Test
    fun `should return 1 item if there are no items in the adapter`() {
        adapter.itemCount shouldBe 1
    }

    @Test
    fun `should return 1 item if there is an item`() {
        adapter.add(listOf(RenderedBlog("Test", "Text")))
        adapter.itemCount shouldBe 1
    }

    @Test
    fun `should return 2 if there are two elements`() {
        adapter.add(
                listOf(RenderedBlog("Test", "Text"),
                        RenderedBlog("Example", "This")))
        adapter.itemCount shouldBe 2
    }

    @Test
    fun `should return 4 if there are 4 elements`() {
        adapter.add(
                listOf(RenderedBlog("Test", "Text"),
                        RenderedBlog("Example", "This"),
                        RenderedBlog("Third", "Text"),
                        RenderedBlog("Fourth", "One")))
        adapter.itemCount shouldBe 4
    }

    @Test
    fun `should return item type 0 if there are no blogs`() {
        adapter.getItemViewType(0) shouldBe 0
    }

    @Test
    fun `should return 1 if there is at least one blog`() {
        forAll(Gen.list(RenderedBlogGenerator())) {
            adapter.clear()
            adapter.add(it)
            (0 until it.size).forEach { pos ->
                adapter.getItemViewType(pos) shouldBe 1
            }
            true
        }
    }

    @Test
    fun `should return an empty view holder for item type 0`() {
        adapter.onCreateViewHolder(parent, 0) should
                beInstanceOf(EmptyBlogViewHolder::class)
    }

    @Test
    fun `should return a blog view holder when the adapter has blogs`() {
        adapter.add(listOf(RenderedBlog(title = "Blog 1", text = "Some text")))

        adapter.onCreateViewHolder(parent, 1) should
                beInstanceOf(ItemBlogViewHolder::class)
    }

    @Test
    fun `should bind an empty view holder`() {
        val viewHolder =
                adapter.onCreateViewHolder(parent, 0)

        adapter.onBindViewHolder(viewHolder, 0)
    }

    @Test
    fun `should bind a item view holder`() {
        val viewHolder = adapter.onCreateViewHolder(parent, 1)
        adapter.add(listOf(
                RenderedBlog(title = "Text", text = "Some text"),
                RenderedBlog(title = "Something", text = "It's time for some other text")))

        adapter.bindViewHolder(viewHolder, 1)
        viewHolder.itemView.apply {
            title.text shouldBe "Text"
            text.text shouldBe "Some text"
        }
        adapter.bindViewHolder(viewHolder, 0)
        viewHolder.itemView.apply {
            title.text shouldBe "Something"
            text.text shouldBe "It's time for some other text"
        }

    }
}

private class RenderedBlogGenerator : Gen<RenderedBlog> {
    private val stringGen by lazy { Gen.string() }

    override fun constants(): Iterable<RenderedBlog> =
            emptyList()

    override fun random(): Sequence<RenderedBlog> =
            generateSequence {
                RenderedBlog(title = stringGen.random().first(),
                        text = stringGen.random().first())
            }
}