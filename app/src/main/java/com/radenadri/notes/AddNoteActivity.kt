package com.radenadri.notes

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.radenadri.notes.databinding.ActivityAddNoteBinding
import com.radenadri.notes.models.Note
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note : Note
    private lateinit var oldNote : Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            // oldNote = intent.getSerializableExtra("current_note") as Note
            oldNote = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("current_note", Note::class.java) as Note
            } else {
                intent.getParcelableExtra<Note>("current_note") as Note
            }
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.content)
            isUpdate = true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etNote.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if (isUpdate) {
                    note = Note(
                        oldNote.id,
                        title,
                        content,
                        formatter.format(Date())
                    )
                } else {
                    note = Note(
                        null,
                        title,
                        content,
                        formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}