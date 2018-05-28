package com.psa.kblog.users.logout

interface LogoutInput {
    fun execute(request: LogoutRequest, output: LogoutOutput)
}