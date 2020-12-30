package com.rent.renters.app.ui.listing


import android.app.Activity
import android.content.Intent

import androidx.core.content.ContextCompat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_list_your_space.*

class ListYourSpaceActivity : BaseActivity(), View.OnClickListener {

    private var mPropId =""

    private lateinit var listingViewModel: ListingViewModel
    private var mSetTheSceneFrom = ""
    private var mSpaceLookUpsFrom = ""
    private var mSpaceGuestFrom = ""
    private var mDefaultIsFrom = ""
    private lateinit var mSessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_your_space)
        mSessionManager = SessionManager(this)
        initViewModel()
        initView()
    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.IS_FROM)) {
                mDefaultIsFrom = intent.getStringExtra(Iconstants.IS_FROM)!!
                mSetTheSceneFrom = mDefaultIsFrom
                mSpaceGuestFrom = mDefaultIsFrom
                mSpaceLookUpsFrom = mDefaultIsFrom
            }
            if(intent.hasExtra(Iconstants.ID))
                mPropId = intent.getStringExtra(Iconstants.ID)!!
        }

        tvListSpaceTitle.text = getString(R.string.great_progress).plus(", ").plus(mSessionManager.getUserDetails().firstname).plus("!")
        btnSetTheScene.setOnClickListener(this)
        btnSpaceLookUps.setOnClickListener(this)
        btnGuests.setOnClickListener(this)
        tvChangeSetTheScene.setOnClickListener(this)
        tvChangeSpaceLookups.setOnClickListener(this)
        tvChangeGuests.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)

        if(mDefaultIsFrom.equals(Iconstants.IS_NEW_LISTING,true)){
            listingViewModel.callListYourSpace().observe(this, Observer {
                baseViewModel.rentersLoader.postValue(false)
                if(it.status == "1"){
                    mPropId = it.data?.propid!!
                }

            })
        } else if(mDefaultIsFrom.equals(Iconstants.EDIT_LISTING,true)){

            callGetListingSteps()
        }
    }

    private fun callGetListingSteps() {
        listingViewModel.callGetListingSteps(mPropId).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            tvSpaceLookups.setTextColor(ContextCompat.getColor(this,(R.color.text_black)))
            tvSpaceLookupsItem.setTextColor(ContextCompat.getColor(this,(R.color.text_black)))
            tvGuests.setTextColor(ContextCompat.getColor(this,R.color.text_black))
            tvGuestsItem.setTextColor(ContextCompat.getColor(this,(R.color.text_black)))
            if(it.status == "1"){
                setChangeButtonVisible()

                if(it.data.step_one_status!! == "1"){
                    ivSetTheScene.visibility = View.VISIBLE
                    btnSetTheScene.visibility = View.GONE
                    tvChangeSetTheScene.visibility = View.VISIBLE

                }else{
                    btnSetTheScene.visibility = View.VISIBLE
                    tvChangeSetTheScene.visibility = View.GONE
                    tvChangeSetTheScene.setTextColor(ContextCompat.getColor(this, R.color.app_color))
                }
                if(it.data.step_two_status!! == "1"){
                    ivSpaceLookUps.visibility = View.VISIBLE
                    btnSpaceLookUps.visibility = View.GONE
                    tvChangeSpaceLookups.visibility = View.VISIBLE
                }else{
                    btnSpaceLookUps.visibility = View.VISIBLE
                    tvChangeSpaceLookups.visibility = View.GONE
                    tvChangeSpaceLookups.setTextColor(ContextCompat.getColor(this, R.color.app_color))
                }
                if(it.data.step_three_status!! == "1"){
                    ivGuests.visibility = View.VISIBLE
                    btnGuests.visibility = View.GONE
                    tvChangeGuests.visibility = View.VISIBLE

                }else{
                    btnGuests.visibility = View.VISIBLE
                    tvChangeGuests.visibility = View.GONE
                    tvChangeGuests.setTextColor(ContextCompat.getColor(this, R.color.app_color))
                }
            }

        })

    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvChangeSetTheScene,R.id.btnSetTheScene -> {
                val spaceTypeIntent = Intent(this, SetTheSceneActivity::class.java)
                spaceTypeIntent.putExtra(Iconstants.ID,mPropId)
                spaceTypeIntent.putExtra(Iconstants.IS_FROM,mSetTheSceneFrom)
                startActivityForResult(spaceTypeIntent, Iconstants.SET_SCENE_REQUEST_CODE)
            }
            R.id.tvChangeSpaceLookups,R.id.btnSpaceLookUps -> {
                val spacePhotoIntent = Intent(this, SpaceLookUpsActivity::class.java)
                spacePhotoIntent.putExtra(Iconstants.ID,mPropId)
                spacePhotoIntent.putExtra(Iconstants.IS_FROM,mSpaceLookUpsFrom)
                startActivityForResult(spacePhotoIntent, Iconstants.SET_LOOK_UPS_REQUEST_CODE)
            }
            R.id.tvChangeGuests,R.id.btnGuests -> {
                val spcaeAmenitiesIntent = Intent(this, SpaceListingGuestActivity::class.java)
                spcaeAmenitiesIntent.putExtra(Iconstants.ID,mPropId)
                spcaeAmenitiesIntent.putExtra(Iconstants.IS_FROM,mSpaceGuestFrom)
                startActivityForResult(spcaeAmenitiesIntent, Iconstants.SET_GUEST_REQUEST_CODE)
            }
            R.id.imgBtnBack ->
                finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Iconstants.SET_SCENE_REQUEST_CODE -> {
                    setResult(Activity.RESULT_OK)
                    mSetTheSceneFrom = Iconstants.EDIT_LISTING
                    if(mDefaultIsFrom.equals(Iconstants.IS_NEW_LISTING,true))
                    btnSpaceLookUps.visibility = View.VISIBLE
                    tvSpaceLookupsItem.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_color))
                    tvSpaceLookups.setTextColor(ContextCompat.getColor(this, R.color.text_black))
                }
                Iconstants.SET_LOOK_UPS_REQUEST_CODE -> {
                    //  callGetListingSteps()
                    setResult(Activity.RESULT_OK)
                    mSpaceLookUpsFrom = Iconstants.EDIT_LISTING
                    if(mDefaultIsFrom.equals(Iconstants.IS_NEW_LISTING,true)) {
                        btnGuests.visibility = View.VISIBLE
                    }
                    tvGuests.setTextColor(ContextCompat.getColor(this, R.color.text_black))
                    tvGuestsItem.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_color))
                }
                Iconstants.SET_GUEST_REQUEST_CODE -> {

                    // callGetListingSteps()

                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }


        }
    }

    private fun setChangeButtonVisible() {

    }


}
