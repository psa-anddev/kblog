package com.psa.kblog.users.splash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kblog.users.checklogin.*
import com.psa.kblog.users.splash.SessionStatus.*
import javax.inject.Inject
import javax.inject.Provider

class SplashViewModel
    @Inject constructor(private val checkLoginProvider: Provider<CheckLoginInput>)
            : ViewModel(), CheckLoginOutput {
    val sessionState: LiveData<SessionStatus> = MutableLiveData()

    fun checkLogin() {
        checkLoginProvider.get()
                .execute(CheckLoginRequest(), this)
    }

    override fun generateViewModel(response: CheckLoginResponse) {
        (sessionState as MutableLiveData).postValue(LOGGED_IN)
    }

    override fun generateViewModel(error: UserNotLoggedIn) {
        (sessionState as MutableLiveData).postValue(UNKNOWN_USER)
    }
}

enum class SessionStatus {
    LOGGED_IN, UNKNOWN_USER
}