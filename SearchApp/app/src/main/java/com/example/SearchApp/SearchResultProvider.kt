package com.example.SearchApp

interface SearchResultProvider {
    fun makeSearchQuery(query : String, start: Int, count: Int)
}