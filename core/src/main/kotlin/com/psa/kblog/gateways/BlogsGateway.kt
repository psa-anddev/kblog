package com.psa.kblog.gateways

import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import io.reactivex.Observable

interface BlogsGateway {
    fun findByUser(user: User): Observable<Blog>
}