package com.radenadri.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.radenadri.notes.adapter.QuotesAdapter
import com.radenadri.notes.databinding.ActivitySettingsBinding
import com.radenadri.notes.interfaces.QuotesApi
import com.radenadri.notes.models.quotes.QuoteRepository
import com.radenadri.notes.models.quotes.QuoteViewModel
import com.radenadri.notes.util.Logger
import com.radenadri.notes.util.QuoteViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: QuoteViewModel
    private lateinit var adapter: QuotesAdapter

    @Inject
    @Named("Logger")
    lateinit var logger: Logger

    @Inject
    @Named("Retrofit")
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define recycler view
        adapter = QuotesAdapter()
        binding.rvQuestion.layoutManager = LinearLayoutManager(this@SettingsActivity)
        binding.rvQuestion.adapter = adapter
        binding.rvQuestion.setHasFixedSize(true)

        // Injecting Retrofit
        val quotesApi = retrofit.create(QuotesApi::class.java)

        // Injecting ViewModel
        viewModel = ViewModelProvider(
            this@SettingsActivity,
            QuoteViewModelFactory(QuoteRepository(quotesApi))
        ).get(QuoteViewModel::class.java)

        // Observe quotes
        viewModel.quotes.observe(this@SettingsActivity) {
            it.body()?.results?.let { quotes -> adapter.setQuotes(quotes) }
        }

        // Get quotes
        viewModel.getQuotes()

        // Add back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set title
        supportActionBar?.title = "Settings"
    }
}