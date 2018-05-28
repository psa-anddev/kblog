package com.psa.kblog.gateways

import com.psa.kblog.entities.User
import io.reactivex.Completable
import io.reactivex.Single

interface UsersGateway {
    fun findLoggedIn(): Single<User>
    fun login(id:String, password: String): Completable
}