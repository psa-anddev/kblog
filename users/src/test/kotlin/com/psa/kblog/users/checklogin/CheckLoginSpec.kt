package com.psa.kblog.users.checklogin

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Single

class CheckLoginSpec: BehaviorSpec({
    val usersTable = table(headers("user"),
            row(User(id = "test", firstName = "Test", lastName = "User")),
            row(User(id = "root", firstName = "Admin", lastName = "System")),
            row(User(id = "bruce.banner", firstName = "Bruce", lastName = "Banner")))

    forAll(usersTable) {user ->
        Given("A user is logged in as ${user.firstName} ${user.lastName}") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn Single.just(user)
            }
            val output = mock<CheckLoginOutput> { }
            val interactor: CheckLoginInput = CheckLogin(usersGateway)

            When("I check the log in") {
                interactor.execute(CheckLoginRequest(), output)

                Then("I get the logged in user") {
                    then(usersGateway).should().findLoggedIn()
                    then(output).should().generateViewModel(CheckLoginResponse(user))
                }
            }
        }
    }

    Given("no user is logged in") {
        val userNotFound = UserNotFound("No user logged in", Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn
                    Single.error<User>(userNotFound)
        }
        val output = mock<CheckLoginOutput> {  }
        val interactor: CheckLoginInput = CheckLogin(usersGateway)

        When("I check for the logged in user") {
            interactor.execute(CheckLoginRequest(), output)

            Then("I get a no user logged in exception") {
                then(usersGateway).should().findLoggedIn()
                then(output).should().generateViewModel(
                        argThat<UserNotLoggedIn> { cause == userNotFound })
            }
        }
    }
})
