package com.psa.kblog.gateways.internal

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(version = 1, entities = [UserModel::class, BlogModel::class])
internal abstract class LocalDatabase: RoomDatabase() {
    abstract val users: UsersDAO
    abstract val blogs: BlogsDAO
}