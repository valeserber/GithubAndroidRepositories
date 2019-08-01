package com.valeserber.githubchallenge.network

import com.squareup.moshi.Json

//DAOs (Data transfer objects) are responsible for parsing responses from the server and formatting objects
//to send to the server

data class NetworkSearchResponse(
    val items: List<NetworkRepository> = emptyList())

data class NetworkRepository(
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
    val owner: NetworkOwner)


data class NetworkOwner(
    @Json(name = "login")
    val ownerName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)