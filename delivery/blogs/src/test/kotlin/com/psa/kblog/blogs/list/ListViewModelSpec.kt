package com.psa.kblog.blogs.list

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.blogs.R
import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.Blog
import com.psa.kblog.users.logout.LogoutFailed
import com.psa.kblog.users.logout.LogoutInput
import com.psa.kblog.users.logout.LogoutResponse
import com.psa.kblog.users.logout.NoActiveSession
import com.psa.kblog.utils.listeners.InstantTaskExecutorListener
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider
import kotlin.collections.List

class ListViewModelSpec : ShouldSpec({
    should("be a view model") {
        ListViewModel(Provider { mock<ListInput> { } },
                Provider { mock<LogoutInput> { } }) should
                beInstanceOf(ViewModel::class)
    }

    should("execute the list blogs use case") {
        val interactor = mock<ListInput> { }
        val viewModel = ListViewModel(Provider { interactor },
                Provider { mock<LogoutInput> { } })

        viewModel.list()

        then(interactor).should().execute(isA(), eq(viewModel))
    }

    should("deliver the list of blogs") {
        val viewModel =
                ListViewModel(Provider { mock<ListInput> { } },
                        Provider { mock<LogoutInput> { } })

        viewModel.generateViewModel(
                ListResponse(listOf(
                        Blog(1, "Test", "Text"),
                        Blog(2, "Second", "Some other text"))))

        viewModel.blogs.value shouldBe listOf(
                RenderedBlog("Test", "Text"),
                RenderedBlog("Second", "Some other text"))
    }

    should("deliver another list of blogs") {
        val viewModel =
                ListViewModel(Provider { mock<ListInput> { } },
                        Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(
                ListResponse(listOf(
                        Blog(5, "Android Navigation", "Android Navigation is"),
                        Blog(9, "The problem with action movies",
                                "Why are action movies less appealing?"),
                        Blog(10, "Lol", "Funny"))))

        viewModel.blogs.value shouldBe listOf(
                RenderedBlog("Android Navigation", "Android Navigation is"),
                RenderedBlog("The problem with action movies", "Why are action movies less appealing?"),
                RenderedBlog("Lol", "Funny"))
    }

    should("communicate the view that the user is not logged in") {
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(SessionNotStarted(Throwable()))

        viewModel.logout.value shouldBe true
    }

    should("return an error message if the blogs fail to load") {
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(BlogListingFailed(Throwable()))

        viewModel.errorMessage.value shouldBe R.string.blogsFailedToLoad
    }

    should("log the user out") {
        val logOutInteractor = mock<LogoutInput> { }
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { logOutInteractor })

        viewModel.logOut()

        then(logOutInteractor).should()
                .execute(isA(), eq(viewModel))
    }

    should("notify the user is logged out") {
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(LogoutResponse())

        viewModel.logout.value shouldBe true
    }

    should("notify the user should be logged out if session is non-existence") {
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(NoActiveSession(Throwable()))

        viewModel.logout.value shouldBe true
    }

    should("notify log out error") {
        val viewModel = ListViewModel(Provider { mock<ListInput> {  } },
                Provider { mock<LogoutInput> {  } })

        viewModel.generateViewModel(LogoutFailed(Throwable()))

        viewModel.errorMessage.value shouldBe R.string.logOutFailed
    }
}) {
    override fun listeners(): List<TestListener> =
            super.listeners() + InstantTaskExecutorListener()
}
