package com.psa.kblog.blogs.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kblog.blogs.R
import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.users.logout.*
import javax.inject.Inject
import kotlin.collections.List
import javax.inject.Provider

class ListViewModel @Inject constructor(
        private val listBlogsProvider: Provider<ListInput>,
        private val logoutProvider: Provider<LogoutInput>):
        ViewModel(), ListOutput, LogoutOutput {
    val blogs: LiveData<List<RenderedBlog>> = MutableLiveData()
    val logout: LiveData<Boolean> = MutableLiveData()
    val errorMessage: LiveData<Int> = MutableLiveData()

    fun list() {
        listBlogsProvider.get()
                .execute(ListRequest(), this)
    }

    override fun generateViewModel(response: ListResponse) {
        (blogs as MutableLiveData)
                .postValue(response.blogs
                        .map { RenderedBlog(it.title, it.text) })
    }

    override fun generateViewModel(error: SessionNotStarted) {
        (logout as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: BlogListingFailed) {
        (errorMessage as MutableLiveData).postValue(R.string.blogsFailedToLoad)
    }

    fun logOut() {
        logoutProvider.get().execute(LogoutRequest(), this)
    }

    override fun generateViewModel(response: LogoutResponse) {
        (logout as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: NoActiveSession) {
        (logout as MutableLiveData).postValue(true)
    }

    override fun generateViewModel(error: LogoutFailed) {
        (errorMessage as MutableLiveData)
                .postValue(R.string.logOutFailed)
    }
}

data class RenderedBlog(val title: String, val text: String)