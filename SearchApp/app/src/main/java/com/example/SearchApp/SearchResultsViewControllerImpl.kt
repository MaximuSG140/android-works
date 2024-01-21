package com.example.SearchApp

import java.util.HashMap

class SearchResultsViewControllerImpl(
    private val view: SearchResultsView,
    private val model: SearchResultsModel,
    private val searcher: SearchResultProvider,
    private val delegate: SearchResultsViewControllerDelegate
) : SearchResultsViewController, SearchResultProviderDelegate {
    override fun setEntryView(index: Int, entryView: SearchResultEntryView) {
        createChildController(index, entryView)
    }

    override fun getEntryViewController(position: Int): SearchResultEntryViewController {
        assert(childControllers[position] != null)
        return childControllers[position]!!
    }

    override fun loadEntries(entriesNumber: Int) {
        searcher.makeSearchQuery(delegate.getCurrentQuery(), model.entriesCount(), entriesNumber)
    }

    override fun reloadEntries(entriesNumber: Int) {
        model.resetData()
        view.itemsReset()
        searcher.makeSearchQuery(delegate.getCurrentQuery(), 0, 10)
    }

    override fun onDataResponse(begin: Int, query: String, data: SearchDataResponse) {
        if (query != delegate.getCurrentQuery()) {
            return
        }

        model.handleReceivedData(begin, data)
        view.moreDataLoaded(data.entries.size)
    }

    private fun createChildController(position: Int, entryView: SearchResultEntryView) {

        childControllers[position] =
            SearchResultEntryViewControllerImpl(this, position, model.entryAt(position), entryView)
    }

    private var childControllers = HashMap<Int, SearchResultEntryViewController>(100)
}