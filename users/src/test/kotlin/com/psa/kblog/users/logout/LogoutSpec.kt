package com.psa.kblog.users.logout

import com.nhaarman.mockito_kotlin.*
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.SessionClosingFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.Completable
import io.reactivex.Single

class LogoutSpec: BehaviorSpec({
    Given("I am not logged in") {
        val throwable =
                UserNotFound("not logged in", Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn
                    Single.error<User>(throwable)
        }
        val output = mock<LogoutOutput> {  }
        val interactor: LogoutInput = Logout(usersGateway)

        When("I log out") {
            interactor.execute(LogoutRequest(), output)

            Then("I get a session not started error") {
                then(usersGateway).should().findLoggedIn()
                then(output).should().generateViewModel(argThat<NoActiveSession>{
                    cause == throwable
                })
            }
        }
    }

    Given("I am logged in") {
        val throwable = SessionClosingFailed(Throwable())
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn
                    Single.just(User(id = "test", firstName = "Test", lastName = "User"))
            on { logout() } doReturn Completable.error(throwable)
        }
        val output = mock<LogoutOutput> {  }
        val interactor: LogoutInput = Logout(usersGateway)

        When("logging out fails") {
            interactor.execute(LogoutRequest(), output)

            Then("I get a log out failed error") {
                then(usersGateway).should().findLoggedIn()
                then(usersGateway).should().logout()
                then(output).should()
                        .generateViewModel(argThat<LogoutFailed> { cause == throwable })
            }
        }
    }

    Given("I have an active session") {
        val usersGateway = mock<UsersGateway> {
            on { findLoggedIn() } doReturn
                    Single.just(User("diana.prince", "Diana", "Prince"))
            on { logout() } doReturn
                    Completable.complete()
        }
        val output = mock<LogoutOutput> {  }
        val interactor: LogoutInput = Logout(usersGateway)

        When("I log out successfully") {
            interactor.execute(LogoutRequest(), output)

            Then("I get the confirmation") {
                then(usersGateway).should().findLoggedIn()
                then(usersGateway).should().logout()
                then(output).should().generateViewModel(isA<LogoutResponse>())
            }
        }
    }
})
