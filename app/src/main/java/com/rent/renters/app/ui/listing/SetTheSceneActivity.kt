package com.rent.renters.app.ui.listing

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.View

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_set_the_scene.*

class SetTheSceneActivity : BaseActivity(), View.OnClickListener {
    private var mLastClickTime: Long = 0

    private lateinit var listingViewModel: ListingViewModel

    internal var fragment: Fragment? = null
    private var mSpaceType = ""
    private var mPropertyType = ""
    private var mPropertySize = ""
    private var mProductName = ""
    private var mCurrency = ""
    private var mBasePrice = ""
    private var mPropId = ""
    private var mGuestCount = "0"
    private var mBedroomCount = "0"
    private var mWeekDiscount =""
    private var mMonthDiscount =""

    private var mIsFrom = ""
    private var mSpaceBannerImage =""


    private val mSpaceTypeFragment = SpaceTypeFragment()
    private val mSpaceTitleFragment = SpaceTitleFragment()
    private val mSpaceGuestFragment = SpaceGuestFragment()
    private val mSpacePriceFragment = SpacePriceFragment()
    private val mSpaceCalendarFragment = SpaceCalendarFragment()

    private var spaceTypeDetails: HashMap<String, String> = HashMap()
    private var spacePriceDetails: HashMap<String, String> = HashMap()
    private var spaceGuestDetails: HashMap<String, String> = HashMap()



