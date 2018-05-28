package com.psa.kblog.users.login

import com.nhaarman.mockito_kotlin.*
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Completable
import io.reactivex.Single

class LoginSpec: BehaviorSpec({
    val userLoggedInValues = table(headers("id", "password", "user"),
            row("test",
                    "pass",
                    User(id = "diana.prince", firstName = "Diana", lastName = "Prince")),
            row("root", "1234",
                    User(id = "bruce.banner", firstName = "Bruce", lastName = "Banner")))

    forAll(userLoggedInValues) { id: String, pass: String, user: User ->
        Given("I am already logged in as ${user.firstName} ${user.lastName}") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn Single.just(user)
            }
            val output = mock<LoginOutput> {  }
            val interactor: LoginInput = Login(usersGateway)

            When("I try to log in with another user") {
                interactor.execute(LoginRequest(id, pass), output)

                Then("I should get an error") {
                    then(usersGateway).should().findLoggedIn()
                    then(output).should().generateViewModel(isA<UserAlreadyLoggedIn>())
                }
            }
        }
    }

    val loginValues = table(headers("id", "password"),
            row("test", "1234"),
            row("root", "admin"))

    forAll(loginValues) { id: String, password: String ->
        Given("I try to log in with id $id and password $password") {
            val throwable = UserNotFound("Wrong details", Throwable())
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("user not logged in", Throwable()))
                on { login(id, password) } doReturn Completable.error(throwable)
            }
            val output = mock<LoginOutput> {  }
            val interactor: LoginInput = Login(usersGateway)

            When("it fails") {
                interactor.execute(LoginRequest(id, password), output)

                Then("I get a login failed error") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().login(id, password)
                    then(output).should().generateViewModel(argThat<LoginFailed> { cause == throwable })
                }
            }
        }
    }

    forAll(loginValues) { id: String, password: String ->
        Given("I log in with id $id and password $password") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("not logged in", Throwable()))
                on { login(id, password) } doReturn Completable.complete()
            }
            val output = mock<LoginOutput> {  }
            val interactor: LoginInput = Login(usersGateway)

            When("it succeeds") {
                interactor.execute(LoginRequest(id, password), output)

                Then("I get a confirmation") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().login(id, password)
                    then(output).should().generateViewModel(isA<LoginResponse>())
                }
            }
        }
    }
})
