package com.psa.kblog.blogs.create

import com.psa.kblog.entities.Blog

data class CreateBlogResponse(val blog: Blog)
class BlogCreationFailed(cause: Throwable): Throwable(cause)