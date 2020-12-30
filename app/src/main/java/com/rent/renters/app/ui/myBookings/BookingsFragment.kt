package com.rent.renters.app.ui.myBookings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.inbox.ViewPagerAdapter

import kotlinx.android.synthetic.main.fragment_inbox.*
import kotlinx.android.synthetic.main.header_layout.*
import java.util.ArrayList




class BookingsFragment : Fragment(),  TabLayout.OnTabSelectedListener {



    private val fragmentList =  ArrayList<Fragment>()
    private val mPageTitle: ArrayList<String> = ArrayList()
    private lateinit var mPagerAdapter: ViewPagerAdapter
    private val mTotalBookingsFragment : TotalBookingsFragment = TotalBookingsFragment()
    private val mCompletedBookingFragment : CompletedBookingFragment = CompletedBookingFragment()
    private val mExpiredBookingsFragment : ExpiredBookingsFragment = ExpiredBookingsFragment()
    private val mPendingApprovalBookingsFragment : PendingApprovalBookingsFragment = PendingApprovalBookingsFragment()
    private val mPaymentPendingBookingsFragment : PendingPaymentBookingsFragment = PendingPaymentBookingsFragment()
    private val mUpcomingCheckinBookingsFragment : UpcomingCheckinBookingsFragment = UpcomingCheckinBookingsFragment()
    private val mDeclinedBookingsFragment : DeclinedBookingsFragment = DeclinedBookingsFragment()
    private val mCancelledBookingsFragment : CancelledBookingFragment = CancelledBookingFragment()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        customizeToolbar()

    }

    private fun customizeToolbar() {
        imgBtnBack.visibility = View.GONE
        tvTitle.text = getString(R.string.my_bookings)
    }


    private fun initView() {
        if(arguments!=null){
            if(arguments?.containsKey(Iconstants.IS_FROM)!!){
                val mIsFrom = arguments?.getString(Iconstants.IS_FROM)
                if(arguments?.containsKey(Iconstants.BOOKING_NO)!!) {
                    val mBookingNo = arguments?.getString(Iconstants.BOOKING_NO)
                    if(mIsFrom == Iconstants.BOOKING_PAGE && mBookingNo!!.isNotEmpty()){
                        val bundle = Bundle()
                        bundle.putString(Iconstants.IS_FROM,Iconstants.BOOKING_PAGE)
                        bundle.putString(Iconstants.BOOKING_NO,mBookingNo)
                        mTotalBookingsFragment.arguments = bundle

                    }
                }


            }

        }

        mPageTitle.add((getString(R.string.total)))
        mPageTitle.add((getString(R.string.completed)))
        mPageTitle.add((getString(R.string.expired)))
        mPageTitle.add((getString(R.string.pending_host_approval)))
        mPageTitle.add((getString(R.string.pending_payment)))
        mPageTitle.add((getString(R.string.upcoming_checkin)))
        mPageTitle.add((getString(R.string.declined)))
        mPageTitle.add((getString(R.string.cancelled)))
        fragmentList.add(mTotalBookingsFragment)
        fragmentList.add(mCompletedBookingFragment)
        fragmentList.add(mExpiredBookingsFragment)
        fragmentList.add(mPendingApprovalBookingsFragment)
        fragmentList.add(mPaymentPendingBookingsFragment)
        fragmentList.add(mUpcomingCheckinBookingsFragment)
        fragmentList.add(mDeclinedBookingsFragment)
        fragmentList.add(mCancelledBookingsFragment)
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.total)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.completed)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.expired)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending_host_approval)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending_payment)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming_checkin)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.declined)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cancelled)))


        mPagerAdapter = ViewPagerAdapter(childFragmentManager, fragmentList, mPageTitle)

        //Adding adapter to pager
        viewPager.adapter = mPagerAdapter

        viewPager.offscreenPageLimit = fragmentList.size
        tabLayout.setupWithViewPager(viewPager)

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 40, 0)
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