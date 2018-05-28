package com.psa.kblog.gateways

import com.psa.kblog.entities.User
import io.reactivex.Single

interface UsersGateway {
    fun findLoggedIn(): Single<User>
}