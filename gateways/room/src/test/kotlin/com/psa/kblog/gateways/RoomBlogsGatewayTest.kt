package com.psa.kblog.gateways

import android.arch.persistence.room.Room
import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.BlogInsertionFailed
import com.psa.kblog.gateways.internal.BlogModel
import com.psa.kblog.gateways.internal.LocalDatabase
import com.psa.kblog.gateways.internal.UserModel
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeUnit.SECONDS

@RunWith(RobolectricTestRunner::class)
class RoomBlogsGatewayTest {
    private val db by lazy {
        Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application,
                LocalDatabase::class.java)
                .build()
    }
    private val gateway by lazy { RoomBlogsGateway(db) }

    @Test
    fun `should be a blogs gateway`() {
        RoomBlogsGateway(RuntimeEnvironment.application) should
                beInstanceOf(BlogsGateway::class)
    }

    @Test
    fun `should return blogs from a certain user`() {
        val values = table(headers("user", "blogs", "startId"),
                row(UserModel(id = "test",
                        firstName = "Test",
                        lastName = "User",
                        password = "aaa",
                        loggedIn = true),
                        listOf(BlogModel(title = "Test Blog",
                                text = "This is a test blog.",
                                userId = "test"),
                                BlogModel(title = "Second Blog",
                                        text = "This is the second blog",
                                        userId = "test")),
                        0),
                row(UserModel(id = "bruce.banner",
                        firstName = "Bruce",
                        lastName = "Banner",
                        password = "avengers",
                        loggedIn = false),
                        listOf(BlogModel("Avengers: A new Hope",
                                "I have nothing to say about that",
                                userId = "bruce.banner")),
                        2))

        forAll(values) { userModel, blogModels, startId ->
            val actual = Completable.fromAction { db.clearAllTables() }
                    .subscribeOn(Schedulers.io())
                    .andThen(Completable.fromAction { db.users.insert(userModel) })
                    .andThen(Observable.fromIterable(blogModels))
                    .flatMapCompletable { Completable.fromAction { db.blogs.insert(it) } }
                    .andThen(gateway.findByUser(User(id = userModel.id,
                            firstName = userModel.firstName,
                            lastName = userModel.lastName)))
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            blogModels.forEachIndexed { index, blogModel ->
                actual.assertValueAt(index,
                        Blog(id = index + startId + 1,
                                title = blogModel.title,
                                text = blogModel.text))
            }
        }
    }

    @Test
    fun `should insert blog`() {
        val values = table(headers("user", "blog"),
                row(User(id = "bruce.banner",
                        firstName = "Bruce",
                        lastName = "Banner"),
                        Blog(id = 1,
                                title = "Test",
                                text = "Some test")),
                row(User(id = "diana.prince",
                        firstName = "Diana",
                        lastName = "Prince"),
                        Blog(id = 2,
                                title = "Wonder Woman",
                                text = "Some other blog")))

        forAll(values) { user, blog ->
            val actual = Single.just(user)
                    .subscribeOn(Schedulers.io())
                    .map { UserModel(id = it.id,
                                firstName = it.firstName,
                                lastName = it.lastName,
                                password = "pass",
                                loggedIn = false) }
                    .flatMapCompletable { Completable.fromAction { db.users.insert(it) } }
                    .andThen(gateway.insert(blog.title, blog.text, user))
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertValue(blog)
        }
    }

    @Test
    fun `should fail to insert a blog if the user does not exist`() {
        val values = table(headers("blog", "user"),
                row(Blog(id = 1, text = "Test", title = "Title"),
                        User(id = "1", firstName = "", lastName = "")),
                row(Blog(id = 2, text = "Heading", title = "Heading"),
                        User(id = "test", firstName = "Test", lastName = "User")))

        forAll(values) { blog, user ->
            val actual = gateway.insert(blog.title, blog.text, user)
                    .test()

            actual.awaitTerminalEvent(1, SECONDS)
            actual.assertError(BlogInsertionFailed::class.java)
        }
    }

    @After
    fun tearDown() {
        db.close()
    }
}