package com.psa.kblog.gateways

import android.arch.persistence.room.Room
import android.content.Context
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.SessionClosingFailed
import com.psa.kblog.exceptions.UserInsertionFailed
import com.psa.kblog.exceptions.UserNotFound
import com.psa.kblog.gateways.internal.LocalDatabase
import com.psa.kblog.gateways.internal.UserModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RoomUsersGateway internal constructor(private val db: LocalDatabase) : UsersGateway {
    constructor(context: Context): this(
            Room.databaseBuilder(context, LocalDatabase::class.java, "kblog")
                    .build())

    override fun findLoggedIn(): Single<User> =
            db.users.queryLoggedInUser().subscribeOn(Schedulers.io())
                    .map { User(id = it.id, firstName = it.firstName, lastName = it.lastName) }
                    .onErrorResumeNext { Single.error(UserNotFound("User", it)) }

    override fun login(id: String, password: String): Completable =
            db.users.queryLogIn(id, password).subscribeOn(Schedulers.io())
                    .map { it.copy(loggedIn = true) }
                    .flatMapCompletable { Completable.defer { Completable.fromAction { db.users.update(it) } } }
                    .onErrorResumeNext { Completable.error(
                            UserNotFound("incorrect user id or password", it)) }

    override fun findById(id: String): Single<User> =
            db.users.findById(id).subscribeOn(Schedulers.io())
                    .map { User(it.id, it.firstName, it.lastName) }
                    .onErrorResumeNext {
                        Single.error(UserNotFound( "User does not exist", it))
                    }

    override fun insert(id: String,
                        firstName: String,
                        lastName: String,
                        password: String): Completable =
            Completable.fromAction { db.users.insert(
                    UserModel(id, firstName, lastName, password, false)) }
                    .onErrorResumeNext { Completable.error(UserInsertionFailed(it)) }

    override fun logout(): Completable =
            db.users.queryLoggedInUser()
                    .map { it.copy(loggedIn = false) }
                    .flatMapCompletable {
                        Completable.fromAction { db.users.update(it) }
                    }
                    .onErrorResumeNext { Completable.error(SessionClosingFailed(it)) }
}