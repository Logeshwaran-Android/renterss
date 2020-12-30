package com.rent.renters.app.ui.userHomePage


import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rent.renters.R
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.myBookings.BookingsFragment
import com.rent.renters.app.ui.inbox.TravellingInboxFragment
import com.rent.renters.app.ui.profile.ProfileFragment
import com.rent.renters.app.ui.savedProperty.SavedFragment
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util

class HomeActivity : BaseActivity(),BottomDialogButtonInterface,Util.BottomSuccessClickListener {


    private lateinit var navView : BottomNavigationView
    lateinit var mListener : BottomDialogButtonInterface
    private lateinit var mSessionManager : SessionManager
    private lateinit var mSuccessListener : Util.BottomSuccessClickListener

    var mCurrentFragment: Fragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_explore -> fragment = ExploreFragment()
            R.id.navigation_saved -> fragment = SavedFragment()
            R.id.navigation_bookings -> fragment = BookingsFragment()
            R.id.navigation_inbox -> fragment = TravellingInboxFragment()
            R.id.navigation_profile -> {
                fragment = ProfileFragment()
                val bundle = Bundle()
                bundle.putString(Iconstants.TYPE, Iconstants.USER)
                fragment.arguments = bundle
            }
        }
        mCurrentFragment = fragment
        loadFragment(fragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(mCurrentFragment is SavedFragment) {
            (mCurrentFragment as SavedFragment).onActivityResult(requestCode,resultCode,data)

        }else  if(mCurrentFragment is TravellingInboxFragment) {
            (mCurrentFragment as TravellingInboxFragment).onActivityResult(requestCode,resultCode,data)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mSuccessListener = this
        mSessionManager = SessionManager(this)
        mSessionManager.setUserType(Iconstants.USER)

        mListener = this
        initView()


    }

    override fun onBackPressed() {
       // super.onBackPressed()
        if(mCurrentFragment is ExploreFragment){
            Util.showBottomSheetDialogWithButtons(this,getString(R.string.app_name),getString(R.string.exit_alert),mListener,true)
        }else{
            navView.selectedItemId = R.id.navigation_explore
            mCurrentFragment = ExploreFragment()
            loadFragment(mCurrentFragment)
        }
    }

    override fun onBottomCookieItemClick() {
        finishAffinity()
    }

    private fun initView() {
        navView = findViewById<BottomNavigationView>(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if(intent!=null){
            if(intent.hasExtra(Iconstants.IS_FROM)){
                val mText = intent.getStringExtra(Iconstants.IS_FROM)
                if(mText.equals(Iconstants.BOOKING_SUCCESS,true) || mText == Iconstants.PUSH_BOOKINGS || mText == Iconstants.BOOKING_UNSUCCESS){
                    if(mText == Iconstants.BOOKING_SUCCESS) {
                      /*  if(intent.hasExtra(Iconstants.MESSAGE))
                        Util.showSuccessDialog(this, intent.getStringExtra(Iconstants.MESSAGE)!!, mSuccessListener, Iconstants.PAYMENT_DETAILS)*/
                        navView.selectedItemId = R.id.navigation_bookings
                        val bundle = Bundle()
                        bundle.putString(Iconstants.IS_FROM, Iconstants.BOOKING_PAGE)

                        if(intent.hasExtra(Iconstants.BOOKING_NO)) {
                            val mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)
                            bundle.putString(Iconstants.BOOKING_NO, mBookingNo)
                        }
                        mCurrentFragment = BookingsFragment()
                        mCurrentFragment!!.arguments = bundle
                        loadFragment(mCurrentFragment)


                    } else if(mText == Iconstants.BOOKING_UNSUCCESS ){
                        navView.selectedItemId = R.id.navigation_bookings
                        val bundle = Bundle()
                        bundle.putString(Iconstants.IS_FROM, Iconstants.BOOKING_PAGE)
                        mCurrentFragment = BookingsFragment()
                        mCurrentFragment!!.arguments = bundle
                        loadFragment(mCurrentFragment)
                    }else {

                                navView.selectedItemId = R.id.navigation_bookings
                                val bundle = Bundle()
                                bundle.putString(Iconstants.IS_FROM, Iconstants.BOOKING_PAGE)
                                mCurrentFragment = BookingsFragment()
                                mCurrentFragment!!.arguments = bundle
                                loadFragment(mCurrentFragment)
                            }
                            }

                if(mText == Iconstants.PUSH_VERIFICATION) {
                    val bundle = Bundle()
                    bundle.putString(Iconstants.TYPE,Iconstants.PUSH_VERIFICATION)
                    mCurrentFragment = ProfileFragment()
                    mCurrentFragment!!.arguments = bundle
                    loadFragment(mCurrentFragment)
                }
            }else{
                navView.selectedItemId = R.id.navigation_explore
            }
        }else{
            navView.selectedItemId = R.id.navigation_explore
        }

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }
    override fun onSuccessClick(isFrom: String) {



    }

}
