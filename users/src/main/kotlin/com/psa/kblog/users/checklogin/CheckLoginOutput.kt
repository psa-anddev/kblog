package com.psa.kblog.users.checklogin

interface CheckLoginOutput {
    fun generateViewModel(response: CheckLoginResponse)
    fun generateViewModel(error: UserNotLoggedIn)
}