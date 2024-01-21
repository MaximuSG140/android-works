package com.example.SearchApp

import org.json.JSONArray
import org.json.JSONObject

class SearchResultsModelImpl : SearchResultsModel {
    override fun entryAt(index: Int): SearchResultEntryModel {
        return entries[index]
    }

    override fun entriesCount(): Int {
        return entries.size
    }

    override fun handleReceivedData(start: Int, data: SearchDataResponse) {
        if (!data.isValid) {
            return
        }
        entries.ensureCapacity(start + data.entries.size)
        var iterateIndex = 0
        while (iterateIndex < data.entries.size && iterateIndex + start < entries.size) {
            val currentData = data.entries[iterateIndex]
            entries[start + iterateIndex] =
                SearchResultEntryModel(currentData.url, currentData.title, currentData.content)
            iterateIndex++
        }

        while (iterateIndex < data.entries.size) {
            val currentData = data.entries[iterateIndex]
            entries.add(SearchResultEntryModel(currentData.url, currentData.title, currentData.content))
            iterateIndex++
        }
    }

    override fun resetData() {
        entries = ArrayList()
    }

    private var entries: ArrayList<SearchResultEntryModel> = ArrayList()
}