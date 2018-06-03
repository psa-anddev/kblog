package com.psa.kblog.gateways.internal

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface UsersDAO {
    @Insert
    fun insert(vararg user: UserModel)

    @Update
    fun update(user: UserModel)

    @Query("Select * From users Where loggedIn > 0")
    fun queryLoggedInUser(): Single<UserModel>

    @Query("Select * From users Where id = :id And password = :password")
    fun queryLogIn(id: String, password: String): Single<UserModel>

    @Query("Select * From users Where id = :id")
    fun findById(id: String): Single<UserModel>
}