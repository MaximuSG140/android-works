package com.example.SearchApp

import java.net.URL

class SearchResultEntryModel(
    var url: URL = URL("about:blank"),
    var title: String = "",
    var content: String = "",
    var expanded: Boolean = false
)