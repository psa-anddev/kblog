package com.psa.kblog.users.login

interface LoginInput {
    fun execute(request: LoginRequest, output: LoginOutput)
}