package com.rent.renters.app.ui.searchLocation

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CustomPlaceSearchModal
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class SearchViewModel : BaseViewModel() {

    private lateinit var mActivity : Activity


    private var searchResponse: MutableLiveData<ArrayList<CustomPlaceSearchModal>> = MutableLiveData()
    private var latLngResponse: MutableLiveData<CustomPlaceSearchModal> = MutableLiveData()
    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()

    fun initMethod(activity: Activity) {
        mActivity = activity
    }

    fun getGooglePlaceSearchResult(input : String) : MutableLiveData<ArrayList<CustomPlaceSearchModal>> {

        if(Util.isNetworkAvailable(mActivity)) {
           // (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            searchResponse = commonApi.getGooglePlaceSearchResult(input)
        }

        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return searchResponse

    }
    fun getLatLng(input : String) : MutableLiveData<CustomPlaceSearchModal> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            latLngResponse = commonApi.getLatLng(input)
        }

        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return latLngResponse

    }



}