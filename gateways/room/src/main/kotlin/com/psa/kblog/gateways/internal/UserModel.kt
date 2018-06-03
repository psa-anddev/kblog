package com.psa.kblog.gateways.internal

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(@PrimaryKey val id: String,
                val firstName: String,
                val lastName: String,
                val password: String,
                val loggedIn: Boolean)
