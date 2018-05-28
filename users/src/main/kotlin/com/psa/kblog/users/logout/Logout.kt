package com.psa.kblog.users.logout

import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Single

class Logout(private val usersGateway: UsersGateway) : LogoutInput {
    override fun execute(request: LogoutRequest, output: LogoutOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = verifySession()
                .flatMap { logout() }
                .subscribe({ response -> output.generateViewModel(response) },
                        { handleError(it, output) })
    }

    private fun verifySession() = usersGateway.findLoggedIn()
            .onErrorResumeNext { Single.error(NoActiveSession(it)) }

    private fun logout(): Single<LogoutResponse> =
            usersGateway.logout()
                    .andThen(Single.just(LogoutResponse()))
                    .onErrorResumeNext { Single.error(LogoutFailed(it)) }

    private fun handleError(throwable: Throwable,
                            output: LogoutOutput) {
        if (throwable is NoActiveSession)
            output.generateViewModel(throwable)
        else
            output.generateViewModel(throwable as LogoutFailed)
    }
}