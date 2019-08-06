package com.valeserber.githubchallenge.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

//Foreign Key with onDelete = CASCADE means that whenever an Owner is deleted,
//then all of its repositories will be deleted
@Entity(
    tableName = "repositories",
    foreignKeys = [ForeignKey(
        entity = DBOwner::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ownerId"),
        onDelete = CASCADE
    )]
)
data class DBRepository(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val url: String,
    val starsCount: Long,
    val forksCount: Long,
    val watchersCount: Long,
    val language: String?,
    val ownerId: Long
)

@Entity(tableName = "owners")
data class DBOwner(
    @PrimaryKey val id: Long,
    val name: String,
    val avatarUrl: String
)