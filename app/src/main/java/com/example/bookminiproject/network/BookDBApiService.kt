package com.example.bookminiproject.network

import com.example.bookminiproject.model.Author
import com.example.bookminiproject.model.AuthorWorksQueryResponse
import com.example.bookminiproject.model.SearchWorksQueryResponse
import com.example.bookminiproject.model.TrendingWorksQueryResponse
import com.example.bookminiproject.model.Work
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookDBApiService {
    @GET("search.json")
    suspend fun getWorksQuery(
        @Query("title")
        query: String = "",
        @Query("limit")
        limit: String = "10"
    ): SearchWorksQueryResponse

    @GET("{Id}.json")
    suspend fun getAuthorInformation(
        @Path("Id")
        id: String,
    ): Author

    @GET("{Id}/works.json")
    suspend fun getAuthorWorks(
        @Path("Id")
        id: String,
    ): AuthorWorksQueryResponse

    @GET("trending/{Time}.json")
    suspend fun getTrendingWorks(
        @Path("Time")
        time: String = "weekly",
        @Query("limit")
        limit: String = "10",
    ): TrendingWorksQueryResponse

    @GET("{WorkId}.json")
    suspend fun getWork(
        @Path("WorkId")
        id: String = ""
    ): Work
}