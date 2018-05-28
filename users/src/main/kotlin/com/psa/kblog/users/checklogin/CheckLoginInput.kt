package com.psa.kblog.users.checklogin

interface CheckLoginInput {
    fun execute(request: CheckLoginRequest, output: CheckLoginOutput)
}