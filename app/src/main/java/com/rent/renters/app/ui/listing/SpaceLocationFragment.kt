package com.rent.renters.app.ui.listing


import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.searchLocation.SearchActivity
import kotlinx.android.synthetic.main.fragment_space_location.*
import com.rent.renters.app.data.model.AddressData
import com.rent.renters.app.data.model.CustomPlaceAddress


/**
 * A simple [Fragment] subclass.
 */
class SpaceLocationFragment : Fragment() {

    private var spaceLocationDetails: HashMap<String, String> = HashMap<String, String>()

    private var mLatitude = ""
    private var mLongitude = ""
    var mRoot: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            mRoot =  inflater.inflate(R.layout.fragment_space_location, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState == null)
        initView()

    }

    fun initView() {
       arguments?.let{
            if (arguments?.containsKey(Iconstants.ADDRESS)!!) {
                try{
                val mAddress = arguments?.getParcelable<AddressData>(Iconstants.ADDRESS)
                mAddress?.let {
                    it.city.let {
                        tvCityVal.text = it
                    }
                    it.country.let {
                        tvCountryVal.text = it
                    }
                    it.state.let {
                        tvStateVal.text = it
                    }
                    it.zipcode.let {
                        tvZipCodeVal.text = it
                    }
                    mLatitude = mAddress?.latitude!!
                    mLongitude = mAddress.longitude
                }
                }catch (ex : Exception){
                    ex.printStackTrace()
                }

            }
            if (arguments?.containsKey(Iconstants.FULL_ADDRESS)!!)
                tvSearch.text = arguments?.getString(Iconstants.FULL_ADDRESS)
        }


        tvSearch.setOnClickListener {
            val searchIntent = Intent(context, SearchActivity::class.java)
             searchIntent.putExtra(Iconstants.HINT,getString(R.string.property_location))
            startActivityForResult(searchIntent, Iconstants.SEARCH_LOCATION_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Iconstants.SEARCH_LOCATION_REQUEST_CODE) {
                val mLocation = data?.getParcelableExtra<CustomPlaceAddress>(Iconstants.LOCATION)
                tvSearch.text = mLocation?.address
                mLatitude = mLocation?.latitude.toString()
                mLongitude = mLocation?.longitude.toString()
                setAddressData(mLocation!!)
            }
        }
    }

    private fun setAddressData(mLocation: CustomPlaceAddress) {

        tvCityVal.text = mLocation.city
        tvCountryVal.text = mLocation.country
        tvStateVal.text = mLocation.state
        tvZipCodeVal.text = mLocation.postalCode

    }

    fun validateFields(): Boolean {
        if (tvSearch.text.toString().isEmpty()) {
            (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.location_err))
            return false
        } else
            return true
    }

    fun getLocationDetails(): HashMap<String, String> {
        spaceLocationDetails.clear()
        spaceLocationDetails[Iconstants.ADDRESS] = tvSearch.text.toString()
        spaceLocationDetails[Iconstants.ZIPCODE] = tvZipCodeVal.text.toString()
        spaceLocationDetails[Iconstants.LATITUDE] = mLatitude
        spaceLocationDetails[Iconstants.LONGITUDE] = mLongitude
        return spaceLocationDetails
    }
}
