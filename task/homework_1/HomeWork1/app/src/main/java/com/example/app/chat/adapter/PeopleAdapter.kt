package com.example.app.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.chat.model.People
import com.example.app.databinding.ItemPeopleBinding

class PeopleAdapter : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    var peopleList = listOf<People>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val person = peopleList[position]
        holder.bind(person)
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPeopleBinding.bind(itemView)

        fun bind(person: People) {
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
        }
    }
}