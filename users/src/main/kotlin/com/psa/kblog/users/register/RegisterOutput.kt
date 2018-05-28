package com.psa.kblog.users.register

interface RegisterOutput {
    fun generateViewModel(response: RegisterResponse)
    fun generateViewModel(error: RegistrationWithSessionStarted)
    fun generateViewModel(error: DuplicatedUser)
    fun generateViewModel(error: PasswordsDoNotMatch)
    fun generateViewModel(error: RegistrationFailed)
}