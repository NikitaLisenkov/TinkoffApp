package com.example.app.presentation.people.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.databinding.ItemPeopleBinding
import com.example.app.presentation.people.model.PeopleUi

class PeopleAdapter : ListAdapter<PeopleUi, PeopleAdapter.PeopleViewHolder>(
    PeopleDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPeopleBinding.bind(itemView)

        fun bind(person: PeopleUi) {
            binding.ivPeopleAvatar.setImageResource(R.drawable.ic_darrel)
            binding.tvPeopleEmail.text = person.email
            binding.tvPeopleName.text = person.fullName
            binding.indicatorOnline.setBackgroundResource(
                if (person.isOnline) {
                    R.drawable.indicator_online
                } else {
                    R.drawable.indicator_offline
                }
            )
            Glide.with(binding.root)
                .load(person.avatarUrl)
                .into(binding.ivPeopleAvatar)
        }
    }
}