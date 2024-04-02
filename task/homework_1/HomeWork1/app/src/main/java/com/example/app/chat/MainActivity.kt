package com.example.app.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.channels.ChannelsFragment
import com.example.app.databinding.ActivityMainBinding
import com.example.app.people.PeopleFragment
import com.example.app.profile.ProfileFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.channelsFragment -> {
                    replaceFragment(ChannelsFragment())
                    true
                }

                R.id.peopleFragment -> {
                    replaceFragment(PeopleFragment())
                    true
                }

                R.id.profileFragment -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.channelsFragment

        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            binding.bottomNavigationView.isGone = fragment is ChatFragment
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}