    private val visibleFragment: Fragment?
        get() {
            /*val fragmentManager = supportFragmentManager
            val fragments = fragmentManager.fragments
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }*/
            var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            return fragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_the_scene)
        initViewModel()
        initView()


    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.ID)) {
                mPropId = intent?.getStringExtra(Iconstants.ID)!!
            }

            if (intent.hasExtra(Iconstants.IS_FROM))
                mIsFrom = intent.getStringExtra(Iconstants.IS_FROM)!!

        }

        when {
            mIsFrom.equals(Iconstants.IS_NEW_LISTING,true) -> {
                val bundle = Bundle()
                bundle.putString(Iconstants.ID, mPropId)
                mSpaceTitleFragment.arguments = bundle
                loadFragment(mSpaceTypeFragment)

            }
            mIsFrom.equals(Iconstants.MANAGE_CALENDAR,true) -> {
                val bundle = Bundle()
                bundle.putString(Iconstants.ID, mPropId)
                bundle.putString(Iconstants.IS_FROM, Iconstants.MANAGE_CALENDAR)
                mSpaceCalendarFragment.arguments = bundle
                loadFragment(mSpaceCalendarFragment)
                progressBar.visibility = View.GONE
                 btnBack.visibility = View.VISIBLE
                btnNext.visibility = View.GONE

            }
            mIsFrom.equals(Iconstants.EDIT_LISTING,true) -> callGetListingStepOne()
        }

        btnBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)

    }

    private fun callGetListingStepOne() {
        listingViewModel.callGetListingStepOne(mPropId).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                val bundle = Bundle()
                it.data.values?.space_type?.let {
                    bundle.putString(Iconstants.SPACE_TYPE, it)
                }
                it.data.values?.property_size?.let {
                    bundle.putString(Iconstants.SPACE_SIZE, it)
                }
                it.data.values?.property_type?.let {
                    bundle.putString(Iconstants.PROPERTY_TYPE, it)
                }
                mSpaceTypeFragment.arguments = bundle
                loadFragment(mSpaceTypeFragment)
                val bundle1 = Bundle()
                bundle1.putString(Iconstants.ID, mPropId)
                mProductName =  it.data.values?.product_name!!
                mSpaceBannerImage = it.data.values?.banner_photos!!

                mCurrency = it.data.values?.currency!!
                mBasePrice = it.data.values?.price!!
                mWeekDiscount = it.data.values?.week_disc!!
                mMonthDiscount = it.data.values?.month_disc!!

                mGuestCount = it.data.values?.guest_count!!
                mBedroomCount = it.data.values?.bedroom_count!!
            }
        })

    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(this)

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment).addToBackStack("")
                    .commit()
            return true
        }
        return false
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v.id) {
            R.id.btnBack -> onBackPressed()
            R.id.btnNext -> when (visibleFragment) {
                is SpaceTypeFragment -> {
                    if ((visibleFragment as SpaceTypeFragment).validateFields()) {
                        spaceTypeDetails = (visibleFragment as SpaceTypeFragment).getSpaceTypeDetails()
                        mSpaceType = spaceTypeDetails[Iconstants.SPACE_TYPE]!!
                        mPropertySize = spaceTypeDetails[Iconstants.SPACE_SIZE]!!
                        mPropertyType = spaceTypeDetails[Iconstants.PROPERTY_TYPE]!!

                        listingViewModel.callPostListingStepOne(mSpaceType,mPropertyType, mPropertySize, mProductName, mCurrency, mBasePrice, mPropId,mGuestCount,mBedroomCount,mWeekDiscount,mMonthDiscount).observe(this, Observer {
                            baseViewModel.rentersLoader.postValue(false)
                            val bundle1 = Bundle()
                            bundle1.putString(Iconstants.ID, mPropId)
                            if(mProductName.isNotEmpty())
                            bundle1.putString(Iconstants.SPACE_TITLE, mProductName)
                            if(mSpaceBannerImage.isNotEmpty())
                            bundle1.putString(Iconstants.SPACE_BANNER_IMAGE, mSpaceBannerImage)
                            mSpaceTitleFragment.arguments = bundle1
                            loadFragment(mSpaceTitleFragment)
                            progressBar.progress = 20
                            if (it.status == "1") {
                                setResult(RESULT_OK)
                            }
                        })
                    }
                }
                is SpaceTitleFragment -> {
                    if ((visibleFragment as SpaceTitleFragment).validateFields()) {
                        mProductName = (visibleFragment as SpaceTitleFragment).getSpaceName()
                        mSpaceBannerImage = (visibleFragment as SpaceTitleFragment).getBannerImage()
                        listingViewModel.callPostListingStepOne(mSpaceType,mPropertyType, mPropertySize, mProductName, mCurrency, mBasePrice, mPropId,mGuestCount,mBedroomCount,mWeekDiscount,mMonthDiscount).observe(this, Observer {
                            baseViewModel.rentersLoader.postValue(false)
                            val bundle = Bundle()
                            bundle.putString(Iconstants.GUEST_COUNT, mGuestCount)
                            bundle.putString(Iconstants.BEDROOM_COUNT, mBedroomCount)
                            mSpaceGuestFragment.arguments = bundle
                            loadFragment(mSpaceGuestFragment)
                            progressBar.progress = 40
                            if (it.status == "1") {
                                setResult(RESULT_OK)
                            }
                        })
                    }
                }
                is SpaceGuestFragment ->{
                    spaceGuestDetails = (visibleFragment as SpaceGuestFragment).getGuestDetails()
                    mGuestCount = spaceGuestDetails[Iconstants.GUEST_COUNT]!!
                    mBedroomCount = spaceGuestDetails[Iconstants.BEDROOM_COUNT]!!
                    listingViewModel.callPostListingStepOne(mSpaceType,mPropertyType, mPropertySize, mProductName, mCurrency, mBasePrice, mPropId,mGuestCount,mBedroomCount,mWeekDiscount,mMonthDiscount).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        val bundle2 = Bundle()
                        bundle2.putString(Iconstants.CURRENCY, mCurrency)
                        bundle2.putString(Iconstants.SPACE_PRICE, mBasePrice)
                        bundle2.putString(Iconstants.WEEKLY_DISCOUNT, mWeekDiscount)
                        bundle2.putString(Iconstants.MONTHLY_DISCOUNT, mMonthDiscount)
                        mSpacePriceFragment.arguments = bundle2
                        loadFragment(mSpacePriceFragment)
                        progressBar.progress = 60
                        if (it.status == "1") {
                            setResult(RESULT_OK)
                        }
                    })

                }
                is SpacePriceFragment -> {
                    // if((visibleFragment as SpacePriceFragment).validateFields()) {

                    spacePriceDetails = (visibleFragment as SpacePriceFragment).getPricingDetails()
                    mCurrency = spacePriceDetails[Iconstants.CURRENCY]!!
                    mBasePrice = spacePriceDetails[Iconstants.SPACE_PRICE]!!
                    mWeekDiscount = spacePriceDetails[Iconstants.WEEKLY_DISCOUNT]!!
                    mMonthDiscount = spacePriceDetails[Iconstants.MONTHLY_DISCOUNT]!!

                    listingViewModel.callPostListingStepOne(mSpaceType,mPropertyType, mPropertySize, mProductName, mCurrency, mBasePrice, mPropId,mGuestCount,mBedroomCount,mWeekDiscount,mMonthDiscount).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        val bundle = Bundle()
                        bundle.putString(Iconstants.ID, mPropId)
                        bundle.putString(Iconstants.IS_FROM,Iconstants.LISTING)
                        mSpaceCalendarFragment.arguments = bundle
                        loadFragment(mSpaceCalendarFragment)
                        progressBar.progress = 80
                        if (it.status == "1") {
                            setResult(RESULT_OK)
                        }
                    })
                    // }
                }
                is SpaceCalendarFragment -> {
                    finish()

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (visibleFragment is SpaceTitleFragment) {
                (visibleFragment as SpaceTitleFragment).onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onBackPressed() {
        //  super.onBackPressed()
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        when (visibleFragment) {
            is SpaceTypeFragment -> {
                finish()

            }
            is SpaceTitleFragment -> {
                //loadFragment(mSpaceTypeFragment)
                supportFragmentManager.popBackStack()
            }
            is SpaceGuestFragment ->{
                supportFragmentManager.popBackStack()
                progressBar.progress = 20
            }
            is SpacePriceFragment -> {
                spacePriceDetails = (visibleFragment as SpacePriceFragment).getPricingDetails()
                mCurrency = spacePriceDetails[Iconstants.CURRENCY]!!
                mBasePrice = spacePriceDetails[Iconstants.SPACE_PRICE]!!
                mWeekDiscount = spacePriceDetails[Iconstants.WEEKLY_DISCOUNT]!!
                mMonthDiscount = spacePriceDetails[Iconstants.MONTHLY_DISCOUNT]!!
                //loadFragment(mSpaceTitleFragment)
                supportFragmentManager.popBackStack()
                progressBar.progress = 40
            }
            is SpaceCalendarFragment -> {
                if(mIsFrom.equals(Iconstants.MANAGE_CALENDAR,true)){
                    finish()
                }else {

                    //loadFragment(mSpacePriceFragment)
                    supportFragmentManager.popBackStack()
                    progressBar.progress = 60
                }
            }
        }
    }
}
