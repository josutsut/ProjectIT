package com.example.projectit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.projectit.R
import com.example.projectit.fragment.GroupFragment
import com.example.projectit.fragment.InvitationFragment
import com.example.projectit.fragment.PlayFragment

class SectionPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return GroupFragment()
            1 -> return InvitationFragment()
            2 -> return PlayFragment()
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "Group"
            1 -> return "Invitation"
            2 -> return "Play"
        }
        return null!!
    }

}