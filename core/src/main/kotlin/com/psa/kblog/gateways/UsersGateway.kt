package com.psa.kblog.gateways

import com.psa.kblog.entities.User
import io.reactivex.Completable
import io.reactivex.Single

interface UsersGateway {
    fun findLoggedIn(): Single<User>
    fun login(id:String, password: String): Completable
    fun findById(id: String): Single<User>
    fun insert(id: String, firstName: String, lastName: String, password: String): Completable
    fun logout(): Completable
}