package com.rent.renters.app.ui.listing


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
import kotlinx.android.synthetic.main.activity_listing_space_guest.*
import kotlinx.android.synthetic.main.activity_space_guest.btnSave


class SpaceListingGuestActivity : BaseActivity(), View.OnClickListener {

    private var mLastClickTime: Long = 0
    private lateinit var listingViewModel: ListingViewModel

    private var mSpaceAmenitiesFragment =  SpaceAmenitiesFragment()
    private var mSpaceEnhanceSearchFragment =  SpaceEnhanceSearchFragment()
    private var mSpaceGuestCheckInFragment =  SpaceGuestCheckinFragment()
    private var mSpaceCancellationDetailsFragment =  SpaceCancellationDetailsFragment()

    private var mCancellationPolicy = ""
    private var mCancellationRules = ""
    private var mMetaDescription = ""
    private var mMetaKeyword = ""
    private var mInstantBooking = ""
    private var mMetaTitle = ""
    private var mAmenitiesId = ""
    private var mPropId = "0"
    private var mIsFrom = ""
    private var mCheckInFromTime =""
    private var mCheckInToTime =""
    private var mCheckOutTime =""
    private var mMinStay =""
    private var mMaxStay =""


    private var guestCheckInDetails : HashMap<String, String> = HashMap()
    private var spaceEnhanceSearchDetails : HashMap<String, String> = HashMap()
    private var spaceCancellationDetails : HashMap<String, String> = HashMap()

