package com.example.SearchApp

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultRecyclerViewAdapterViewHolder(
    itemView: View
) :
    RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.resultEntryTitle)
    val content: TextView = itemView.findViewById(R.id.resultEntryContent)
    val expandButton: ImageButton = itemView.findViewById(R.id.expandButton)
}