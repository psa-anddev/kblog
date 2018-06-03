package com.psa.kblog.gateways.internal

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "blogs",
        foreignKeys = [(ForeignKey(entity = UserModel::class,
                parentColumns = ["id"],
                childColumns = ["userId"]))])
data class BlogModel(val title: String,
                     val text: String,
                     val userId: String,
                     @PrimaryKey(autoGenerate = true)
                     val id: Int? = null)
