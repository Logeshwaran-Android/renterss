package com.rent.renters.app.ui.base


import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rent.renters.R
import com.rent.renters.app.data.model.CustomPlaceAddress
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.mylibrary.util.Util

open class BaseViewModel constructor(): ViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()

    var rentersError: MutableLiveData<String> = MutableLiveData()

    var rentersLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun rentersError(): LiveData<String> {
        return rentersError
    }

    fun rentersLoader(): LiveData<Boolean> {
        return rentersLoader
    }


    fun init(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetGooglePlaceDetails(latlng : LatLng): MutableLiveData<CustomPlaceAddress> {
        var response: MutableLiveData<CustomPlaceAddress> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.getGooglePlaceDetails(latlng)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }



}