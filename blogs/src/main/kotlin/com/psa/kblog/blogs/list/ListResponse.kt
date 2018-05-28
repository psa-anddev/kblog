package com.psa.kblog.blogs.list

import com.psa.kblog.entities.Blog
import kotlin.collections.List

class BlogListingFailed(cause: Throwable): Throwable(cause)
data class ListResponse(val blogs: List<Blog>)