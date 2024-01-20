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

class SearchResultProviderImpl : SearchResultProvider {
    class CancellableCallback(
        var owner: SearchResultProviderImpl?,
        val mainThreadHandler: Handler
    ) : okhttp3.Callback {
        fun cancel() {
            owner = null;
        }

        override fun onFailure(call: Call, e: IOException) {
            mainThreadHandler.post { owner?.onFailure() }
        }

        override fun onResponse(call: Call, response: Response) {
            mainThreadHandler.post { owner?.onSuccess(response) }
        }
    }

    fun setDelegate(newDelegate: SearchResultProviderDelegate) {
        delegate = newDelegate
    }

    override fun sendRequest(query: String) {
        requestInProgress?.cancel()

        val url = HttpUrl.Builder().host("customsearch.googleapis.com").scheme("https")
            .addPathSegment("customsearch").addPathSegment("v1").addQueryParameter("q", query)
            .addQueryParameter("cx", "e4016032c8d1c48f3")
            .addQueryParameter("key", "AIzaSyBnxmnwIq4v95mbdBukU9dtVSQ2w4PsRHM").build()
        val request = Request.Builder().method("GET", null).url(url).build()
        val call = client.newCall(request)

        requestInProgress =
            CancellableCallback(this, HandlerCompat.createAsync(Looper.getMainLooper()))
        call.enqueue(requestInProgress!!)
    }

    fun onFailure() {
        delegate?.handleError();
        requestInProgress?.cancel()
        requestInProgress = null;
    }

    fun onSuccess(response: Response) {
        if (response.isSuccessful && response.body != null) {
            delegate?.handleJsonResponse(response.body!!.string())
        }
        requestInProgress?.cancel()
        requestInProgress = null;
    }

    private val client = OkHttpClient()
    private var requestInProgress: CancellableCallback? = null
    private var delegate: SearchResultProviderDelegate? = null
}