package com.example.SearchApp

interface SearchResultProvider {
    fun sendRequest(query : String)
}