package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase(this)
        val i = intent

        val note = i.getSerializableExtra("extraNote") as Note
        binding.etEditTitle.setText(note.title)
        binding.etEditContent.setText(note.content)

        binding.ivSaveChanges.setOnClickListener {
            val noteTitle = binding.etEditTitle.text.toString()
            val noteContent = binding.etEditContent.text.toString()
            if (noteTitle == note.title && noteContent == note.content) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
            } else {
                db.updateNote(Note(note.id, noteTitle, noteContent))
                setResult(1)
                finish()
            }
        }

        binding.ivBack.setOnClickListener {
            setResult(0)
            finish()
        }
    }
}