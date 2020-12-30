package com.rent.renters.app.ui.searchLocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.header_layout.*
import android.text.Editable
import android.text.TextWatcher

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rent.renters.app.data.model.CustomPlaceAddress


import com.rent.renters.app.data.model.CustomPlaceSearchModal
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import java.util.*

import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


class SearchActivity : BaseActivity(), View.OnClickListener, CustomPlaceSearchAdapter.CustomOnClickListener, BaseActivity.locationInterface {


    private lateinit var searchViewModel: SearchViewModel
    private lateinit var mCustomPlaceSearchAdapter: CustomPlaceSearchAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var mResultAddress: String = ""
    private var mIsCurrentLocation : Boolean = false

    private var mAddressListArr: ArrayList<CustomPlaceSearchModal> = ArrayList()
    private lateinit var mListener: CustomPlaceSearchAdapter.CustomOnClickListener
    private lateinit var mLocationListener: locationInterface
    var timer = Timer()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mListener = this
        mLocationListener = this
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.initMethod(this)
    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.LOCATION)) {
                val mLocation = intent.getStringExtra(Iconstants.LOCATION)
                if(mLocation!!.isNotEmpty())
                autoCompleteSearchTxt.setText(mLocation)
            }
            if(intent.hasExtra(Iconstants.HINT)) {
                val mHint = intent.getStringExtra(Iconstants.HINT)
                if(mHint!!.isNotEmpty())
                    autoCompleteSearchTxt.setHint(mHint)
            }

        }
        tvTitle.text = getString(R.string.location)

        imgBtnBack.setOnClickListener(this)
        ivLocation.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvNearbyPlaces.layoutManager = layoutManager

        autoCompleteSearchTxt.threshold = 1

        mCustomPlaceSearchAdapter = CustomPlaceSearchAdapter(this, mAddressListArr, mListener)
        rvNearbyPlaces.adapter = mCustomPlaceSearchAdapter

        autoCompleteSearchTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(!mIsCurrentLocation) {
                    timer = Timer()
                    timer.schedule(timerTask {
                        mResultAddress = autoCompleteSearchTxt.text.toString()
                        startSearch(mResultAddress)
                    }, 600)
                } else{
                    mIsCurrentLocation = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer.cancel()
            }
        })

    }


    private fun startSearch(address: String) {
        runOnUiThread {
            val mAddress = address.toLowerCase().replace(" ", "%20")
            searchViewModel.getGooglePlaceSearchResult(mAddress).observe(this, Observer {
               // baseViewModel.rentersLoader.postValue(false)
                if(it!=null) {

                    mAddressListArr.clear()
                    mAddressListArr.addAll(it)
                    mCustomPlaceSearchAdapter.notifyDataSetChanged()
                }

            })
        }
    }





    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgBtnBack -> finish()
            R.id.ivLocation -> {
                mIsCurrentLocation = true
                     getCurrentLocation(mLocationListener)


            }
        }
    }

    override fun onItemClickListener(customPlaceSearchModal: CustomPlaceSearchModal) {
        mResultAddress = customPlaceSearchModal.address

       searchViewModel.getLatLng(customPlaceSearchModal.placeId).observe(this, Observer {
              if (it != null) {
                  autoCompleteSearchTxt.setText(mResultAddress)
                  mAddressListArr.clear()
                  mCustomPlaceSearchAdapter.notifyDataSetChanged()
                  val mCustomAddress = getAddressFromLatLong(it.latitude.toDouble(),it.longitude.toDouble())
                  mCustomAddress.address = mResultAddress
                  val resultIntent = Intent()
                  resultIntent.putExtra(Iconstants.LOCATION, mCustomAddress)
                  setResult(Activity.RESULT_OK, resultIntent)
                  finish()


              }
          })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun locationUpdate(customAddress: CustomPlaceAddress) {

            mResultAddress = customAddress.city.plus(",").plus(customAddress.state).plus(",").plus(customAddress.country)
            autoCompleteSearchTxt.setText(mResultAddress)
            customAddress.address = mResultAddress
            val resultIntent = Intent()
            resultIntent.putExtra(Iconstants.LOCATION, customAddress)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

    }
}
