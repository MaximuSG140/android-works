package com.example.SearchApp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val resultsView = findViewById<RecyclerView>(R.id.searchResultsView)
        val model = SearchResultsModelImpl()
        val searcher = SearchResultProviderImpl()
        val adapter = SearchResultsRecyclerViewAdapter(this, model)
        val controller = SearchResultsViewControllerImpl(adapter, model, searcher)
        adapter.setController(controller)
        resultsView.adapter = adapter
        resultsView.layoutManager = LinearLayoutManager(this)
        searcher.setDelegate(controller)
        val button = findViewById<ImageButton>(R.id.imageButton)
        val editText = findViewById<EditText>(R.id.editTextText)
        button.setOnClickListener{searcher.sendRequest(editText.text.toString())}
    }
}
