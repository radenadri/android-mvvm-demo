package com.radenadri.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.radenadri.notes.database.NoteDatabase
import com.radenadri.notes.database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository

    val allNotes : LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes

        /* val userData = fillData()
        val data = MutableLiveData<List<Note>>()
        data.value = userData
        allNotes = data */
    }

    /* fun fillData() : List<Note> {
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
    } */

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