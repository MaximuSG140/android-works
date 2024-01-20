package com.example.SearchApp

import android.util.Log

class SearchResultsViewControllerImpl(
    private val view: SearchResultsView,
    private val model: SearchResultsModel,
    private val searcher: SearchResultProvider
) : SearchResultsViewController, SearchResultProviderDelegate {
    override fun setEntryView(index: Int, entryView: SearchResultEntryView) {
        createChildController(index, entryView)
    }

    override fun getEntryViewController(position: Int): SearchResultEntryViewController {
        assert(childControllers != null)
        assert(childControllers!!.size > position)
        assert(childControllers!![position] != null)
        return childControllers!![position]!!
    }

    override fun handleJsonResponse(jsonString: String) {
        model.loadFromJson(jsonString)
        childControllers = arrayOfNulls(model.entriesCount())
        view.update();
    }

    override fun handleError() {
        Log.e("Net", "Unknown error")
    }

    private fun createChildController(position: Int, entryView: SearchResultEntryView) {
        childControllers!![position] =
            SearchResultEntryViewControllerImpl(this, position, model.entryAt(position), entryView)
    }

    private var childControllers: Array<SearchResultEntryViewController?>? = null
}