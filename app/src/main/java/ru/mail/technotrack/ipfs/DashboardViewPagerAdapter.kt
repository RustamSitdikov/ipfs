package ru.mail.technotrack.ipfs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class DashboardViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}

