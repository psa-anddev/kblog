package com.psa.kblog.users.registration

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kblog.users.R
import com.psa.kblog.users.register.*
import javax.inject.Inject
import javax.inject.Provider

class RegistrationViewModel
@Inject constructor(private val registerProvider: Provider<RegisterInput>):
ViewModel(), RegisterOutput {
    val registered: LiveData<Boolean> = MutableLiveData()
    val error: LiveData<Int> = MutableLiveData()
    val idHint: LiveData<Int> = MutableLiveData()
    val confirmPasswordHint: LiveData<Int> = MutableLiveData()

    fun register(id: String,
                 firstName: String,
                 lastName: String,
                 password: String,
                 confirmPassword: String) {
        registerProvider.get()
                .execute(RegisterRequest(
                        id,
                        firstName,
                        lastName,
                        password,
                        confirmPassword),
                this)
    }

    override fun generateViewModel(response: RegisterResponse) {
        (registered as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: RegistrationWithSessionStarted) {
        (this.error as MutableLiveData).postValue(R.string.sessionIsStarted)
    }

    override fun generateViewModel(error: DuplicatedUser) {
        (idHint as MutableLiveData).postValue(R.string.userAlreadyExists)
    }

    override fun generateViewModel(error: PasswordsDoNotMatch) {
        (confirmPasswordHint as MutableLiveData).postValue(R.string.passwordsDoNotMatch)
    }

    override fun generateViewModel(error: RegistrationFailed) {
        (this.error as MutableLiveData).postValue(R.string.registrationError)
    }
}
