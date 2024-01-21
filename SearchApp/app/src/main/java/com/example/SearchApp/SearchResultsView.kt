package com.example.SearchApp

interface SearchResultsView {
    fun update()
    fun itemsReset()
    fun moreDataLoaded(count: Int)
}