    private val visibleFragment: Fragment?
        get() {
            /*  val fragmentManager = supportFragmentManager
              val fragments = fragmentManager.fragments
              for (fragment in fragments) {
                  if (fragment != null && fragment.isVisible)
                      return fragment
              }
              return null*/
            var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            return fragment
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_space_guest)
        initViewModel()
        initView()
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID)){
                mPropId = intent?.getStringExtra(Iconstants.ID)!!
            }
            if(intent.hasExtra(Iconstants.IS_FROM))
                mIsFrom = intent.getStringExtra(Iconstants.IS_FROM)!!
        }

        if(mIsFrom.equals(Iconstants.IS_NEW_LISTING,true)) {
            val bundle = Bundle()
            bundle.putString(Iconstants.ID,mPropId)
            mSpaceAmenitiesFragment.arguments = bundle
            btnBack.text = getString(R.string.step_two)
            loadFragment(mSpaceAmenitiesFragment)
        }
        else if(mIsFrom.equals(Iconstants.EDIT_LISTING,true)){
            callGetListingStepThree()
        }

        btnBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnGoBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)

    }

    private fun callGetListingStepThree() {
        listingViewModel.callGetListingStepThree(mPropId).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                val bundle = Bundle()
                it.data.property_data?.let {
                    bundle.putStringArrayList(Iconstants.AMENITIES_LIST, it.amenities_id!!)
                }
                bundle.putString(Iconstants.ID,mPropId)
                mSpaceAmenitiesFragment.arguments = bundle
                btnBack.text = getString(R.string.step_two)
                loadFragment(mSpaceAmenitiesFragment)
                val bundle1 = Bundle()
                bundle1.putString(Iconstants.MINIMUM_STAY,it.data.property_data?.min_stay!!)
                bundle1.putString(Iconstants.MAXIMUM_STAY,it.data.property_data?.max_stay!!)
                bundle1.putString(Iconstants.META_TITLE,it.data.property_data?.meta_title!!)
                bundle1.putString(Iconstants.META_DESCRIPTION,it.data.property_data?.meta_description!!)
                bundle1.putString(Iconstants.META_KEYWORD,it.data.property_data?.meta_keywords!!)
                mSpaceEnhanceSearchFragment.arguments = bundle1

                val bundle2 = Bundle()
                bundle2.putString(Iconstants.CANCELLATION_POLICY,it.data.property_data?.cancellation_policy!!)
                bundle2.putString(Iconstants.CANCELLATION_RULES,it.data.property_data?.cancellation_rules!!)
                bundle2.putString(Iconstants.INSTANCE_BOOK,it.data.property_data?.instant_booking!!)
                mSpaceCancellationDetailsFragment.arguments = bundle2

                val bundle3 = Bundle()
                bundle3.putString(Iconstants.CHECK_IN_FROM_TIME,it.data.property_data?.guest_check_in_form!!)
                bundle3.putString(Iconstants.CHECK_IN_TO_TIME,it.data.property_data?.guest_check_in_to!!)
                bundle3.putString(Iconstants.CHECK_OUT_TIME,it.data.property_data?.check_out_time!!)
                mSpaceGuestCheckInFragment.arguments = bundle3


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

            R.id.btnBack -> {
                btnBack.visibility = View.VISIBLE
                btnNext.visibility = View.VISIBLE
                btnSave.visibility = View.GONE
                btnGoBack.visibility = View.GONE
                if (visibleFragment is SpaceAmenitiesFragment) {
                    val spacePhotoIntent = Intent(this, SpaceLookUpsActivity::class.java)
                    spacePhotoIntent.putExtra(Iconstants.ID,mPropId)
                    spacePhotoIntent.putExtra(Iconstants.IS_FROM,Iconstants.EDIT_LISTING)
                    startActivityForResult(spacePhotoIntent, Iconstants.SET_LOOK_UPS_REQUEST_CODE)
                    finish()
                }
                else if (visibleFragment is SpaceEnhanceSearchFragment) {
                    val bundle = Bundle()
                    bundle.putString(Iconstants.ID,mPropId)
                    mSpaceAmenitiesFragment.arguments = bundle
                    btnBack.text = getString(R.string.step_two)
                    // loadFragment(mSpaceAmenitiesFragment)
                    supportFragmentManager.popBackStack()
                }
                else if(visibleFragment is SpaceGuestCheckinFragment){
                    //loadFragment(mSpaceEnhanceSearchFragment)
                    supportFragmentManager.popBackStack()
                    progressBar.progress = 25
                }else if(visibleFragment is SpaceCancellationDetailsFragment){
                    //loadFragment(mSpaceEnhanceSearchFragment)
                    supportFragmentManager.popBackStack()
                    progressBar.progress = 50
                }
            }
            R.id.btnNext -> if (visibleFragment is SpaceAmenitiesFragment) {

                btnBack.text = getString(R.string.back)
                mAmenitiesId = (visibleFragment as SpaceAmenitiesFragment).getSelectedAmenitiesID()

                listingViewModel.callPostListingStepThree(mPropId,mAmenitiesId,mMetaTitle,mMetaKeyword,mMetaDescription,mInstantBooking,mCancellationPolicy, mCancellationRules,mCheckInFromTime,mCheckInToTime,mCheckOutTime,mMinStay,mMaxStay).observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    loadFragment(mSpaceEnhanceSearchFragment)
                    progressBar.progress = 25
                    if(it.status == "1"){
                        setResult(RESULT_OK)
                    }
                })
            } else if (visibleFragment is SpaceEnhanceSearchFragment) {

                spaceEnhanceSearchDetails = (visibleFragment as SpaceEnhanceSearchFragment).getEnhanceSearchDetails()
                mMetaTitle = spaceEnhanceSearchDetails[Iconstants.META_TITLE]!!
                mMetaKeyword = spaceEnhanceSearchDetails[Iconstants.META_KEYWORD]!!
                mMetaDescription = spaceEnhanceSearchDetails[Iconstants.META_DESCRIPTION]!!
                mMinStay = spaceEnhanceSearchDetails[Iconstants.MINIMUM_STAY]!!
                mMaxStay = spaceEnhanceSearchDetails[Iconstants.MAXIMUM_STAY]!!

                listingViewModel.callPostListingStepThree(mPropId, mAmenitiesId, mMetaTitle, mMetaKeyword, mMetaDescription, mInstantBooking, mCancellationPolicy, mCancellationRules,mCheckInFromTime,mCheckInToTime,mCheckOutTime,mMinStay,mMaxStay).observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    loadFragment(mSpaceGuestCheckInFragment)
                    progressBar.progress = 50
                    if (it.status == "1") {
                        setResult(RESULT_OK)
                    }
                })
            }
                else if (visibleFragment is SpaceGuestCheckinFragment) {
                    btnBack.text = getString(R.string.back)
                    btnBack.visibility = View.GONE
                    btnNext.visibility = View.GONE
                    btnSave.visibility = View.VISIBLE
                    btnGoBack.visibility = View.VISIBLE

                guestCheckInDetails = (visibleFragment as SpaceGuestCheckinFragment).getCheckInDetails()
                    mCheckInFromTime = guestCheckInDetails[Iconstants.CHECK_IN_FROM_TIME]!!
                    mCheckInToTime = guestCheckInDetails[Iconstants.CHECK_IN_TO_TIME]!!
                    mCheckOutTime = guestCheckInDetails[Iconstants.CHECK_OUT_TIME]!!

                    listingViewModel.callPostListingStepThree(mPropId,mAmenitiesId,mMetaTitle,mMetaKeyword,mMetaDescription,mInstantBooking,mCancellationPolicy, mCancellationRules,mCheckInFromTime,mCheckInToTime,mCheckOutTime,mMinStay,mMaxStay).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        loadFragment(mSpaceCancellationDetailsFragment)
                        progressBar.progress = 75
                        if(it.status == "1"){
                            setResult(RESULT_OK)
                        }
                    })
            }
            R.id.btnSave -> {
                if (visibleFragment is SpaceCancellationDetailsFragment) {
                    spaceCancellationDetails = (visibleFragment as SpaceCancellationDetailsFragment).getCancellationDetails()
                    mCancellationPolicy = spaceCancellationDetails[Iconstants.CANCELLATION_POLICY]!!
                    mCancellationRules = spaceCancellationDetails[Iconstants.CANCELLATION_RULES]!!
                    mInstantBooking = spaceCancellationDetails[Iconstants.INSTANCE_BOOK]!!

                    listingViewModel.callPostListingStepThree(mPropId, mAmenitiesId, mMetaTitle, mMetaKeyword, mMetaDescription, mInstantBooking, mCancellationPolicy, mCancellationRules,mCheckInFromTime,mCheckInToTime,mCheckOutTime,mMinStay,mMaxStay).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        if (it.status == "1") {
                            setResult(RESULT_OK)
                            finish()
                        }
                    })
                }
            }
            R.id.btnGoBack -> {
                onBackPressed()
            }
        }

    }

    override fun onBackPressed() {
        // super.onBackPressed()
        btnBack.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        btnGoBack.visibility = View.GONE
        if (visibleFragment is SpaceAmenitiesFragment) {
            finish()
        }
        else if (visibleFragment is SpaceEnhanceSearchFragment) {
            val bundle = Bundle()
            bundle.putString(Iconstants.ID,mPropId)
            mSpaceAmenitiesFragment.arguments = bundle
            btnBack.text = getString(R.string.step_two)
            // loadFragment(mSpaceAmenitiesFragment)
            supportFragmentManager.popBackStack()
        } else if(visibleFragment is SpaceGuestCheckinFragment){
            //loadFragment(mSpaceEnhanceSearchFragment)
            supportFragmentManager.popBackStack()
            progressBar.progress = 25
        }else if(visibleFragment is SpaceCancellationDetailsFragment){
            //loadFragment(mSpaceEnhanceSearchFragment)
            supportFragmentManager.popBackStack()
            progressBar.progress = 50
        }
    }
}
