package com.valeserber.githubchallenge.network

import com.squareup.moshi.Json
import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.DBRepository
import com.valeserber.githubchallenge.domain.Owner

//DTOs (Data transfer objects) are responsible for parsing responses from the server and formatting objects
//to send to the server

data class NetworkSearchResponse(
    val items: List<NetworkRepository> = emptyList()
)

data class NetworkRepository(
    val id: Long,
    val name: String,
    val description: String?,
    val url: String,
    @Json(name = "stargazers_count")
    val starsCount: Long,
    @Json(name = "forks_count")
    val forksCount: Long,
    @Json(name = "watchers_count")
    val watchersCount: Long,
    val language: String?,
    val owner: NetworkOwner
)


data class NetworkOwner(
    val id: Long,
    @Json(name = "login")
    val ownerName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)


fun NetworkOwner.asDomainModel(): Owner {
    return Owner(id, ownerName, avatarUrl)
}

fun NetworkSearchResponse.asDatabaseModel(): Pair<Array<DBOwner>, Array<DBRepository>> {

    val repositoriesList = ArrayList<DBRepository>()
    val ownersList = ArrayList<DBOwner>()

    items.forEach {
        val repo = DBRepository(
            it.id,
            it.name,
            it.description,
            it.url,
            it.starsCount,
            it.forksCount,
            it.watchersCount,
            it.language,
            it.owner.id
        )
        val owner = DBOwner(
            it.owner.id,
            it.owner.ownerName,
            it.owner.avatarUrl
        )
        repositoriesList.add(repo)
        ownersList.add(owner)
    }

    return Pair(ownersList.toTypedArray(), repositoriesList.toTypedArray())
}
