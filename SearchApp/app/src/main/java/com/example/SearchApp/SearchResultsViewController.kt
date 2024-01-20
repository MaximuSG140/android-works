package com.example.SearchApp

interface SearchResultsViewController {
    fun setEntryView(index : Int, entryView : SearchResultEntryView)
    fun getEntryViewController(position: Int) : SearchResultEntryViewController
}
