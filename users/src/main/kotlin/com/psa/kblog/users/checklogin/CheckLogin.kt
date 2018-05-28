package com.psa.kblog.users.checklogin

import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Single

class CheckLogin(private val usersGateway: UsersGateway): CheckLoginInput {
    override fun execute(request: CheckLoginRequest, output: CheckLoginOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = usersGateway.findLoggedIn()
                .map { CheckLoginResponse(it) }
                .onErrorResumeNext { Single.error(UserNotLoggedIn(it)) }
                .subscribe({ response -> output.generateViewModel(response) },
                        { output.generateViewModel(it as UserNotLoggedIn)})
    }
}