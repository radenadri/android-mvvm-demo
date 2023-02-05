package com.radenadri.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: QuoteViewModel
    private lateinit var adapter: QuotesAdapter

    private var page = 1

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
        binding.rvQuestion.itemAnimator = null

        // Injecting Retrofit
        val quotesApi = retrofit.create(QuotesApi::class.java)

        // Injecting ViewModel
        viewModel = ViewModelProvider(
            this@SettingsActivity,
            QuoteViewModelFactory(QuoteRepository(quotesApi))
        ).get(QuoteViewModel::class.java)

        // Observe message
        viewModel.message.observe(this@SettingsActivity) {
            if (it != null) {
                binding.rvSpinner.visibility = View.GONE
                Toast.makeText(this@SettingsActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe quotes
        viewModel.quotes.observe(this@SettingsActivity) {
            if (it != null) {
                it.results?.let { quotes ->
                    adapter.setQuotes(quotes)
                    binding.rvSpinner.visibility = View.GONE
                }
            }
        }

        // observe loading
        viewModel.loading.observe(this@SettingsActivity) {
            if (it) {
                // Show loading
                adapter.showLoading()
            } else {
                // Hide loading
                adapter.hideLoading()
            }
        }

        // Add on scroll listener to recycler view
        binding.rvQuestion.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Check if scroll is at the end
                if (!recyclerView.canScrollVertically(1)) {
                    logger.log("End of recycler view")

                    // Get quotes
                     CoroutineScope(Dispatchers.Main).launch {
                         // viewModel.getQuotes(page)
                         // disable loading after 3 seconds
                         delay(3000)
                         viewModel.hideLoading()
                     }
                    viewModel.showLoading()

                    // Increment page
                    page++
                }
            }
        })

        // Get quotes
        viewModel.getQuotes()

        // Add back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set title
        supportActionBar?.title = "Settings"
    }
}