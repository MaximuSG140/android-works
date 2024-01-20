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

    override fun loadFromJson(value: String) {
        try {
            val jsonObject = JSONObject(value)
            val itemsList = jsonObject.getJSONArray("items")
            parseJsonArray(itemsList)
        } catch (e : Exception) {
            entries = emptyArray()
            e.printStackTrace()
        }
    }

    private fun parseJsonArray(array : JSONArray) {
        val size = array.length()
        val entriesNullable : Array<SearchResultEntryModel?> = arrayOfNulls(size)
        var i = 0
        while (i < size) {
            val entryObject = array.getJSONObject(i);
            val entryModel = SearchResultEntryModel();
            entryModel.title = entryObject.getString("title")
            entryModel.content = entryObject.getString("snippet")
            entriesNullable[i] = entryModel
            i++
        }

        entries = Array(size) { index -> entriesNullable[index]!! }
    }

    private var entries : Array<SearchResultEntryModel> = emptyArray()
}