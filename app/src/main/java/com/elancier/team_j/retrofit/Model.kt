package com.elancier.team_j.retrofit

object Model {
    data class Result(val query: Query)
    data class Query(val searchinfo: SearchInfo)
    data class SearchInfo(val totalhits: Int)
}