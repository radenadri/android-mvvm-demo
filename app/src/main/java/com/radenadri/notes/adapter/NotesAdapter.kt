package com.radenadri.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.radenadri.notes.R
import com.radenadri.notes.models.Note
import kotlin.random.Random

class NotesAdapter(private val context : Context, val listener: NotesClickListener) : RecyclerView
.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]

        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.content.text = currentNote.content

        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notesLayout.setCardBackgroundColor(holder.itemView.resources.getColor(RandomColor
            (), null))

        holder.notesLayout.setOnClickListener() {
            listener.onItemClicked(currentNote)
        }

        holder.notesLayout.setOnLongClickListener() {
            listener.onItemLongClicked(currentNote, holder.notesLayout)
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateList(newList: List<Note>) {
        notesList.clear()
        notesList.addAll(newList)
        fullList.clear()
        fullList.addAll(newList)

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        notesList.clear()
        for (note in fullList) {
            if (
                note.title?.lowercase()?.contains(search.lowercase()) == true ||
                note.content?.lowercase()?.contains(search.lowercase()) == true
            ) {
                notesList.add(note)
            }
        }

        notifyDataSetChanged()
    }

    fun RandomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)

        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesLayout = itemView.findViewById<CardView>(R.id.cardLayout)
        val title = itemView.findViewById<TextView>(R.id.etTitle)
        val content = itemView.findViewById<TextView>(R.id.etNote)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
    }

    interface NotesClickListener {

        fun onItemClicked(note: Note)
        fun onItemLongClicked(note: Note, cardView: CardView)
    }
}