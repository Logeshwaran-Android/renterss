package com.rent.renters.app.ui.listing


import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_space_look_ups.*
import android.os.SystemClock


class SpaceLookUpsActivity : BaseActivity(), View.OnClickListener {


    private var mLastClickTime: Long = 0
    private lateinit var listingViewModel: ListingViewModel

    private val mSpacePhotoFragment = SpacePhotoFragment()
    private val mSpaceDescriptionFragment = SpaceDescriptionFragment()
    private val mSpaceLocationFragment = SpaceLocationFragment()


    private var mLatitude = ""
    private var mLongitude = ""

    private var mZipCode = ""
    private var mAddress = ""
    private var mAboutYourPlace = ""
    private var mSpaceRules = ""
    private var mAboutNeighborhood = ""
    private var mOtherThingsToNote = ""
    private var mSummary = ""
    private var mPropId = "0"

    private var mIsFrom = ""


    private var spaceDescriptionDetails: HashMap<String, String> = HashMap()
    private var spaceLocationDetails: HashMap<String, String> = HashMap()
    private var changedSpaceLocationDetails: HashMap<String, String> = HashMap()

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
        setContentView(R.layout.activity_space_look_ups)
        if(savedInstanceState == null) {
            initViewModel()
            initView()
            getIntentValues()
        }else {
            initViewModel()
            initView()
        }
    }

    private fun getIntentValues(){
        if (intent != null) {
            if (intent.hasExtra(Iconstants.ID)) {
                mPropId = intent?.getStringExtra(Iconstants.ID)!!
            }
            if(intent.hasExtra(Iconstants.IS_FROM))
                mIsFrom = intent.getStringExtra(Iconstants.IS_FROM)!!
        }


        if(mIsFrom.equals(Iconstants.IS_NEW_LISTING,true)) {
            val bundle = Bundle()
            bundle.putString(Iconstants.ID, mPropId)
            mSpacePhotoFragment.arguments = bundle
            btnBack.text = getString(R.string.step_one)
            loadFragment(mSpacePhotoFragment)
        }else if(mIsFrom.equals(Iconstants.EDIT_LISTING,true)){
            callGetListingStepTwo()
        }

    }
    private fun initView() {
        btnBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)

    }

    private fun callGetListingStepTwo() {

        if(mIsFrom.equals(Iconstants.EDIT_LISTING,true)){
            listingViewModel.callGetListingStepTwo(mPropId).observe(this, Observer {
                baseViewModel.rentersLoader.postValue(false)
                if(it.status == "1"){
                    val bundle = Bundle()
                    bundle.putString(Iconstants.ID, mPropId)
                    it.data.properties_images?.let {
                        bundle.putParcelableArrayList(Iconstants.SPACE_GALLERY_IMAGE, it)
                    }
                    mSpacePhotoFragment.arguments = bundle
                    btnBack.text = getString(R.string.step_one)

                    val bundle1 = Bundle()
                    bundle1.putString(Iconstants.ID,mPropId)
                    bundle1.putString(Iconstants.IS_FROM,mIsFrom)
                    bundle1.putString(Iconstants.SUMMARY,it.data.property_data?.summary)
                    bundle1.putString(Iconstants.ABOUT_YOUR_PLACE,it.data.property_data?.about_your_place)
                    bundle1.putString(Iconstants.OTHER_THINGS_TO_NOTE,it.data.property_data?.other_things_to_note)
                    bundle1.putString(Iconstants.SPACE_RULES,it.data.property_data?.space_rules)
                    bundle1.putString(Iconstants.ABOUT_NEIGHBORHOOD,it.data.property_data?.about_neighborhood)
                    mSpaceDescriptionFragment.arguments = bundle1
                    val bundle2 = Bundle()
                    it.data.property_data?.address?.let {
                        bundle2.putParcelable(Iconstants.ADDRESS, it)
                    }
                    bundle2.putString(Iconstants.FULL_ADDRESS, it.data.property_data?.full_address!!)
                    mSpaceLocationFragment.arguments = bundle2
                    loadFragment(mSpacePhotoFragment)
                }
            })
        }

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
                when (visibleFragment) {
                    is SpacePhotoFragment -> {
                        val spaceTypeIntent = Intent(this, SetTheSceneActivity::class.java)
                        spaceTypeIntent.putExtra(Iconstants.ID,mPropId)
                        spaceTypeIntent.putExtra(Iconstants.IS_FROM,Iconstants.EDIT_LISTING)
                        startActivityForResult(spaceTypeIntent, Iconstants.SET_SCENE_REQUEST_CODE)
                        finish()
                    }
                    is SpaceDescriptionFragment -> {
                        val bundle = Bundle()
                        bundle.putString(Iconstants.ID, mPropId)
                        mSpacePhotoFragment.arguments = bundle
                        btnBack.text = getString(R.string.step_one)
                        //loadFragment(mSpacePhotoFragment)
                        supportFragmentManager.popBackStack()
                    }
                    is SpaceLocationFragment -> {
                        progressBar.progress = 25
                      //  loadFragment(mSpaceDescriptionFragment)
                        supportFragmentManager.popBackStack()
                    }
                    is SpaceMapPinFragment -> {
                        progressBar.progress = 50
                        //loadFragment(mSpaceLocationFragment)
                        supportFragmentManager.popBackStack()
                    }
                }
            }
            R.id.btnNext -> when (visibleFragment) {

                is SpacePhotoFragment -> {
                    btnBack.text = getString(R.string.back)
                    loadFragment(mSpaceDescriptionFragment)
                    progressBar.progress = 25

                }
                is SpaceDescriptionFragment -> {


                    btnBack.text = getString(R.string.back)

                    spaceDescriptionDetails = (visibleFragment as SpaceDescriptionFragment).getDescriptionDetails()
                    mSummary = spaceDescriptionDetails[Iconstants.SUMMARY]!!
                    mAboutYourPlace = spaceDescriptionDetails[Iconstants.ABOUT_YOUR_PLACE]!!
                    mOtherThingsToNote = spaceDescriptionDetails[Iconstants.OTHER_THINGS_TO_NOTE]!!
                    mSpaceRules = spaceDescriptionDetails[Iconstants.SPACE_RULES]!!
                    mAboutNeighborhood = spaceDescriptionDetails[Iconstants.ABOUT_NEIGHBORHOOD]!!

                    listingViewModel.callPostListingStepTwo(mPropId, mSummary, mAboutYourPlace, mOtherThingsToNote, mSpaceRules, mAboutNeighborhood, mAddress, mZipCode).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        loadFragment(mSpaceLocationFragment)
                        progressBar.progress = 50
                        if (it.status == "1") {
                            setResult(RESULT_OK)
                        }
                    })

                }
                is SpaceLocationFragment -> {
                    btnBack.text = getString(R.string.back)
                    if ((visibleFragment as SpaceLocationFragment).validateFields()) {

                        spaceLocationDetails = (visibleFragment as SpaceLocationFragment).getLocationDetails()
                        mAddress = spaceLocationDetails[Iconstants.ADDRESS]!!
                        mZipCode = spaceLocationDetails[Iconstants.ZIPCODE]!!
                        mLatitude = spaceLocationDetails[Iconstants.LATITUDE]!!
                        mLongitude = spaceLocationDetails[Iconstants.LONGITUDE]!!

                        listingViewModel.callPostListingStepTwo(mPropId, mSummary, mAboutYourPlace, mOtherThingsToNote, mSpaceRules, mAboutNeighborhood, mAddress, mZipCode).observe(this, Observer {
                            baseViewModel.rentersLoader.postValue(false)
                            val mSpaceMapPinFragment = SpaceMapPinFragment()
                            val bundle = Bundle()
                            bundle.putString(Iconstants.LATITUDE, mLatitude)
                            bundle.putString(Iconstants.LONGITUDE, mLongitude)
                            mSpaceMapPinFragment.arguments = bundle
                            loadFragment(mSpaceMapPinFragment)
                            progressBar.progress = 75
                            if (it.status == "1") {
                                setResult(RESULT_OK)
                            }
                        })
                    }
                }
                is SpaceMapPinFragment -> {
                    btnBack.text = getString(R.string.back)
                    changedSpaceLocationDetails = (visibleFragment as SpaceMapPinFragment).getLocationDetails()
                    mAddress = changedSpaceLocationDetails[Iconstants.ADDRESS]!!
                    mZipCode = changedSpaceLocationDetails[Iconstants.ZIPCODE]!!
                    listingViewModel.callPostListingStepTwo(mPropId, mSummary, mAboutYourPlace, mOtherThingsToNote, mSpaceRules, mAboutNeighborhood, mAddress, mZipCode).observe(this, Observer {
                        baseViewModel.rentersLoader.postValue(false)
                        if (it.status == "1") {
                            setResult(RESULT_OK)
                            finish()
                        }
                    })
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (visibleFragment is SpacePhotoFragment) {
                (visibleFragment as SpacePhotoFragment).onActivityResult(requestCode, resultCode, data)
            }
            if (visibleFragment is SpaceLocationFragment) {
                (visibleFragment as SpaceLocationFragment).onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        when (visibleFragment) {
            is SpacePhotoFragment -> {
                finish()
            }
            is SpaceDescriptionFragment -> {
                val bundle = Bundle()
                bundle.putString(Iconstants.ID, mPropId)
                mSpacePhotoFragment.arguments = bundle
                btnBack.text = getString(R.string.step_one)
                //loadFragment(mSpacePhotoFragment)
                supportFragmentManager.popBackStack()
            }
            is SpaceLocationFragment -> {
                progressBar.progress = 25
                //loadFragment(mSpaceDescriptionFragment)
                supportFragmentManager.popBackStack()
            }
            is SpaceMapPinFragment -> {
                progressBar.progress = 50
               // loadFragment(mSpaceLocationFragment)
                supportFragmentManager.popBackStack()

            }
        }
    }
}
