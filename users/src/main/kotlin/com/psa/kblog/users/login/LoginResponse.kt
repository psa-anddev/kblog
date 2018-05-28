package com.psa.kblog.users.login

class LoginResponse


class UserAlreadyLoggedIn: Throwable()

class LoginFailed(cause: Throwable): Throwable(cause)