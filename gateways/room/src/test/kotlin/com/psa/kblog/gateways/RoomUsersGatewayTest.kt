package com.psa.kblog.gateways

import android.arch.persistence.room.Room
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.SessionClosingFailed
import com.psa.kblog.exceptions.UserInsertionFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.internal.LocalDatabase
import com.psa.kblog.gateways.internal.UserModel
import com.psa.kblog.test.NonEmptyStringGenerator
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeUnit.SECONDS

@RunWith(RobolectricTestRunner::class)
class RoomUsersGatewayTest {
    private val db by lazy {
        Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application,
                LocalDatabase::class.java)
                .build()
    }
    private val gateway by lazy { RoomUsersGateway(db) }

    @Test
    fun `should be a users gateway`() {
        gateway should beInstanceOf(UsersGateway::class)
    }

    @Test
    fun `should throw user not logged in exception if no user exist`() {
        val actual = gateway.findLoggedIn()
                .test()

        actual.awaitTerminalEvent(1, SECONDS)
        actual.assertError(UserNotFound::class.java)
    }

    @Test
    fun `should throw user not found exception if no user is logged in`() {
        val actual = Completable.fromAction {
            db.users.insert(
                    UserModel(id = "test",
                            firstName = "Test",
                            lastName = "User",
                            password = "t35t",
                            loggedIn = false),
                    UserModel(id = "root",
                            firstName = "Administrator",
                            lastName = "User",
                            password = "r00t",
                            loggedIn = false))
        }
                .subscribeOn(Schedulers.io())
                .andThen(gateway.findLoggedIn())
                .test()

        actual.awaitTerminalEvent(1, SECONDS)
        actual.assertError(UserNotFound::class.java)
    }

    @Test
    fun `should find the logged in user`() {
        val values = table(headers("models", "user"),
                row(listOf(UserModel(id = "bruce.banner",
                        firstName = "Bruce",
                        lastName = "Banner",
                        password = "b4nn3r",
                        loggedIn = false),
                        UserModel(id = "diana.prince",
                                firstName = "Diana",
                                lastName = "Prince",
                                password = "pr1nc3",
                                loggedIn = true),
                        UserModel(id = "clark.kent",
                                firstName = "Clark",
                                lastName = "Kent",
                                password = "k3nt",
                                loggedIn = false)),
                        User(id = "diana.prince",
                                firstName = "Diana",
                                lastName = "Prince")),
                row(listOf(UserModel(id = "user1",
                        firstName = "User",
                        lastName = "One",
                        password = "pass",
                        loggedIn = true),
                        UserModel("user2",
                                firstName = "User",
                                lastName = "Two",
                                password = "root",
                                loggedIn = false)),
                        User(id = "user1",
                                firstName = "User",
                                lastName = "One")))

        forAll(values) { models, user ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .subscribeOn(Schedulers.io())
                    .andThen(Completable.fromAction { db.users.insert(*models.toTypedArray()) })
                    .andThen(gateway.findLoggedIn())
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(user)
        }
    }

    @Test
    fun `should fail to log in if the user does not exist`() {
        forAll(Gen.string(), Gen.string()) { id, password ->
            val actual = gateway.login(id, password)
                    .test()
            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(UserNotFound::class.java)
            true
        }
    }

    @Test
    fun `should fail to log in if the password is wrong`() {
        forAll(NonEmptyStringGenerator(), NonEmptyStringGenerator(), NonEmptyStringGenerator()) { id, realPassword, enteredPassword ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .subscribeOn(Schedulers.io())
                    .andThen(Completable.fromAction {
                        db.users.insert(UserModel(id = id,
                                firstName = "Test",
                                lastName = "User",
                                password = realPassword,
                                loggedIn = false))
                    })
                    .andThen(gateway.login(id, enteredPassword))
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(UserNotFound::class.java)
            true
        }
    }

    @Test
    fun `should succeed log in if id and password are correct`() {
        forAll(NonEmptyStringGenerator(), NonEmptyStringGenerator()) { id, password ->
            val userModel = UserModel(id = id,
                    password = password,
                    firstName = "Test",
                    lastName = "User",
                    loggedIn = false)

            val actual = Completable.fromAction { db.clearAllTables() }
                    .andThen(Completable.fromAction { db.users.insert(userModel) })
                    .subscribeOn(Schedulers.io())
                    .andThen(Completable.defer { gateway.login(id, password) })
                    .andThen(Single.defer { db.users.queryLoggedInUser() })
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(userModel.copy(loggedIn = true))
            true
        }
    }

    @Test
    fun `should not find user if no user with that id exists`() {
        forAll(NonEmptyStringGenerator()) { id ->
            val actual = gateway.findById(id).test()
            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(UserNotFound::class.java)
            true
        }
    }

    @Test
    fun `should find user by id`() {
        forAll(NonEmptyStringGenerator(),
                NonEmptyStringGenerator(),
                NonEmptyStringGenerator()) { id, first, last ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .andThen(Completable.fromAction {
                        db.users.insert(UserModel(id = id,
                                firstName = first,
                                lastName = last,
                                password = "pass",
                                loggedIn = false))
                    })
                    .andThen(gateway.findById(id))
                    .subscribeOn(Schedulers.io())
                    .test()
            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(User(id, firstName = first, lastName = last))
            true
        }
    }

    @Test
    fun `should insert users`() {
        forAll(NonEmptyStringGenerator(),
                NonEmptyStringGenerator(),
                NonEmptyStringGenerator(),
                NonEmptyStringGenerator()) { id, first, last, password ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .subscribeOn(Schedulers.io())
                    .andThen(gateway.insert(id, first, last, password))
                    .andThen(db.users.findById(id))
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(UserModel(id, first, last, password, false))
            true
        }
    }

    @Test
    fun `should fail to insert user with an id that does not exist`() {
        forAll(NonEmptyStringGenerator(),
                NonEmptyStringGenerator(),
                NonEmptyStringGenerator(),
                NonEmptyStringGenerator()) { id, first, last, password ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .andThen(Completable.fromAction {
                        db.users.insert(
                                UserModel(id = id,
                                        firstName = "Test",
                                        lastName = "User",
                                        password = "test",
                                        loggedIn = false))
                    })
                    .andThen(gateway.insert(id, first, last, password))
                    .subscribeOn(Schedulers.io())
                    .test()
            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(UserInsertionFailed::class.java)
            true
        }
    }

    @Test
    fun `should log user out`() {
        val values = table(headers("value"),
                row(UserModel(id = "test",
                        firstName = "Test",
                        lastName = "User",
                        password = "test",
                        loggedIn = true)),
                row(UserModel(id = "root",
                        firstName = "Administrator",
                        lastName = "User",
                        password = "r00t",
                        loggedIn = true)))
        forAll(values) {
            val actual =
                    Completable.fromAction { db.clearAllTables() }
                            .subscribeOn(Schedulers.io())
                            .andThen(Completable.fromAction { db.users.insert(it) })
                            .andThen(gateway.logout())
                            .andThen(db.users.findById(it.id))
                            .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(it.copy(loggedIn = false))
        }
    }

    @Test
    fun `should fail to log out if user is not logged in`() {
        val values = table(headers("value"),
                row(UserModel(id = "test",
                        firstName = "Test",
                        lastName = "User",
                        password = "test",
                        loggedIn = false)),
                row(UserModel(id = "root",
                        firstName = "Administrator",
                        lastName = "User",
                        password = "r00t",
                        loggedIn = false)))
        forAll(values) {
            val actual =
                    Completable.fromAction { db.clearAllTables() }
                            .subscribeOn(Schedulers.io())
                            .andThen(Completable.fromAction { db.users.insert(it) })
                            .andThen(gateway.logout())
                            .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(SessionClosingFailed::class.java)
        }
    }

    @Test
    fun `should create a gateway given a context`() {
        RoomUsersGateway(RuntimeEnvironment.application) should
                beInstanceOf(UsersGateway::class)
    }

    @After
    fun tearDown() {
        db.close()
    }
}