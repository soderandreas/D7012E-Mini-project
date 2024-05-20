package com.example.bookminiproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "covers")
    var covers: List<Int>,

    @SerialName(value = "publish_date")
    var publishDate: String = "",

    @SerialName(value = "number_of_pages")
    var numberOfPages: Int = 0,

    @SerialName(value = "description")
    var description: String = "",

    @SerialName(value = "subjects")
    var subjects: List<String>
)

// Used for one specific work
@Serializable
data class Work(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "covers")
    var covers: List<Int>? = null,

    @Serializable(with = DescriptionSerializer::class)
    @SerialName(value = "description")
    var description: Description? = null,

    @SerialName(value = "subjects")
    var subjects: List<String> = listOf(),

    @SerialName(value = "authors")
    var authors: List<AuthorRole>,

    @SerialName(value = "first_publish_date")
    var firstPublishDate: String? = null
)

// For many works, used in query
@Serializable
data class Works(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "cover_i")
    var coverImage: Int? = null,

    @SerialName(value = "author_name")
    var authorName: List<String> = listOf(""),

    @SerialName(value = "first_publish_year")
    var firstPublishYear: Int? = null,
)

// For many works, used in subject query
@Serializable
data class SubjectWorks (
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "cover_id")
    var coverImage: Int? = null,

    @SerialName(value = "first_publish_year")
    var firstPublishYear: Int? = null,

    @SerialName(value = "authors")
    var authors: List<SubjectAuthors> = listOf()
)

// Local version of works
@Serializable
@Entity(tableName = "work")
data class WorkLocal(
    @PrimaryKey
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "cover_i")
    var coverImage: Int? = null,

    @SerialName(value = "author_name")
    var authorName: String = "",

    @SerialName(value = "first_publish_date")
    var firstPublishDate: String? = "",
)

@Serializable
data class SubjectAuthors(
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class AuthorRole(
    @SerialName("author")
    val author: AuthorKey
)

@Serializable
data class AuthorKey(
    @SerialName("key")
    val key: String
)

@Serializable
data class Author(
    @SerialName(value = "name")
    var name: String,

    @Serializable(with = BiographySerializer::class)
    @SerialName(value = "bio")
    var bio: Biography? = null,

    @SerialName(value = "photos")
    var photos: List<Int> = listOf(),

    @SerialName(value = "birth_date")
    var birthDate: String? = null,

    @SerialName(value = "death_date")
    var deathDate: String? = null
)