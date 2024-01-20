package com.example.SearchApp

class SearchResultEntryViewControllerImpl(
    private val parentController: SearchResultsViewController,
    private val index: Int,
    private val model: SearchResultEntryModel,
    private val entryView: SearchResultEntryView
) : SearchResultEntryViewController {

    override fun onClick() {
        val newValue = !model.expanded
        setExpanded(newValue)
        entryView.update()
    }

    override fun setExpanded(expanded: Boolean) {
        model.expanded = expanded
    }
}