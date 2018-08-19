package com.psa.kblog.blogs.list

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback

class ListCallback(adapter: RecyclerView.Adapter<*>)
    : SortedListAdapterCallback<RenderedBlog>(adapter) {
    override fun areItemsTheSame(p0: RenderedBlog?,
                                 p1: RenderedBlog?): Boolean =
            p0 != null && p1 != null && p0.title == p1.title

    override fun compare(p0: RenderedBlog?,
                         p1: RenderedBlog?): Int =
            when {
                p0 == null && p1 == null -> 0
                p0 == null -> -1
                p1 == null -> 1
                else -> p0.title.compareTo(p1.title)
            }

    override fun areContentsTheSame(p0: RenderedBlog?,
                                    p1: RenderedBlog?)
            : Boolean =
            (p0 == null && p1 == null) || p0 == p1
}