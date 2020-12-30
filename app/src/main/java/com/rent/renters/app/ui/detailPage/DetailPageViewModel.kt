package com.rent.renters.app.ui.detailPage

import android.app.Activity

import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class DetailPageViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity
    private var detailPageResponse: MutableLiveData<DetailPageResponse> = MutableLiveData()
    private var hostProfileResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()



    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetDetailPageDetails(propId: String) : MutableLiveData<DetailPageResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            //(mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            detailPageResponse = commonApi.callGetDetailPage(mSessionManager!!.getApiHeader(),propId)

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return detailPageResponse

    }

    fun callGetHostProfileDetails(userId: String) : MutableLiveData<ProfileResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            hostProfileResponse = commonApi.callGetProfile(mSessionManager!!.getApiHeader(),userId)

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return hostProfileResponse

    }
    fun callGetBlockedDates(propId: String) : MutableLiveData<BlockedDatesResponse> {
        var response: MutableLiveData<BlockedDatesResponse> = MutableLiveData()
        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBlockedDates(mSessionManager!!.getApiHeader(), propId)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
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


}