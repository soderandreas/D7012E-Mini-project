package com.example.bookminiproject.database

import android.util.Log
import com.example.bookminiproject.model.Author
import com.example.bookminiproject.model.AuthorWorksQueryResponse
import com.example.bookminiproject.model.SearchWorksQueryResponse
import com.example.bookminiproject.model.TrendingWorksQueryResponse
import com.example.bookminiproject.model.Work
import com.example.bookminiproject.model.WorkLocal
import com.example.bookminiproject.model.Works
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
    suspend fun getTrendingWorks(time: String = "weekly"): TrendingWorksQueryResponse
    suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse
    suspend fun getWorksQuery(query: String): SearchWorksQueryResponse
    suspend fun getWork(id: String): Work
    suspend fun getAuthorName(id: String): String
    suspend fun getAuthor(id: String): Author
}

class NetworkWorksRepository(private val apiService: BookDBApiService) : WorksRepository {
    override suspend fun getTrendingWorks(time: String): TrendingWorksQueryResponse {
        return apiService.getTrendingWorks(time)
    }

    override suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse {
        return apiService.getAuthorWorks(id)
    }

    override suspend fun getWorksQuery(query: String): SearchWorksQueryResponse {
        return apiService.getWorksQuery(query)
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

interface DBWorksRepository {
    suspend fun getWorks(): List<WorkLocal>

    suspend fun getWork(key: String): WorkLocal

    suspend fun insertWork(work: Work, authorName: String)

    suspend fun deleteWork(work: Work)

    suspend fun deleteAllWorks()
}

class LocalWorkRepository(private val bookDao: BookDao) : DBWorksRepository {
    override suspend fun getWorks(): List<WorkLocal> {
        return bookDao.getAllWorks()
    }

    override suspend fun getWork(key: String): WorkLocal {
        return bookDao.getWork(key)
    }

    override suspend fun insertWork(work: Work, authorName: String) {
        bookDao.insertNewWork(
            WorkLocal(
                key = work.key,
                title = work.title,
                coverImage = work.covers?.get(0),
                authorName = authorName,
                firstPublishDate = work.firstPublishDate
            )
        )
    }

    override suspend fun deleteWork(work: Work) {
        bookDao.deleteWork(work.key)
    }

    override suspend fun deleteAllWorks() {
        bookDao.deleteAllWorks()
    }
}
