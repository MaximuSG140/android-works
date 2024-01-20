package com.example.SearchApp

class SearchResultEntryViewImpl(
    val parentView: SearchResultsRecyclerViewAdapter,
    val position: Int
) : SearchResultEntryView {

    override fun update() {
        parentView.notifyItemChanged(position)
    }
}