package com.rent.renters.app.ui.reservations



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

import com.rent.renters.R
import com.rent.renters.app.ui.inbox.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_reservations.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ReservationsFragment : Fragment(),TabLayout.OnTabSelectedListener{

    private val fragmentList =  ArrayList<Fragment>()
    private val mPageTitle: ArrayList<String> = ArrayList()



    private lateinit var mPagerAdapter: ViewPagerAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {


        imgBtnBack.visibility = View.GONE
        tvTitle.text = getString(R.string.my_reservation)

        mPageTitle.add((getString(R.string.total)))
        mPageTitle.add((getString(R.string.completed)))
        mPageTitle.add((getString(R.string.expired)))
        mPageTitle.add((getString(R.string.pending_host_approval)))
        mPageTitle.add((getString(R.string.pending_payment)))
        mPageTitle.add((getString(R.string.upcoming_checkin)))
        mPageTitle.add((getString(R.string.declined)))
        mPageTitle.add((getString(R.string.cancelled)))
        fragmentList.add(TotalReservationsFragment())
        fragmentList.add(CompletedReservationsFragment())
        fragmentList.add(ExpiredReservationsFragment())
        fragmentList.add(PendingApprovalReservationFragment())
        fragmentList.add(PendingPaymentReservationFragment())
        fragmentList.add(UpcomingCheckinReservationFragment())
        fragmentList.add(DeclinedReservationFragment())
        fragmentList.add(CancelledReservationFragement())
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.total)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.completed)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.expired)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending_host_approval)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending_payment)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming_checkin)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.declined)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cancelled)))


        mPagerAdapter = ViewPagerAdapter(childFragmentManager, fragmentList,mPageTitle )

        //Adding adapter to pager
        viewPager.adapter = mPagerAdapter

        viewPager.offscreenPageLimit = fragmentList.size

        tabLayout.setupWithViewPager(viewPager)

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this)



    }


    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {

    }

    override fun onTabReselected(tab: TabLayout.Tab) {

    }





}
