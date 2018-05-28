package com.psa.kblog.users.login

import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Single
import io.reactivex.SingleSource

class Login(private val usersGateway: UsersGateway) : LoginInput {
    override fun execute(request: LoginRequest, output: LoginOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = usersGateway.findLoggedIn()
                .flatMap { Single.error<LoginResponse>(UserAlreadyLoggedIn()) }
                .onErrorResumeNext { loginIfPossible(it, request) }
                .subscribe({ response -> output.generateViewModel(response) },
                        { deliverError(it, output) })
    }

    private fun loginIfPossible(throwable: Throwable, request: LoginRequest):
            SingleSource<out LoginResponse> =
            if (isLoggedIn(throwable))
                Single.error<LoginResponse>(throwable)
            else
                logIn(request)

    private fun isLoggedIn(throwable: Throwable) = throwable is UserAlreadyLoggedIn

    private fun logIn(request: LoginRequest): Single<LoginResponse> =
            usersGateway.login(request.id, request.password)
                    .andThen(Single.just(LoginResponse()))
                    .onErrorResumeNext { Single.error(LoginFailed(it)) }

    private fun deliverError(error: Throwable?, output: LoginOutput) {
        if (error is UserAlreadyLoggedIn)
            output.generateViewModel(error)
        else
            output.generateViewModel(error as LoginFailed)
    }
}