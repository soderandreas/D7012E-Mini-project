package com.example.bookminiproject.database

import com.example.bookminiproject.model.Author
import com.example.bookminiproject.model.AuthorWorksQueryResponse
import com.example.bookminiproject.model.TrendingWorksQueryResponse
import com.example.bookminiproject.model.Work
import com.example.bookminiproject.network.BookDBApiService

interface BooksRepository {
    suspend fun getAuthorInformation(authorId: String): Author
}

class NetworkBooksRepository(private val apiService: BookDBApiService) : BooksRepository {
    override suspend fun getAuthorInformation(authorId: String): Author {
        return apiService.getAuthorInformation(authorId)
    }
}

interface WorksRepository {
    suspend fun getTrendingWorks(): TrendingWorksQueryResponse
    suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse
    suspend fun getWork(id: String): Work
    suspend fun getAuthorName(id: String): String
    suspend fun getAuthor(id: String): Author
}

class NetworkWorksRepository(private val apiService: BookDBApiService) : WorksRepository {
    override suspend fun getTrendingWorks(): TrendingWorksQueryResponse {
        return apiService.getTrendingWorks()
    }

    override suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse {
        return apiService.getAuthorWorks(id)
    }

    override suspend fun getWork(id: String): Work {
        return apiService.getWork(id)
    }

    override suspend fun getAuthorName(id: String): String {
        return apiService.getAuthorInformation(id).name
    }

    override suspend fun getAuthor(id: String): Author {
        return apiService.getAuthorInformation(id)
    }
}
