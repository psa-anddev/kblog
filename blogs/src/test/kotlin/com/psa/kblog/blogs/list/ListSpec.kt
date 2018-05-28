package com.psa.kblog.blogs.list

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.BlogFindingFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.Observable
import io.reactivex.Single

class ListSpec: BehaviorSpec({
    Given("I am not logged in") {
        val throwable = UserNotFound("Not logged in", Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn
                    Single.error<User>(throwable)
        }
        val blogsGateway = mock<BlogsGateway> {  }
        val output = mock<ListOutput> {  }
        val interactor: ListInput = List(usersGateway, blogsGateway)

        When("I list the blogs") {
            interactor.execute(ListRequest(), output)

            Then("I get a no active session error") {
                then(usersGateway).should().findLoggedIn()
                then(output).should()
                        .generateViewModel(argThat<SessionNotStarted> { cause == throwable})
            }
        }
    }

    Given("I am logged in") {
        val user = User("test", "Test", "User")
        val throwable = BlogFindingFailed(Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn Single.just(user)
        }
        val blogsGateway = mock<BlogsGateway> {
            on { findByUser(user) } doReturn Observable.error<Blog>(throwable)
        }
        val output = mock<ListOutput> {  }
        val interactor: ListInput = List(usersGateway, blogsGateway)

        When("requesting the blogs fails") {
            interactor.execute(ListRequest(), output)

            Then("I am notified about it") {
                then(usersGateway).should().findLoggedIn()
                then(blogsGateway).should().findByUser(user)
                then(output).should()
                        .generateViewModel(argThat<BlogListingFailed> { cause == throwable  })
            }
        }
    }

    Given("I have a session started") {
        val user = User(id = "test", firstName = "Test", lastName = "User")
        val blogs = listOf(
                Blog(id = 1, title = "Hey", text = "Hello World"),
                Blog(id = 5, title = "The power", text = "Let's discuss"))
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn Single.just(user) }
        val blogsGateway = mock<BlogsGateway> {
            on { findByUser(user) } doReturn Observable.fromIterable(blogs)
        }
        val output = mock<ListOutput> { }
        val interactor: ListInput = List(usersGateway, blogsGateway)

        When("I list the blogs") {
            interactor.execute(ListRequest(), output)

            Then("I get all my blogs") {
                then(usersGateway).should().findLoggedIn()
                then(blogsGateway).should().findByUser(user)
                then(output).should().generateViewModel(ListResponse(blogs))
            }
        }
    }
})
