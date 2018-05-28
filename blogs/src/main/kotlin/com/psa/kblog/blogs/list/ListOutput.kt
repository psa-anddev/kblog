package com.psa.kblog.blogs.list

import com.psa.kblog.blogs.SessionNotStarted

interface ListOutput {
    fun generateViewModel(response: ListResponse)
    fun generateViewModel(error: SessionNotStarted)
    fun generateViewModel(error: BlogListingFailed)
}