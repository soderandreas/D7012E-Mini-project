package com.example.bookminiproject.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingWorksQueryResponse(
    @SerialName(value = "query")
    var query: String = "",

    @SerialName(value = "works")
    var works: List<Works> = listOf(),

    @SerialName(value = "days")
    var days: Int,

    @SerialName(value = "hours")
    var hours: Int
)

@Serializable
data class AuthorWorksQueryResponse(
    @SerialName(value = "size")
    var size: Int = 0,
    @SerialName(value = "entries")
    var entries: List<Work>,
)

@Serializable
data class SearchWorksQueryResponse(
    @SerialName(value = "numFound")
    var numFound: Int = 0,
    @SerialName(value = "start")
    var start: Int = 0,
    @SerialName(value = "docs")
    var docs: List<Works>? = null
)

@Serializable
data class SubjectWorksQueryResponse(
    @SerialName(value = "key")
    var key: String = "",
    @SerialName(value = "work_count")
    var workCount: Int = 0,
    @SerialName(value = "works")
    var works: List<SubjectWorks>? = null,
)
