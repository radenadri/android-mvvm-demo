package com.radenadri.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.radenadri.notes.databinding.ActivitySettingsBinding
import com.radenadri.notes.interfaces.QuotesApi
import com.radenadri.notes.util.Logger
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

        // Injecting Retrofit
        val quotesApi = retrofit.create(QuotesApi::class.java)

        // Create a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val result = quotesApi.getQuotes()
            logger.log(result.body()?.results.toString())
        }

        // Add back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set title
        supportActionBar?.title = "Settings"
    }
}