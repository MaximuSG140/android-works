package com.example.SearchApp

interface SearchResultProviderDelegate {
    fun handleJsonResponse(jsonString : String)
    fun handleError()
}