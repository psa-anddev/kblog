package com.psa.kblog.gateways

import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import io.reactivex.Observable
import io.reactivex.Single

interface BlogsGateway {
    fun findByUser(user: User): Observable<Blog>
    fun insert(title: String, text: String, user: User): Single<Blog>
}