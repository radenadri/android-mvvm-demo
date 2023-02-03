package com.radenadri.notes.models.quotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class QuoteViewModel(private val retrofitService: QuoteRepository) : ViewModel() {

    private val _quotes = MutableLiveData<Response<QuoteList>>()

    val quotes: LiveData<Response<QuoteList>>

    get() = _quotes

    fun getQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofitService.getQuotes()
            _quotes.postValue(response)
        }
    }
}