package com.psa.kblog.blogs.create

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.blogs.SessionNotStarted
import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.BlogInsertionFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Single

class CreateSpec : BehaviorSpec({
    Given("I am not logged in") {
        val throwable = UserNotFound("Not logged in", Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn Single.error<User>(throwable)
        }
        val blogsGateway = mock<BlogsGateway> { }
        val output = mock<CreateBlogOutput> { }
        val interactor: CreateBlogInput = CreateBlog(usersGateway, blogsGateway)
        When("I try to insert a blog") {
            interactor.execute(CreateBlogRequest(title = "Test", text = "Some text"), output)

            Then("I get a session not started error") {
                then(usersGateway).should().findLoggedIn()
                then(output).should()
                        .generateViewModel(argThat<SessionNotStarted> { cause == throwable })
            }
        }
    }

    val requests = table(headers("request", "user"),
            row(CreateBlogRequest(title = "Test",
                    text = "Some text"),
                    User("diana.prince",
                            "Diana",
                            "Prince")),
            row(CreateBlogRequest(title = "New developments",
                    text = "The new developments in this"),
                    User(id = "clark.kent",
                            firstName = "Clark",
                            lastName = "Kent")))

    forAll(requests) { request, user ->
        Given("I am logged in as ${user.firstName} ${user.lastName}") {
            val throwable = BlogInsertionFailed(Throwable())
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn Single.just(user)
            }
            val blogsGateway = mock<BlogsGateway> {
                on { insert(request.title, request.text, user) } doReturn
                        Single.error<Blog>(throwable)
            }
            val output = mock<CreateBlogOutput> {  }
            val interactor: CreateBlogInput = CreateBlog(usersGateway, blogsGateway)

            When("the blog insertion fails") {
                interactor.execute(request, output)

                Then("I get a blog creation failed error") {
                    then(usersGateway).should().findLoggedIn()
                    then(blogsGateway).should().insert(request.title, request.text, user)
                    then(output).should()
                            .generateViewModel(argThat<BlogCreationFailed> { cause == throwable })
                }
            }
        }

        Given("I am in a session as ${user.firstName} ${user.lastName}") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn Single.just(user)
            }
            val blog = Blog(id = 10, title = request.title, text = request.text)
            val blogsGateway = mock<BlogsGateway> {
                on { insert(request.title, request.text, user) } doReturn
                        Single.just(blog)
            }
            val output = mock<CreateBlogOutput> {  }
            val interactor: CreateBlogInput = CreateBlog(usersGateway, blogsGateway)

            When("the blog creation succeeds") {
                interactor.execute(request, output)

                Then("I get the created blog") {
                    then(usersGateway).should().findLoggedIn()
                    then(blogsGateway).should().insert(request.title, request.text, user)
                    then(output).should().generateViewModel(CreateBlogResponse(blog))
                }
            }
        }
    }
})
