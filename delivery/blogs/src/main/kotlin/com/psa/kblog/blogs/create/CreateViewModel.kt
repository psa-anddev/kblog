package com.psa.kblog.blogs.create

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kblog.blogs.R
import com.psa.kblog.blogs.SessionNotStarted
import javax.inject.Inject
import javax.inject.Provider

class CreateViewModel @Inject constructor(
        private val createBlogProvider: Provider<CreateBlogInput>)
    : ViewModel(), CreateBlogOutput {

    val created: LiveData<Boolean> = MutableLiveData()
    val logOut: LiveData<Boolean> = MutableLiveData()
    val errorMessage: LiveData<Int> = MutableLiveData()

    fun createBlog(title: String, text: String) {
        createBlogProvider.get()
                .execute(CreateBlogRequest(title, text), this)
    }

    override fun generateViewModel(response: CreateBlogResponse) {
        (created as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: SessionNotStarted) {
        (logOut as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: BlogCreationFailed) {
        (errorMessage as MutableLiveData).postValue(R.string.blogCreationFailed)
    }
}