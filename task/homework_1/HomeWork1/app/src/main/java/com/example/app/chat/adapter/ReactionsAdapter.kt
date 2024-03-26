package com.example.app.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R

class ReactionsAdapter(
    private val onReactionClick: (String) -> Unit
) : RecyclerView.Adapter<ReactionsAdapter.ReactionViewHolder>() {

    var reactions = listOf<String>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emoji, parent, false)
        return ReactionViewHolder(view).apply {
            itemView.setOnClickListener {
                onReactionClick.invoke(
                    requireNotNull(reaction)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.bind(reactions[position])
    }

    override fun getItemCount(): Int {
        return reactions.size
    }

    class ReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var reaction: String? = null

        fun bind(reaction: String) {
            this.reaction = reaction
            (itemView as TextView).text = reaction
        }
    }
}