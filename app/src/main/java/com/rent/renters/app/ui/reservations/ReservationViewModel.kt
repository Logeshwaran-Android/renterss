package com.rent.renters.app.ui.reservations

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.MyReservationResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class ReservationViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetTotalReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetCompletedReservationList(page : Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "completed",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetPendingHostApprovalReservationList(page:Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "pendingapproval",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetPendingPaymentReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "pendingpayment",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetUpcomingCheckinReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "upcomingcheckin",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetExpiredReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "expired",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
    fun callGetReservationFilterList(type:String,keyword:String,fromDate:String,toDate:String,searchType:String): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationFilterList(mSessionManager!!.getApiHeader(), type,keyword,fromDate,toDate,searchType)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callApproveDeclineRequest(message:String,senderId:String,receiverId:String,booking_no:String,booking_status:String): MutableLiveData<CommonResponse> {

        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callOwnerReservationRequest(mSessionManager!!.getApiHeader(),message,senderId,receiverId,booking_no,booking_status)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetDeclinedReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "declined",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetCancelledReservationList(page: Int): MutableLiveData<MyReservationResponse> {
        var response: MutableLiveData<MyReservationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReservationList(mSessionManager!!.getApiHeader(), "cancelled",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
}
