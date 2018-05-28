package com.psa.kblog.blogs.create

import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.User
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Single

class CreateBlog(private val usersGateway: UsersGateway,
                 private val blogsGateway: BlogsGateway) : CreateBlogInput {
    override fun execute(request: CreateBlogRequest, output: CreateBlogOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = checkSession()
                .flatMap { insertBlog(request, it) }
                .map { CreateBlogResponse(it) }
                .subscribe({ output.generateViewModel(it) },
                        { handleError(it, output) })
    }

    private fun checkSession() =
            usersGateway.findLoggedIn()
                    .onErrorResumeNext { Single.error(SessionNotStarted(it)) }

    private fun insertBlog(request: CreateBlogRequest,
                           user: User) =
            blogsGateway.insert(request.title, request.text, user)
                    .onErrorResumeNext { Single.error(BlogCreationFailed(it)) }

    private fun handleError(throwable: Throwable,
                            output: CreateBlogOutput) {
        if (throwable is SessionNotStarted)
            output.generateViewModel(throwable)
        else
            output.generateViewModel(throwable as BlogCreationFailed)
    }
}