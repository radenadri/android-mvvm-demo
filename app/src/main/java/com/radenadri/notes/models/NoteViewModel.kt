package com.radenadri.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.radenadri.notes.database.NoteDatabase
import com.radenadri.notes.database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository

    val allNotes : LiveData<List<Note>>

    init {
        // Fill the database with dummy data
        /* val userData = List<Note> {
            return listOf<Note>(
                Note(
                    1,
                    "Title 1",
                    "Content 1",
                    "Date 1"
                ),
                Note(
                    2,
                    "Title 2",
                    "Content 2",
                    "Date 2"
                ),
            )
        }
        val data = MutableLiveData<List<Note>>()
        data.value = userData
        allNotes = data */

        // Get the database and the dao
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
}