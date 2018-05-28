package com.psa.kblog.users.register

class RegisterResponse
class RegistrationWithSessionStarted: Throwable()
class DuplicatedUser: Throwable()
class PasswordsDoNotMatch: Throwable()
class RegistrationFailed(cause: Throwable): Throwable(cause)