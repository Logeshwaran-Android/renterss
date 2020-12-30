package com.rent.renters.app.ui.inbox


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class ViewPagerAdapter//Constructor to the class
(fm: FragmentManager, //integer to count number of tabs
 private var fragmentList: ArrayList<Fragment>?,
 private var mPageTitle: ArrayList<String>?)//Initializing tab count
    : FragmentStatePagerAdapter(fm) {

    //Overriding method getItem
    override fun getItem(position: Int): Fragment {
        //Returning the current tabs
        return when (position) {
            0 -> {
                fragmentList!![0]
            }
            1 -> {
                fragmentList!![1]
            }
            2 ->{
                fragmentList!![2]
            }
            3 ->{
                fragmentList!![3]
            }
            4->{
                fragmentList!![4]
            }
            5->{
                fragmentList!![5]
            }
            6->{
                fragmentList!![6]
            }
            7->{
                fragmentList!![7]
            }
            else -> fragmentList!![0]
        }
    }

    //Overriden method getCount to get the number of tabs
    override fun getCount(): Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mPageTitle?.get(position)
    }
}
