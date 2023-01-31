package com.radenadri.notes.interfaces

import com.radenadri.notes.models.quotes.QuoteList
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {
    @GET("/quotes")
    suspend fun getQuotes(): Response<QuoteList>
}