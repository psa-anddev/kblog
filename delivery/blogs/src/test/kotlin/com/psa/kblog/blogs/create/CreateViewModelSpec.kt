package com.psa.kblog.blogs.create

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.blogs.R
import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.Blog
import com.psa.kblog.utils.listeners.InstantTaskExecutorListener
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

class CreateViewModelSpec : ShouldSpec({
    should("be a view model") {
        CreateViewModel(
                Provider { mock<CreateBlogInput> {  }}) should
                beInstanceOf(ViewModel::class)
    }

    should("issue create blog request") {
        forAll { title: String, text: String ->
            val interactor = mock<CreateBlogInput> {  }
            val viewModel = CreateViewModel(Provider { interactor })

            viewModel.createBlog(title, text)

            then(interactor).should().execute(CreateBlogRequest(title, text), viewModel)
            true
        }
    }

    should("return the new blog") {
        forAll { id: Int, title: String, text: String ->
            val viewModel = CreateViewModel(Provider { mock<CreateBlogInput> { }})

            viewModel.generateViewModel(CreateBlogResponse(Blog(id, title, text)))

            viewModel.created.value shouldBe true
            true
        }
    }

    should("log the user out when session  is not started") {
        val viewModel = CreateViewModel(Provider { mock<CreateBlogInput> { }})

        viewModel.generateViewModel(SessionNotStarted(Throwable()))

        viewModel.logOut.value shouldBe true
    }

    should("communicate the creation of the blog failed") {
        val viewModel = CreateViewModel(Provider { mock<CreateBlogInput> { }})

        viewModel.generateViewModel(BlogCreationFailed(Throwable()))

        viewModel.errorMessage.value shouldBe R.string.blogCreationFailed
    }
}) {
    override fun listeners(): List<TestListener> =
            super.listeners() + InstantTaskExecutorListener()
}
