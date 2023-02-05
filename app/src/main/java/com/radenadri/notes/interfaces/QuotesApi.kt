package com.radenadri.notes.interfaces

import com.haroldadmin.cnradapter.NetworkResponse
import com.radenadri.notes.models.quotes.ErrorResult
import com.radenadri.notes.models.quotes.QuoteList
import retrofit2.http.GET

interface QuotesApi {
    @GET("/quotes")
    suspend fun getQuotes(): NetworkResponse<QuoteList, ErrorResult>
}