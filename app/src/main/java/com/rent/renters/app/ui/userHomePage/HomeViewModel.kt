package com.rent.renters.app.ui.userHomePage

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class HomeViewModel : BaseViewModel() {

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetSpaceTypes(): MutableLiveData<TotalSpaceResponse> {
        var spaceTypeResponse: MutableLiveData<TotalSpaceResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            spaceTypeResponse = commonApi.callGetSpaceTypes(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceTypeResponse

    }

    fun callGetSearchSpaceList(address: String, mListId: String,minSquareFeet:Int,maxSquareFeet:Int,minPrice:Int,maxPrice:Int,instant:String,fromDate:String,toDate:String,page:Int,attribute:String,guest:Int): MutableLiveData<SearchSpaceResponse> {
         var spaceSearchResponse: MutableLiveData<SearchSpaceResponse> = MutableLiveData()
        if (Util.isNetworkAvailable(mActivity)) {
            spaceSearchResponse = commonApi.callGetSearchSpaceList(mSessionManager!!.getApiHeader(),address,mListId,minSquareFeet,maxSquareFeet,minPrice,maxPrice,instant,fromDate,toDate,page,attribute,guest)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceSearchResponse

    }

    fun callGetSavedWishList(): MutableLiveData<SearchSpaceResponse> {

        var spaceWishListResponse: MutableLiveData<SearchSpaceResponse> = MutableLiveData()
        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            spaceWishListResponse = commonApi.callGetSavedWishList(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceWishListResponse

    }

    fun callAddRemoveWishList(propId:String): MutableLiveData<AddRemoveWishListResponse> {
        var spaceAddRemoveWishListResponse: MutableLiveData<AddRemoveWishListResponse> = MutableLiveData()
        if (Util.isNetworkAvailable(mActivity)) {

            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            spaceAddRemoveWishListResponse = commonApi.callAddRemoveWishlist(mSessionManager!!.getApiHeader(),propId)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceAddRemoveWishListResponse

    }


    fun callGetDefaultFilterValues( ): MutableLiveData<FilterDefaultDataResponse> {
        var response: MutableLiveData<FilterDefaultDataResponse> = MutableLiveData()
        if (Util.isNetworkAvailable(mActivity)) {

            response = commonApi.callGetDefaultFilterValues(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return response

    }


}
