package com.psa.kblog.users.register

data class RegisterRequest(val id: String,
                           val firstName: String,
                           val lastName: String,
                           val password: String,
                           val confirmPassword: String)
