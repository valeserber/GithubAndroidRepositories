package com.valeserber.githubchallenge.domain

data class Repository(
    val id : Long,
    val name: String,
    val description: String?,
    val url: String,
    val starsCount: Long,
    val forksCount: Long,
    val watchersCount: Long,
    val language: String?,
    val owner: Owner
)