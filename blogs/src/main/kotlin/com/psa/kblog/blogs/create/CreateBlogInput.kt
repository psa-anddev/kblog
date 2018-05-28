package com.psa.kblog.blogs.create

interface CreateBlogInput {
    fun execute(request: CreateBlogRequest, output: CreateBlogOutput)
}