package com.example.medibank.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // # Headline Fragment
                val bundle = Bundle()
                bundle.putString("fragmentName", "Headline Fragment")
                val headlineFragment = HeadlinesFragment()
                headlineFragment.arguments = bundle
                return headlineFragment
            }
            1 -> {
                // # Movies Fragment
                val bundle = Bundle()
                bundle.putString("fragmentName", "Sources Fragment")
                val sourcesFragment = SourcesFragment()
                sourcesFragment.arguments = bundle
                return sourcesFragment
            }
            2 -> {
                // # Books Fragment
                val bundle = Bundle()
                bundle.putString("fragmentName", "Saved Fragment")
                val savedFragment = SavedFragment()
                savedFragment.arguments = bundle
                return savedFragment
            }
            else -> return BaseFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}