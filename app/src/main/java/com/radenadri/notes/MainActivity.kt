package com.radenadri.notes

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.radenadri.notes.adapter.NotesAdapter
import com.radenadri.notes.database.NoteDatabase
import com.radenadri.notes.databinding.ActivityMainBinding
import com.radenadri.notes.interfaces.QuotesApi
import com.radenadri.notes.models.Note
import com.radenadri.notes.models.NoteViewModel
import com.radenadri.notes.util.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note

    @Inject
    @Named("Logger")
    lateinit var logger: Logger

    @Inject
    @Named("Retrofit")
    lateinit var retrofit: Retrofit

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // val note = it.data?.getSerializableExtra("note") as? Note
            val note = if (Build.VERSION.SDK_INT >= 33) {
                it.data?.getParcelableExtra("note", Note::class.java)
            } else {
                it.data?.getParcelableExtra<Note>("note")
            }
            if (note != null) {
                viewModel.updateNote(note)
            }
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // val note = it.data?.getSerializableExtra("note") as? Note
                val note = if (Build.VERSION.SDK_INT >= 33) {
                    it.data?.getParcelableExtra("note", Note::class.java)
                } else {
                    it.data?.getParcelableExtra<Note>("note")
                }

                if (note != null) {
                    viewModel.insertNote(note)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Injecting Logger
        logger.log("Hello from MainActivity")

        // Injecting Retrofit
        val quotesApi = retrofit.create(QuotesApi::class.java)

        // Create a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val result = quotesApi.getQuotes()
            logger.log(result.body().toString())
        }

        // Initialize the UI
        initUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { notes ->
            notes?.let {
                adapter.updateList(it)
            }
        }

        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayout.VERTICAL
        )
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }

                return true
            }
        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onItemLongClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this@MainActivity)
        popupMenu.menuInflater.inflate(R.menu.pop_up_menu, popupMenu.menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> {
                viewModel.deleteNote(selectedNote)
                return true
            }
            else -> return false
        }
    }
}
