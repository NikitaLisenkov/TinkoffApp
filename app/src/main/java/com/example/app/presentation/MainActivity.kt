package com.example.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.databinding.ActivityMainBinding
import com.example.app.presentation.base.Hideable
import com.example.app.presentation.channels.ChannelsFragment
import com.example.app.presentation.people.PeopleFragment
import com.example.app.presentation.profile.ProfileFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            if (it.itemId == binding.bottomNavigationView.selectedItemId && savedInstanceState != null) {
                return@setOnItemSelectedListener true
            }

            if (currentFragment is Hideable) return@setOnItemSelectedListener true

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

        supportFragmentManager.addOnBackStackChangedListener {
            hideNavigationView()
        }

        binding.bottomNavigationView.selectedItemId = savedInstanceState?.getInt(
            ARG_ITEM_ID,
            R.id.channelsFragment
        )
            ?: R.id.channelsFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_ITEM_ID, binding.bottomNavigationView.selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hideNavigationView()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    private fun hideNavigationView() {
        binding.bottomNavigationView.isGone = currentFragment is Hideable
    }

    private companion object {
        private const val ARG_ITEM_ID: String = "ARG_ITEM_ID"
    }
}