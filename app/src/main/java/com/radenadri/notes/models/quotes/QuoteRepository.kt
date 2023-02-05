package com.radenadri.notes.models.quotes

import com.radenadri.notes.interfaces.QuotesApi

class QuoteRepository constructor(private val retrofitService: QuotesApi) {
     suspend fun getQuotes() = retrofitService.getQuotes()
}