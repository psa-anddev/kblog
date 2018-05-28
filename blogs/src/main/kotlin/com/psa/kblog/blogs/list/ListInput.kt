package com.psa.kblog.blogs.list

interface ListInput {
    fun execute(request: ListRequest, output: ListOutput)
}