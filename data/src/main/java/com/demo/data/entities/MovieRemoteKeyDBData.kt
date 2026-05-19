package com.demo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key_table")
data class MovieRemoteKeyDBData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nextPage: Int?,
    val prevPage: Int?
)
