package com.example.notesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int)
}

class NoteViewAdapter(
    private var notes: MutableList<Note>,
    context: Context,
    private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NoteViewAdapter.NoteViewHolder>(){

    private val db = AppDatabase(context)

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                listener.onItemLongClick(adapterPosition)
                true
            }
        }
        val title = view.findViewById<TextView>(R.id.tvNoteTitle)
        val content = view.findViewById<TextView>(R.id.tvNoteContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content
        }

    fun refreshData(newNotes: MutableList<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
