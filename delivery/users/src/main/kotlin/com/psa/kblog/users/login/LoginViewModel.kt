package com.psa.kblog.users.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kblog.users.R
import javax.inject.Provider

class LoginViewModel(
        private val loginInteractorProvider: Provider<LoginInput>)
    : ViewModel(), LoginOutput {
    val success: LiveData<Boolean> = MutableLiveData()
    val error: LiveData<Int> = MutableLiveData()

    fun login(username: String, password: String) {
        loginInteractorProvider.get()
                .execute(LoginRequest(username, password), this)
    }

    override fun generateViewModel(error: UserAlreadyLoggedIn) {
        (success as MutableLiveData).postValue(false)
        (this.error as MutableLiveData).postValue(R.string.loginError)
    }

    override fun generateViewModel(error: LoginFailed) {
        (success as MutableLiveData).postValue(false)
        (this.error as MutableLiveData).postValue(R.string.wrongUserOrPassword)
    }

    override fun generateViewModel(response: LoginResponse) {
        (success as MutableLiveData).postValue(true)
    }
}