package com.example.SearchApp
interface SearchResultProviderDelegate {
    fun onDataResponse(begin: Int, query: String, data: SearchDataResponse)
}