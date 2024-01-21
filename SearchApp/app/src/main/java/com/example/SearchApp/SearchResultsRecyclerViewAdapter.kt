package com.example.SearchApp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class SearchResultsScrollHelper(
    private val owner: SearchResultsRecyclerViewAdapter,
    recyclerView: RecyclerView
) :
    RecyclerView.OnScrollListener() {
    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy >= 0) {
            val visibleItemCount = recyclerView.layoutManager!!.childCount
            val totalItemCount = recyclerView.layoutManager!!.itemCount
            val firstVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (!owner.isLoading() && !owner.isLastPage()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    owner.loadMoreItems()
                }
            }
            return
        }

        val firstVisibleItemPosition =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (!owner.isLoading() && firstVisibleItemPosition <= 0) {
            owner.reloadItems()
        }
    }
}

class SearchResultsRecyclerViewAdapter(
    private val recyclerView: RecyclerView,
    private val context: Context,
    private val model: SearchResultsModel
) : Adapter<SearchResultRecyclerViewAdapterViewHolder>(), SearchResultsView {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SearchResultRecyclerViewAdapterViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.search_result_entry, parent, false)
        return SearchResultRecyclerViewAdapterViewHolder(view)
    }

    fun setController(newController: SearchResultsViewController) {
        controller = newController
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    fun isLastPage(): Boolean {
        return model.entriesCount() == 100
    }

    fun loadMoreItems() {
        isLoading = true
        // Default size of google page.
        controller?.loadEntries(10)
    }

    fun reloadItems() {
        controller?.reloadEntries(20);
    }

    override fun getItemCount(): Int {
        return model.entriesCount()
    }

    override fun onBindViewHolder(
        holder: SearchResultRecyclerViewAdapterViewHolder, position: Int
    ) {
        assert(controller != null)
        controller!!.setEntryView(position, SearchResultEntryViewImpl(this, position))
        val entry = model.entryAt(position)
        holder.title.text = entry.title
        holder.content.text = entry.content
        holder.content.visibility = if (entry.expanded) VISIBLE else GONE
        holder.expandButton.setOnClickListener {
            controller!!.getEntryViewController(position).onClick()
            notifyItemChanged(holder.bindingAdapterPosition)
        }

        holder.expandButton.setImageResource(
            if (entry.expanded)
                R.drawable.arrow_drop_up else R.drawable.arrow_drop_down
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun update() {
        // Trigger full rebuild.
        notifyDataSetChanged()
    }

    override fun itemsReset() {
        update()
        isLoading = false
    }

    override fun moreDataLoaded(count: Int) {
        isLoading = false
        update()
    }

    private var controller: SearchResultsViewController? = null
    private var scrollHelper = SearchResultsScrollHelper(this, recyclerView)
    private var isLoading: Boolean = false
}