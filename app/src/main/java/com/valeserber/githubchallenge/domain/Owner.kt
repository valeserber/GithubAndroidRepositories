package com.valeserber.githubchallenge.domain

data class Owner(
    val id: Long,
    val name: String? = null,
    val avatarUrl: String? = null
)