package com.radenadri.notes.models.quotes

import android.util.Log
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

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)

    val loading: LiveData<Boolean>

    get() = _loading

    fun getQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofitService.getQuotes()
            _quotes.postValue(response)
        }
    }

    // set loading to true
    fun showLoading() {
        _loading.value = true
        Log.d("QuoteViewModel", "showLoading: ${loading.value}")
    }

    // set loading to false
    fun hideLoading() {
        _loading.value = false
        Log.d("QuoteViewModel", "hideLoading: ${loading.value}")
    }
}