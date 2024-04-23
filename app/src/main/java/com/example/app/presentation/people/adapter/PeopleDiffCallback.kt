package com.example.app.presentation.people.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.presentation.people.model.People

class PeopleDiffCallback : DiffUtil.ItemCallback<People>() {

    override fun areItemsTheSame(oldItem: People, newItem: People): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: People, newItem: People): Boolean {
        return oldItem == newItem
    }
}