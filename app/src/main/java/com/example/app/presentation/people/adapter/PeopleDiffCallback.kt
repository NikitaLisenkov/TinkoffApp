package com.example.app.presentation.people.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.presentation.people.model.PeopleUi

class PeopleDiffCallback : DiffUtil.ItemCallback<PeopleUi>() {

    override fun areItemsTheSame(oldItem: PeopleUi, newItem: PeopleUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PeopleUi, newItem: PeopleUi): Boolean {
        return oldItem == newItem
    }
}