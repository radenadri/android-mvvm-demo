package com.radenadri.notes.interfaces

import com.haroldadmin.cnradapter.NetworkResponse
import com.radenadri.notes.models.quotes.ErrorResult
import com.radenadri.notes.models.quotes.QuoteList
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {
    @GET("/quotes")
    suspend fun getQuotes(
        @Query("page") page: Int = 1,
    ): NetworkResponse<QuoteList, ErrorResult>
}