package com.valeserber.githubchallenge.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("/search/repositories")
    fun searchRepositoriesAsync(
        @Query("q") query: String,
        @Query("sort") criteria: String,
        @Query("order") order: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Deferred<NetworkSearchResponse>
}