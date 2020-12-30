package com.rent.renters.app.ui.map

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.rent.renters.R
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.data.model.SearchSpaceResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.filter.MoreFilterActivity
import com.rent.renters.app.ui.userHomePage.HomeViewModel
import com.rent.renters.app.ui.userHomePage.SimilarSpaceTypeItemAdapter
import kotlinx.android.synthetic.main.activity_map_view.*
import kotlinx.android.synthetic.main.activity_map_view.rvSpaceTypeItems

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.mylibrary.util.SnapHelperOneByOne
import kotlinx.android.synthetic.main.no_data_layout.*


class MapViewActivity : BaseActivity(),  View.OnClickListener, OnMapReadyCallback,SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener {
    override fun spaceTypeItemClick(item: SearchSpaceData) {
        val detailPageIntent = Intent(this, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID,item.id)
        startActivity(detailPageIntent)
    }

    private lateinit var googleMap: GoogleMap
    private var spaceTypeItemAdapter: SimilarSpaceTypeItemAdapter? = null
    private lateinit var homeViewModel: HomeViewModel
    private var mSearchSpaceList : ArrayList<SearchSpaceData> = ArrayList()
    private var mMarkerList : ArrayList<Marker> = ArrayList()
    private var mPreviousMarker : Marker ?= null


    private lateinit var mSessionManager : SessionManager
    private lateinit  var iconFactory : IconGenerator


    private var mVisibleItemPosition : Int = 0
    private var mPreviousVisibleItemPosition : Int = 0
    private var mListId : String = ""
    private var mMainListId : String = ""

    private var mCity : String = ""
    private var mMinPrice: Int = 1
    private var mMaxPrice: Int = 1
    private var mInstantBook: String = ""
    private var mAttribute: String = ""

