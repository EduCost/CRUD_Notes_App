package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: AlertDialog
    private lateinit var adapter: NoteViewAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase(this)

        // Configuring the Recycler View
        adapter = NoteViewAdapter(db.getAllData(), this,
            object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val noteClicked = adapter.getNote(position)
                    val intent = Intent(applicationContext, NoteActivity::class.java)
                    intent.putExtra("extraNote", noteClicked)
                    resultLauncher.launch(intent)
                }

                override fun onItemLongClick(position: Int) {
                    showDeleteDialog(position)
                }
            })
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == 1) {
                    adapter.refreshData(db.getAllData())
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                }
            }

        val layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager


        // Showing the Alert Dialog to add a new note
        binding.fabAddNote.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.dialog_transparent_bg)
            val view = layoutInflater.inflate(R.layout.add_note_dialog, null)
            builder.setView(view)
            builder.setCancelable(false)

            val etTitle = view.findViewById<EditText>(R.id.etEditTitle)
            val etContent = view.findViewById<EditText>(R.id.etEditContent)
            val btnClose = view.findViewById<ImageButton>(R.id.btnCloseDialog)
            val btnCheck = view.findViewById<ImageButton>(R.id.btnCheck)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            btnCheck.setOnClickListener {
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                if (title.isBlank() && content.isBlank()) {
                    Toast.makeText(this, "Please enter a title and content", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val addToDB = db.insertData(title, content)
                    if (addToDB == -1L) {
                        Toast.makeText(this, "Error adding note", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.refreshData(db.getAllData())
                    }
                    dialog.dismiss()
                }
            }

            dialog = builder.create()
            dialog.show()
        }
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this, R.style.dialog_rounded)
        val view = layoutInflater.inflate(R.layout.delete_note_dialog, null)
        builder.setView(view)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val dialog = builder.create()

        btnDelete.setOnClickListener {
            val note = adapter.getNote(position)
            db.deleteNote(note.id)
            adapter.refreshData(db.getAllData())
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}