package com.psa.kblog.users.register

import com.psa.kblog.gateways.UsersGateway
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleSource

class Register(private val usersGateway: UsersGateway) : RegisterInput {
    override fun execute(request: RegisterRequest, output: RegisterOutput) {
        @Suppress("UNUSED_VARIABLE")
        val d = usersGateway.findLoggedIn()
                .flatMap { Single.error<RegisterResponse>(RegistrationWithSessionStarted()) }
                .onErrorResumeNext { validateRegistration(it, request) }
                .subscribe({ response -> output.generateViewModel(response) },
                        { handleException(it, output) })
    }

    private fun validateRegistration(throwable: Throwable,
                                     request: RegisterRequest): SingleSource<out RegisterResponse> =
            if (throwable is RegistrationWithSessionStarted)
                Single.error<RegisterResponse>(throwable)
            else
                validateId(request)

    private fun validateId(request: RegisterRequest):
            Single<RegisterResponse> =
            usersGateway.findById(request.id)
                    .flatMap { Single.error<RegisterResponse>(DuplicatedUser()) }
                    .onErrorResumeNext { failOrCheckPassword(it, request) }

    private fun failOrCheckPassword(throwable: Throwable,
                                    request: RegisterRequest):
            SingleSource<out RegisterResponse> =
            if (throwable is DuplicatedUser)
                Single.error<RegisterResponse>(throwable)
            else
                validatePassword(request)

    private fun validatePassword(request: RegisterRequest):
            Single<RegisterResponse> =
            Completable.fromAction { checkPasswordsMatch(request) }
                    .andThen(register(request))
                    .andThen(Single.just(RegisterResponse()))
                    .onErrorResumeNext { wrapException(it) }

    private fun checkPasswordsMatch(request: RegisterRequest) {
        if (request.password != request.confirmPassword)
            throw PasswordsDoNotMatch()
    }

    private fun register(request: RegisterRequest): Completable =
            Completable.defer {
                usersGateway.insert(request.id,
                        request.firstName,
                        request.lastName,
                        request.password)
            }

    private fun wrapException(throwable: Throwable):
            SingleSource<out RegisterResponse> =
            if (throwable is PasswordsDoNotMatch)
                Single.error<RegisterResponse>(throwable)
            else
                Single.error<RegisterResponse>(RegistrationFailed(throwable))

    private fun handleException(throwable: Throwable,
                                output: RegisterOutput) {
        when (throwable) {
            is RegistrationWithSessionStarted -> output.generateViewModel(throwable)
            is DuplicatedUser -> output.generateViewModel(throwable)
            is PasswordsDoNotMatch -> output.generateViewModel(throwable)
            else -> output.generateViewModel(throwable as RegistrationFailed)
        }
    }
}