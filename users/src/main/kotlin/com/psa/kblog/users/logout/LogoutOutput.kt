package com.psa.kblog.users.logout

interface LogoutOutput {
    fun generateViewModel(response: LogoutResponse)
    fun generateViewModel(error: NoActiveSession)
    fun generateViewModel(error: LogoutFailed)
}