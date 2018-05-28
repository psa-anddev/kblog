package com.psa.kblog.users.register

interface RegisterInput {
    fun execute(request: RegisterRequest, output: RegisterOutput)
}