    private lateinit var mListener : SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
        mListener = this
        mMinPrice = (applicationContext as RentersApplication).getMinPriceValue()
        mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID)) {
                mListId = intent.getStringExtra(Iconstants.ID)!!
                mMainListId = mListId
            }
            if(intent.hasExtra(Iconstants.LOCATION))
                mCity = intent.getStringExtra(Iconstants.LOCATION)!!
        }

        iconFactory = IconGenerator(this)
        ivClose.setOnClickListener(this)
        btnFilter.setOnClickListener(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvSpaceTypeItems.layoutManager = layoutManager


        setSpaceTypeItemAdapter()
      //  getCurrentLocation(mLocationListener)

        setRecyclerViewScrollListener()


    }

    private fun setRecyclerViewScrollListener() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(mSearchSpaceList.size > 0) {


                        mSearchSpaceList[mVisibleItemPosition].isSelected = true
                        if (mVisibleItemPosition != mPreviousVisibleItemPosition) {
                            iconFactory.setStyle(IconGenerator.STYLE_WHITE, this@MapViewActivity)
                            mMarkerList[mPreviousVisibleItemPosition].zIndex = 0F
                            mMarkerList[mPreviousVisibleItemPosition].setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[mPreviousVisibleItemPosition].base_price!!))))
                            mSearchSpaceList[mPreviousVisibleItemPosition].isSelected = false
                        }
                        mPreviousVisibleItemPosition = mVisibleItemPosition
                        spaceTypeItemAdapter!!.notifyDataSetChanged()

                        iconFactory.setStyle(IconGenerator.STYLE_BLUE, this@MapViewActivity)
                        mMarkerList[mVisibleItemPosition].zIndex = 1F
                        (mMarkerList[mVisibleItemPosition]).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[mVisibleItemPosition].base_price!!))))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mSearchSpaceList[mVisibleItemPosition].latitude!!.toDouble(), mSearchSpaceList[mVisibleItemPosition].longitude!!.toDouble()), 14.0f))
                    }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0) {
                    mVisibleItemPosition = (recyclerView.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()+1
                } else if (dx < 0) {
                    mVisibleItemPosition = (recyclerView.layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()-1
                }

            }
        }
        rvSpaceTypeItems.addOnScrollListener(scrollListener)
    }
    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.initMethod(this)
      callGetMapList()

    }

    private fun callGetMapList() {
        baseViewModel.rentersLoader.postValue(true)
        homeViewModel.callGetSearchSpaceList(mCity,mListId,1,(applicationContext as RentersApplication).getMaxPriceValue(),mMinPrice,mMaxPrice,mInstantBook,"","",1,mAttribute,0).observe(this, Observer<SearchSpaceResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1") {
                mSearchSpaceList.clear()
                mSearchSpaceList.addAll(it.data?.search_results!!)
                googleMap.clear()
                mMarkerList.clear()
                setGoogleMap(mSearchSpaceList)
                if(mSearchSpaceList.size > 0) {
                    noDataLayout.visibility = View.GONE
                    rvSpaceTypeItems.visibility = View.VISIBLE
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mSearchSpaceList[0].latitude!!.toDouble(), mSearchSpaceList[0].longitude!!.toDouble()), 14.0f))
                    mSearchSpaceList[0].isSelected = true
                } else{
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.visibility = View.GONE
                    rvSpaceTypeItems.visibility = View.INVISIBLE

                }
                setSpaceTypeItemAdapter()

            }
        })
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!
        val mCurrentLatLng = LatLng((applicationContext as RentersApplication).mLatitude,(applicationContext as RentersApplication).mLongitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng,14.0f))
        googleMap.setOnMarkerClickListener {

            val position : Int = it?.tag as Int

            if(mPreviousMarker!=null){
                val mPreviousPosition : Int = mPreviousMarker?.tag as Int
                mSearchSpaceList[mPreviousPosition].isSelected = false
                iconFactory.setStyle(IconGenerator.STYLE_WHITE,this)
                mPreviousMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[mPreviousPosition].base_price!!))))

            }
            iconFactory.setStyle(IconGenerator.STYLE_BLUE,this)
            it.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[position].base_price!!))))
            mSearchSpaceList[position].isSelected = true
            spaceTypeItemAdapter!!.notifyDataSetChanged()
            rvSpaceTypeItems.smoothScrollToPosition(position)
            mPreviousMarker = it
            true

        }

    }

   /* override fun locationUpdate(address: String) {
        baseViewModel.rentersLoader.postValue(true)
        homeViewModel.callGetSearchSpaceList(address,mListId,0,0,0,0,"","","",1,"").observe(this, Observer<SearchSpaceResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1") {
                mSearchSpaceList.addAll(it.data.search_results)
                setGoogleMap(mSearchSpaceList)
                if(mSearchSpaceList.size > 0) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mSearchSpaceList[0].latitude.toDouble(), mSearchSpaceList[mVisibleItemPosition].longitude.toDouble()), 14.0f))
                    mSearchSpaceList[0].isSelected = true
                }
                setSpaceTypeItemAdapter(mSearchSpaceList)

            }
        })
    }*/

    private fun setGoogleMap(mSearchSpaceList: ArrayList<SearchSpaceData>) {
        for(i in 0 until  mSearchSpaceList.size){
            val mMarkerLabel = mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[i].base_price)
            val mLatLng = LatLng(mSearchSpaceList[i].latitude!!.toDouble(),mSearchSpaceList[i].longitude!!.toDouble())
            addIcon(mMarkerLabel,mLatLng,i)
        }
    }

    private fun addIcon(text: CharSequence, position: LatLng, listPosition : Int) {
        val markerOptions : MarkerOptions = if(listPosition == 0){
            iconFactory.setStyle(IconGenerator.STYLE_BLUE,this)
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(position).anchor(iconFactory.anchorU, iconFactory.anchorV).zIndex(1F)
        } else{
            iconFactory.setStyle(IconGenerator.STYLE_WHITE,this)
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(position).anchor(iconFactory.anchorU, iconFactory.anchorV).zIndex(0F)
        }


        val marker = (googleMap.addMarker(markerOptions))
        marker.tag = listPosition
        mMarkerList.add(marker)


    }


    private fun setSpaceTypeItemAdapter() {
        if (spaceTypeItemAdapter == null) {
            spaceTypeItemAdapter = SimilarSpaceTypeItemAdapter(this,  mListener,mSearchSpaceList)
            rvSpaceTypeItems.adapter = spaceTypeItemAdapter
            SnapHelperOneByOne().attachToRecyclerView(rvSpaceTypeItems)
        } else {
            spaceTypeItemAdapter!!.notifyDataSetChanged()
        }
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ivClose -> finish()
            R.id.btnFilter ->{
                val moreFilterIntent = Intent(this, MoreFilterActivity::class.java)

                val bundle = Bundle()
                bundle.putString(Iconstants.ID,mListId)
                bundle.putString(Iconstants.SELECTED_ID,mAttribute)
                if(mMaxPrice!= 0) {
                    bundle.putInt(Iconstants.MIN_PRICE, mMinPrice)
                    bundle.putInt(Iconstants.MAX_PRICE, mMaxPrice)
                }
                bundle.putString(Iconstants.INSTANCE_BOOK,mInstantBook)


                moreFilterIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivityForResult(moreFilterIntent,Iconstants.MORE_FILTER_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Iconstants.MORE_FILTER_REQUEST_CODE){
                mListId = data?.getStringExtra(Iconstants.ID)!!
                mAttribute = data.getStringExtra(Iconstants.SELECTED_ID)!!
                mMinPrice = data.getIntExtra(Iconstants.MIN_PRICE,1)
                mMaxPrice = data.getIntExtra(Iconstants.MAX_PRICE,(applicationContext as RentersApplication).getMaxPriceValue())
                mInstantBook = if(data.getBooleanExtra(Iconstants.INSTANCE_BOOK,false))
                    "Yes"
                else
                    "No"
                mVisibleItemPosition = 0
                mPreviousVisibleItemPosition = 0
                callGetMapList()
            }
        }else if(resultCode == Activity.RESULT_CANCELED) {
            if(requestCode == Iconstants.MORE_FILTER_REQUEST_CODE){
                mAttribute = ""
                mListId = mMainListId
                mVisibleItemPosition = 0
                mPreviousVisibleItemPosition = 0
                callGetMapList()
            }
        }
        }
    override fun addRemoveWishList(item: SearchSpaceData,position: Int) {
        homeViewModel.callAddRemoveWishList(item.id!!).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSearchSpaceList[position].is_favorite = it.data?.is_favorite!!
                setSpaceTypeItemAdapter()
                baseViewModel.rentersError.postValue(it.message)
            }
        })

    }

}
