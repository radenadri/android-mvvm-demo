package com.radenadri.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radenadri.notes.R
import com.radenadri.notes.models.quotes.Result

class QuotesAdapter : RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder>() {

    private var quotes = mutableListOf<Result>()

    fun setQuotes(quotes: List<Result>) {
        this.quotes = quotes.toMutableList()
        notifyDataSetChanged()
    }

    inner class QuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAuthor = itemView.findViewById<TextView>(R.id.txt_author)
        val txtQuotes = itemView.findViewById<TextView>(R.id.txt_quotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        return QuotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_quotes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        val currentQuotes = quotes[position]

        holder.txtAuthor.text = currentQuotes.author
        holder.txtQuotes.text = currentQuotes.content
    }

    override fun getItemCount(): Int = quotes.size

}