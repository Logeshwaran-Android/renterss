package com.rent.renters.app.ui.hostHomePage


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rent.renters.R
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.inbox.HostingInboxFragment
import com.rent.renters.app.ui.listing.ListingsFragment
import com.rent.renters.app.ui.profile.ProfileFragment
import com.rent.renters.app.ui.reservations.ReservationsFragment
import com.rent.renters.app.ui.transactionHistory.TransactionHistoryFragment
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util

class HostingActivity : BaseActivity(),BottomDialogButtonInterface {

    private lateinit var navView : BottomNavigationView
    private var  mIsFrom =""
    lateinit var mListener : BottomDialogButtonInterface
    var mCurrentFragment: Fragment? = null
    lateinit var fragment: Fragment
    private lateinit var mSessionManager: SessionManager
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_inbox -> fragment = HostingInboxFragment()
            R.id.navigation_listings -> fragment = ListingsFragment()
            R.id.navigation_reservations -> fragment = ReservationsFragment()
            R.id.navigation_transaction -> fragment = TransactionHistoryFragment()
            R.id.navigation_profile -> {
                fragment = ProfileFragment()
                val bundle = Bundle()
                bundle.putString(Iconstants.TYPE, Iconstants.HOST)
                fragment.arguments = bundle
            }
        }
        mCurrentFragment = fragment
        loadFragment(mCurrentFragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)
        mListener = this
        mSessionManager = SessionManager(this)
        mSessionManager.setUserType(Iconstants.HOST)
        initView()

    }

    private fun initView() {

        navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if(intent!=null){
            if(intent.hasExtra(Iconstants.IS_FROM)){
                mIsFrom  = intent.getStringExtra(Iconstants.IS_FROM)!!
                if(mIsFrom == Iconstants.PUSH_RESERVATION){
                    navView.selectedItemId = R.id.navigation_reservations
                }
                if(mIsFrom == Iconstants.PUSH_LISTING){
                    navView.selectedItemId = R.id.navigation_listings
                }
                if(mIsFrom == Iconstants.COMPLETED_TRANSACTIONS){
                    val bundle = Bundle()
                    bundle.putString(Iconstants.TYPE,Iconstants.COMPLETED_TRANSACTIONS)
                    mCurrentFragment = TransactionHistoryFragment()
                    mCurrentFragment!!.arguments = bundle
                    loadFragment(mCurrentFragment)
                    navView.selectedItemId = R.id.navigation_transaction
                }
            }else{
                navView.selectedItemId = R.id.navigation_inbox
                mCurrentFragment = HostingInboxFragment()
                loadFragment(mCurrentFragment)
            }
        }else{
            navView.selectedItemId = R.id.navigation_inbox
            mCurrentFragment = HostingInboxFragment()
            loadFragment(mCurrentFragment)
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
    override fun onBackPressed() {
        // super.onBackPressed()
        if(mCurrentFragment is HostingInboxFragment){
            Util.showBottomSheetDialogWithButtons(this,getString(R.string.app_name),getString(R.string.exit_alert),mListener,true)
        }else{
            navView.selectedItemId = R.id.navigation_inbox
            mCurrentFragment = HostingInboxFragment()
            loadFragment(mCurrentFragment)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(mCurrentFragment is HostingInboxFragment){
            (mCurrentFragment as HostingInboxFragment).onActivityResult(requestCode,resultCode,data)
        }

    }

    override fun onBottomCookieItemClick() {
        finishAffinity()
    }

}
