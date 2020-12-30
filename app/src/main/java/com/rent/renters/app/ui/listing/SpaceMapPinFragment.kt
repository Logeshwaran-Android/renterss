package com.rent.renters.app.ui.listing


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.data.model.CustomPlaceAddress
import com.rent.renters.app.ui.base.BaseActivity
import com.google.android.gms.maps.model.LatLng




/**
 * A simple [Fragment] subclass.
 */
class SpaceMapPinFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener {



    private var spaceLocationDetails: HashMap<String, String> = HashMap<String, String>()
    private lateinit var googleMap: GoogleMap

    private var mLatitude = ""
    private var mLongitude = ""
    private lateinit var mCustomPlace : CustomPlaceAddress
    //private var mMarker : Marker ?= null
    var mRoot: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_space_map_pin, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    fun initView(){
        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.LATITUDE))
                mLatitude = arguments?.getString(Iconstants.LATITUDE,"")!!
            if(arguments!!.containsKey(Iconstants.LONGITUDE))
                mLongitude = arguments?.getString(Iconstants.LONGITUDE,"")!!
        }

        if(mLatitude.isNotEmpty() && mLongitude.isNotEmpty())
            mCustomPlace = (context as BaseActivity).getAddressFromLatLong(mLatitude.toDouble(),mLongitude.toDouble())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true


        if(mLatitude.trim().isNotEmpty() && mLongitude.trim().isNotEmpty()) {
            val mCurrentLatLng = LatLng(mLatitude.toDouble(), mLongitude.toDouble())
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, 14.0f))
        }
       // val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_marker)

     /*   val markerOptions = MarkerOptions().position(LatLng(mLatitude.toDouble(), mLongitude.toDouble()))
                .icon(icon)*/
       // mMarker = googleMap.addMarker(markerOptions)

        googleMap.setOnCameraIdleListener(this)


        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        /*googleMap.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            //get latlng at the center by calling
            val midLatLng = map.cameraPosition.target
        })*/
    }

    override fun onCameraIdle() {
//        mMarker?.let {
//            mMarker!!.position = (googleMap.cameraPosition.target)
//        }

        mCustomPlace = (context as BaseActivity).getAddressFromLatLong(googleMap.cameraPosition.target.latitude,googleMap.cameraPosition.target.longitude)
    }
    override fun onCameraMove() {
        /*mMarker?.let {
            mMarker!!.position = (googleMap.cameraPosition.target)
        }*/

    }
    fun getLocationDetails(): HashMap<String, String> {
        mCustomPlace?.let {
            spaceLocationDetails.clear()
            spaceLocationDetails[Iconstants.ADDRESS] = mCustomPlace.city.plus(",").plus(mCustomPlace.state).plus(",").plus(mCustomPlace.country!!)
            mCustomPlace.postalCode?.let {
                spaceLocationDetails[Iconstants.ZIPCODE] = mCustomPlace.postalCode!!
            }
        }
        return spaceLocationDetails
    }



}
