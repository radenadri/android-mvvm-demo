package com.radenadri.notes.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radenadri.notes.R
import com.radenadri.notes.models.quotes.Result

class QuotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var quotes = mutableListOf<Result>()

    private var isLoading = false

    companion object {
        private const val VIEW_LOADING = 0
        private const val VIEW_QUOTES = 1
    }

    fun setQuotes(quotes: List<Result>) {
        this.quotes = quotes.toMutableList()
        notifyDataSetChanged()
    }

    inner class QuotesViewHolder(itemView: View) : RecyclerView
    .ViewHolder
        (itemView) {
        val txtAuthor = itemView.findViewById<TextView>(R.id.txt_author)
        val txtQuotes = itemView.findViewById<TextView>(R.id.txt_quotes)
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_LOADING) {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_data_loading, parent, false)
            )
        } else {
            return QuotesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_quotes, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is QuotesViewHolder) {
            holder.txtAuthor.text = quotes[position].author
            holder.txtQuotes.text = quotes[position].content
        } else if (holder is LoadingViewHolder) {
            Log.d("QuotesAdapter", "onBindViewHolder: $isLoading")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == quotes.size && isLoading) VIEW_LOADING else VIEW_QUOTES
    }

    override fun getItemCount(): Int = quotes.size + if (isLoading) 1 else 0

    fun showLoading() {
        isLoading = true
        notifyItemInserted(quotes.size)
        Log.d("QuotesAdapter", "showLoading: $isLoading")
    }

    fun hideLoading() {
        isLoading = false
        notifyItemRemoved(quotes.size)
        Log.d("QuotesAdapter", "hideLoading: $isLoading")
    }

}