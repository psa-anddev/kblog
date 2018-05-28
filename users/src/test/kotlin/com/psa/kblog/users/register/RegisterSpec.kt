package com.psa.kblog.users.register

import com.nhaarman.mockito_kotlin.*
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.UserInsertionFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.UsersGateway
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Completable
import io.reactivex.Single

class RegisterSpec: BehaviorSpec({
    val loggedInValues = table(headers("id", "first name", "last name",
            "password", "confirm", "user"),
            row("1", "First", "User", "p", "p",
                    User(id = "test", firstName = "Test", lastName = "User")),
            row("diana.prince", "Diana", "Prince", "test", "test",
                    User(id = "peter.parker", firstName = "Peter", lastName = "Parker")))

    forAll(loggedInValues) { id: String, first: String, last: String,
                             pass: String, confirm: String, user: User ->
        Given("a session is already started as ${user.firstName} ${user.lastName}") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn Single.just(user)
            }
            val output = mock<RegisterOutput> {  }
            val interactor: RegisterInput = Register(usersGateway)

            When("I try to register") {
                interactor.execute(RegisterRequest(id, first, last, pass, confirm), output)

                Then("I get a registration error") {
                    then(usersGateway).should().findLoggedIn()
                    then(output).should().generateViewModel(isA<RegistrationWithSessionStarted>())
                }
            }
        }
    }

    val idExistingValues = table(headers("id", "first name", "last name",
            "password", "confirm", "user"),
            row("1", "Peter", "Parker", "test", "test",
                    User(id = "1", firstName = "Clark", lastName = "Kent")),
            row("test", "Diana", "Prince", "1234", "56678",
                    User(id = "Test", firstName = "Test", lastName = "User")))

    forAll(idExistingValues) { id, firstName, lastName, password, confirm, user ->
        Given("User with id $id is already registered") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("not logged in", Throwable()))
                on { findById(id) } doReturn Single.just(user)
            }
            val output = mock<RegisterOutput> {  }
            val interactor: RegisterInput = Register(usersGateway)

            When("I try to register another user with the same id") {
                interactor.execute(
                        RegisterRequest(id, firstName, lastName, password, confirm), output)

                Then("I get a user already registered error") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().findById(id)
                    then(output).should().generateViewModel(isA<DuplicatedUser>())
                }
            }
        }
    }

    val differentPassValues = table(headers("password", "confirm"),
            row("test", "tet"),
            row("a", "b"))

    forAll(differentPassValues) { password, confirm ->
        Given("$password is not equal to $confirm") {
            val id = "test"
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("not logged in", Throwable()))
                on { findById(id) } doReturn
                        Single.error<User>(UserNotFound("id does not exist", Throwable()))
            }
            val output = mock<RegisterOutput> {  }
            val interactor: RegisterInput = Register(usersGateway)

            When("I register") {
                interactor.execute(
                        RegisterRequest(id, "test", "user", password, confirm),
                        output)

                Then("I get a passwords not match error") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().findById(id)
                    then(output).should().generateViewModel(isA<PasswordsDoNotMatch>())
                }
            }
        }
    }

    val registrationValues = table(headers("id", "first name", "last name", "password"),
            row("test", "Test", "User", "test"),
            row("diana.prince", "Diana", "Prince", "wonder woman"))

    forAll(registrationValues) { id, firstName, lastName, password ->
        Given("I am registering user with id $id") {
            val throwable = UserInsertionFailed(Throwable())
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("not logged in", Throwable()))
                on { findById(id) } doReturn
                        Single.error<User>(UserNotFound("id does not exist", Throwable()))
                on { insert(id, firstName, lastName, password) } doReturn Completable.error(throwable)
            }
            val output = mock<RegisterOutput> {  }
            val interactor: RegisterInput = Register(usersGateway)

            When("the registration fails") {
                interactor.execute(
                        RegisterRequest(id, firstName, lastName, password, password), output)

                Then("I get a registration error") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().findById(id)
                    then(usersGateway).should().insert(id, firstName, lastName, password)
                    then(output).should().generateViewModel(argThat<RegistrationFailed> {
                        cause == throwable
                    })
                }
            }
        }
    }

    forAll(registrationValues) { id, firstName, lastName, password ->
        Given("I'm registering user with id $id") {
            val usersGateway = mock<UsersGateway> {
                on { findLoggedIn() } doReturn
                        Single.error<User>(UserNotFound("not logged in", Throwable()))
                on { findById(id) } doReturn
                        Single.error<User>(UserNotFound("no id found", Throwable()))
                on { insert(id, firstName, lastName, password) } doReturn
                        Completable.complete()
            }
            val output = mock<RegisterOutput> { }
            val interactor: RegisterInput = Register(usersGateway)

            When("it succeeds") {
                interactor.execute(RegisterRequest(id, firstName, lastName, password, password),
                        output)

                Then("I get a confirmation") {
                    then(usersGateway).should().findLoggedIn()
                    then(usersGateway).should().findById(id)
                    then(usersGateway).should().insert(id, firstName, lastName, password)
                    then(output).should().generateViewModel(isA<RegisterResponse>())
                }
            }
        }
    }
})
