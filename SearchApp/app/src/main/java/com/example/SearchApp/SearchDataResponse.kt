package com.example.SearchApp

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class SearchDataResponse(
    var isValid: Boolean = false,
    var entries: Array<SearchDataResponseEntry> = arrayOf()
) {
    companion object {
        fun fromJson(json: String): SearchDataResponse {
            return try {
                val jsonObject = JSONObject(json)
                val itemsList = jsonObject.getJSONArray("items")
                val entries = parseResultsArray(itemsList)
                SearchDataResponse(true, entries)
            } catch (e: Exception) {
                e.printStackTrace()
                SearchDataResponse(false)
            }
        }

        private fun parseResultsArray(jsonArray: JSONArray): Array<SearchDataResponseEntry> {
            val size = jsonArray.length()
            val entriesNullable: Array<SearchDataResponseEntry?> = arrayOfNulls(size)
            var i = 0
            while (i < size) {
                val entryObject = jsonArray.getJSONObject(i)
                val title = entryObject.getString("title")
                val content = entryObject.getString("snippet")
                val url = entryObject.getString("link")
                entriesNullable[i] = SearchDataResponseEntry(title, content, URL(url))
                i++
            }

            return Array(size) { index -> entriesNullable[index]!! }
        }
    }


}