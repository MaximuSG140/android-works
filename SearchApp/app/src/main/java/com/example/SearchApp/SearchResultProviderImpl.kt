package com.example.SearchApp

import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class SearchResultProviderImpl : SearchResultProvider {
    class SingleRequestHandler(
        private var owner: SearchResultProviderImpl,
        private val mainThreadHandler: Handler
    ) : okhttp3.Callback {
        fun cancelRequest() {
            isCancelled = true
        }

        override fun onFailure(call: Call, e: IOException) {
            if (isCancelled) {
                return
            }
            mainThreadHandler.post { owner.onFailure(this) }
        }

        override fun onResponse(call: Call, response: Response) {
            if (isCancelled) {
                return
            }
            mainThreadHandler.post { owner.onSuccess(this, response) }
        }

        private var isCancelled = false
    }

    class RequestData(val start: Int, val count: Int, val query: String)

    fun setDelegate(newDelegate: SearchResultProviderDelegate) {
        delegate = newDelegate
    }

    override fun makeSearchQuery(query: String, start: Int, count: Int) {
        reset()
        currentRequestData = RequestData(start, count, query)
        doSearchQuery()
    }

    fun onFailure(handler: SingleRequestHandler) {
        if (handler != currentRequestHandler) {
            return
        }
        passDataToDelegate(SearchDataResponse(false))
    }

    fun onSuccess(handler: SingleRequestHandler, response: Response) {
        if (handler != currentRequestHandler) {
            return
        }

        if (!response.isSuccessful || response.body == null) {
            passDataToDelegate(SearchDataResponse(false))
            return
        }

        val parsedResponse = SearchDataResponse.fromJson(response.body!!.string())
        if (!parsedResponse.isValid) {
            passDataToDelegate(SearchDataResponse(false))
            return
        }

        responseDataBuilder.addEntries(parsedResponse.entries)
        if (responseDataBuilder.entriesCount() >= currentRequestData!!.count) {
            passDataToDelegate(responseDataBuilder.build())
            return
        }

        // In case if data is not enough send another request.
        doSearchQuery()
    }

    private fun passDataToDelegate(data: SearchDataResponse) {
        assert(delegate != null)
        // Request should be in progress.
        assert(currentRequestData != null)
        // All data has to be collected or fail has occurred.
        assert(data.entries.size == currentRequestData!!.count || !data.isValid)
        assert(currentRequestHandler != null)
        delegate!!.onDataResponse(
            currentRequestData!!.start,
            currentRequestData!!.query,
            data
        )
        reset()
    }

    private fun reset() {
        currentRequestHandler?.cancelRequest()
        currentRequestHandler = null
        responseDataBuilder = SearchDataResponseBuilder()
        currentRequestData = null
    }

    private fun doSearchQuery() {
        assert(currentRequestData != null)
        assert(responseDataBuilder.entriesCount() < currentRequestData!!.count)
        val requestStart = currentRequestData!!.start + responseDataBuilder.entriesCount()
        // Limit count to 10 because google api restricts entries count to 10.
        val countToRequest =
            min(currentRequestData!!.count - responseDataBuilder.entriesCount(), 10)
        val url = HttpUrl.Builder().host("customsearch.googleapis.com").scheme("https")
            .addPathSegment("customsearch").addPathSegment("v1")
            .addQueryParameter("q", currentRequestData!!.query)
            .addQueryParameter("cx", "e4016032c8d1c48f3")
            .addQueryParameter("key", "AIzaSyBnxmnwIq4v95mbdBukU9dtVSQ2w4PsRHM")
            .addQueryParameter("start", requestStart.toString())
            .addQueryParameter("num", countToRequest.toString()).build()
        val request = Request.Builder().method("GET", null).url(url).build()
        val call = client.newCall(request)

        currentRequestHandler =
            SingleRequestHandler(
                this,
                HandlerCompat.createAsync(Looper.getMainLooper())
            )
        call.enqueue(currentRequestHandler!!)
    }

    private val client = OkHttpClient()
    private var delegate: SearchResultProviderDelegate? = null
    private var currentRequestHandler: SingleRequestHandler? = null
    private var currentRequestData: RequestData? = null
    private var responseDataBuilder = SearchDataResponseBuilder()
}