package com.example.bookminiproject.database

import android.content.Context
import com.example.bookminiproject.network.BookDBApiService
import com.example.bookminiproject.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val booksRepository: NetworkBooksRepository
    val worksRepository: NetworkWorksRepository
    val localWorksRepository: LocalWorksRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val bookDBJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(bookDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.BOOK_LIST_BASE_URL)
        .build()

    private val retrofitService: BookDBApiService by lazy {
        retrofit.create(BookDBApiService::class.java)
    }

    override val booksRepository: NetworkBooksRepository by lazy {
        NetworkBooksRepository(retrofitService)
    }

    override val worksRepository: NetworkWorksRepository by lazy {
        NetworkWorksRepository(retrofitService)
    }

    override val localWorksRepository: LocalWorksRepository by lazy {
        LocalWorksRepository(BookDatabase.getDatabase(context).bookDao())
    }
}
