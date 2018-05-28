package com.psa.kblog.blogs.create

import com.psa.kblog.blogs.SessionNotStarted

interface CreateBlogOutput {
    fun generateViewModel(response: CreateBlogResponse)
    fun generateViewModel(error: SessionNotStarted)
    fun generateViewModel(error: BlogCreationFailed)
}