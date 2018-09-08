package com.psa.kblog.blogs.list

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kblog.blogs.R
import kotlinx.android.synthetic.main.blogs_item.view.*
import kotlin.collections.List

class BlogsAdapter : RecyclerView.Adapter<BlogViewHolder>() {
    private val blogs: SortedList<RenderedBlog> by
    lazy { SortedList(RenderedBlog::class.java, ListCallback(this)) }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    type: Int): BlogViewHolder =
            BlogViewHolder(
                    itemView = inflateViewForType(parent, type),
                    type = type)

    private fun inflateViewForType(parent: ViewGroup,
                                   type: Int) =
            LayoutInflater.from(parent.context)
                    .inflate(getLayoutForType(type),
                            parent,
                            false)

    private fun getLayoutForType(type: Int): Int =
            if (type == 0)
                R.layout.blogs_empty
            else
                R.layout.blogs_item

    override fun getItemCount(): Int {
        return if (blogs.size() == 0)
            1
        else
            blogs.size()
    }

    override fun onBindViewHolder(viewHolder: BlogViewHolder,
                                  position: Int) {
        if (position < blogs.size())
            viewHolder.bind(blogs.get(position))
    }

    override fun getItemViewType(position: Int): Int =
            if (blogs.size() == 0)
                0
            else
                1

    fun add(blogs: List<RenderedBlog>) {
        this.blogs.addAll(blogs)
    }

    internal fun clear() {
        blogs.clear()
    }
}

sealed class BlogViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    class EmptyBlogViewHolder(itemView: View) :
            BlogViewHolder(itemView) {
        override fun bind(item: RenderedBlog) {
        }
    }

    class ItemBlogViewHolder(itemView: View) :
            BlogViewHolder(itemView) {
        override fun bind(item: RenderedBlog) {
            itemView.apply {
                title.text = item.title
                text.text = item.text
            }
        }
    }

    companion object {
        operator fun invoke(itemView: View,
                            type: Int): BlogViewHolder =
                if (type == 0)
                    EmptyBlogViewHolder(itemView)
                else
                    ItemBlogViewHolder(itemView)
    }

    abstract fun bind(item: RenderedBlog)
}