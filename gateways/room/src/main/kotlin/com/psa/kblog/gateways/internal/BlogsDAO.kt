package com.psa.kblog.gateways.internal

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface BlogsDAO {
    @Insert
    fun insert(blog: BlogModel): Long

    @Query("Select * From blogs Where userId = :userId")
    fun findByUserId(userId: String): List<BlogModel>
}