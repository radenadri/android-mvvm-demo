package com.radenadri.notes.util

import com.radenadri.notes.models.quotes.QuoteRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.radenadri.notes.models.quotes.QuoteViewModel

class QuoteViewModelFactory constructor(private val repository: QuoteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            QuoteViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}