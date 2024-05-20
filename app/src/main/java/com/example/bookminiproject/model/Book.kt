package com.example.bookminiproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive

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