package com.example.SearchApp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityDelegate(val queryEdit: EditText) : SearchResultsViewControllerDelegate {
    override fun getCurrentQuery(): String {
        return queryEdit.text.toString()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val resultsView = findViewById<RecyclerView>(R.id.searchResultsView)
        val model = SearchResultsModelImpl()
        val searcher = SearchResultProviderImpl()
        val adapter = SearchResultsRecyclerViewAdapter(resultsView, this, model)
        val editText = findViewById<EditText>(R.id.editTextText)
        val controller = SearchResultsViewControllerImpl(
            adapter,
            model,
            searcher,
            ActivityDelegate(editText)
        )
        adapter.setController(controller)
        resultsView.adapter = adapter
        resultsView.layoutManager = LinearLayoutManager(this)
        searcher.setDelegate(controller)
        val button = findViewById<ImageButton>(R.id.imageButton)
        button.setOnClickListener { searcher.makeSearchQuery(editText.text.toString(), 0, 20) }
    }
}
