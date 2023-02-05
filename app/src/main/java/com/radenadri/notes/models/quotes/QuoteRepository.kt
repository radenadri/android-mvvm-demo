package com.radenadri.notes.models.quotes

import com.radenadri.notes.interfaces.QuotesApi

class QuoteRepository constructor(private val retrofitService: QuotesApi) {
     suspend fun getQuotes(page: Int = 1) = retrofitService.getQuotes(page)
}