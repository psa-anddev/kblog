package com.psa.kblog.users.login

interface LoginOutput {
    fun generateViewModel(error: UserAlreadyLoggedIn)
    fun generateViewModel(error: LoginFailed)
    fun generateViewModel(response: LoginResponse)
}