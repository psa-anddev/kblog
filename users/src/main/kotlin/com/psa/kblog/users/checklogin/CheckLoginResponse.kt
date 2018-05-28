package com.psa.kblog.users.checklogin

import com.psa.kblog.entities.User

data class CheckLoginResponse(val user: User)

class UserNotLoggedIn(cause:Throwable): Throwable(cause)
