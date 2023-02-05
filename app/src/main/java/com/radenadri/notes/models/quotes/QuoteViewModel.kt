package com.radenadri.notes.models.quotes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class QuoteViewModel(private val retrofitService: QuoteRepository) : ViewModel() {

    // Quotes
    private var _quotes = MutableLiveData<QuoteList?>()
    val quotes: LiveData<QuoteList?>
    get() = _quotes

    // Loading
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
    get() = _loading

    // Notification message
    private val _message = MutableLiveData<String>()
    val message : LiveData<String>
    get() = _message

    fun getQuotes(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = retrofitService.getQuotes(page)) {
                is NetworkResponse.Success -> {
                    if (response.body.results.size == 0) {
                        _message.postValue("No more quotes")
                        return@launch
                    }

                    // Add new quotes to old quotes
                    var currentQuotes = _quotes.value

                    // If currentQuotes is null, set currentQuotes to response.body
                    // Else, set currentQuotes to currentQuotes + response.body
                    if (currentQuotes == null) {
                        currentQuotes = response.body
                    } else {
                        currentQuotes = currentQuotes.copy(
                            results = currentQuotes.results + response.body.results
                        )
                    }

                    // Set _quotes to currentQuotes
                    _quotes.postValue(currentQuotes)

                }
                is NetworkResponse.ServerError -> {
                    _message.postValue("Server Error")
                }
                is NetworkResponse.NetworkError -> {
                    _message.postValue("Network Error")
                }
                is NetworkResponse.UnknownError -> {
                    _message.postValue("Unknown Error")
                }
            }
        }
    }

    // set loading to true
    fun showLoading() {
        _loading.value = true
    }

    // set loading to false
    fun hideLoading() {
        _loading.value = false
    }
}