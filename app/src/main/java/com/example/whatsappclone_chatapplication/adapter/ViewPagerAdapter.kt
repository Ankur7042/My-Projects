package com.example.whatsappclone_chatapplication.adapter

import android.app.Fragment
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    private val context: Context,
    fm: FragmentManager?,
    private val list: ArrayList<androidx.fragment.app.Fragment>,
) : FragmentPagerAdapter(fm!!) {
//Deprecated
    override fun getCount(): Int {
       return 3
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    companion object{
        val TAB_TITLES = arrayOf("Chats","Status","Call")
    }
}