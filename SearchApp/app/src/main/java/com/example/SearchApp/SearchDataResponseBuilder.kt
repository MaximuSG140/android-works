package com.example.SearchApp

class SearchDataResponseBuilder {
    fun build(): SearchDataResponse {
        return SearchDataResponse(true, entries.toTypedArray())
    }

    fun addEntries(newEntries: Array<SearchDataResponseEntry>) {
        entries.addAll(newEntries)
    }

    fun entriesCount(): Int {
        return entries.size
    }

    private val entries: ArrayList<SearchDataResponseEntry> = ArrayList()
}