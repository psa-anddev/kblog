package com.psa.kblog.blogs.list

import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Observable
import io.reactivex.Single

class List(private val usersGateway: UsersGateway,
           private val blogsGateway: BlogsGateway) : ListInput {
    override fun execute(request: ListRequest, output: ListOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = checkSession()
                .flatMapObservable { findBlogs(it) }
                .toList()
                .map { ListResponse(it) }
                .subscribe({ output.generateViewModel(it) },
                        { handleError(it, output) })
    }

    private fun checkSession() =
            usersGateway.findLoggedIn()
                    .onErrorResumeNext { Single.error(SessionNotStarted(it)) }

    private fun findBlogs(user: User): Observable<Blog> =
            blogsGateway.findByUser(user)
                    .onErrorResumeNext { throwable: Throwable ->
                        Observable.error<Blog>(BlogListingFailed(throwable))
                    }

    private fun handleError(throwable: Throwable,
                            output: ListOutput) {
        if (throwable is SessionNotStarted)
            output.generateViewModel(throwable)
        else
            output.generateViewModel(throwable as BlogListingFailed)
    }
}