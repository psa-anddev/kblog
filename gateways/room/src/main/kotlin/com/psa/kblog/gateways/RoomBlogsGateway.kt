package com.psa.kblog.gateways

import android.arch.persistence.room.Room
import android.content.Context
import com.psa.kblog.entities.Blog
import com.psa.kblog.entities.User
import com.psa.kblog.exceptions.BlogInsertionFailed
import com.psa.kblog.gateways.internal.BlogModel
import com.psa.kblog.gateways.internal.LocalDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RoomBlogsGateway internal constructor(private val db: LocalDatabase) : BlogsGateway {
    constructor(context: Context) :
            this(Room.databaseBuilder(context, LocalDatabase::class.java, "kblog").build())

    override fun findByUser(user: User): Observable<Blog> =
            Single.fromCallable { db.blogs.findByUserId(user.id) }
                    .subscribeOn(Schedulers.io())
                    .flatMapObservable { Observable.fromIterable(it) }
                    .map { Blog(id = it.id ?: 0, title = it.title, text = it.text) }

    override fun insert(title: String, text: String, user: User): Single<Blog> =
            Single.fromCallable {
                db.blogs.insert(BlogModel(title = title, text = text, userId = user.id))
            }.map { Blog(id = it.toInt(), title = title, text = text) }
                    .subscribeOn(Schedulers.io())
                    .onErrorResumeNext { Single.error(BlogInsertionFailed(it)) }
}