package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteViewAdapter(private var notes: MutableList<Note>) : RecyclerView.Adapter<NoteViewAdapter.NoteViewHolder>(){

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.tvNoteTitle)
        val content = view.findViewById<TextView>(R.id.tvNoteContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.content.text = notes[position].content
    }

    fun refreshData(newNotes: MutableList<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}