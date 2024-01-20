package com.example.SearchApp

interface SearchResultsModel {
    fun entryAt(index : Int) : SearchResultEntryModel
    fun entriesCount() : Int
    fun loadFromJson(value : String)
}