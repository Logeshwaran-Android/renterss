package com.rent.renters.app.ui.bookingSteps

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class BookingViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()



    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetPaymentCalculation(propId: String,startDate: String,endDate: String) : MutableLiveData<PaymentCalculationResponse> {
        var response: MutableLiveData<PaymentCalculationResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetPaymentCalculation(mSessionManager!!.getApiHeader(), propId,startDate,endDate)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callRemoveCoupon(bookingNo: String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callRemoveCoupon(mSessionManager!!.getApiHeader(), bookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response


    }

    fun callProceedToBooking(propId: String,startDate: String,endDate: String,adultCount:String, childCount:String,infantCount:String) : MutableLiveData<PaymentBookingResponse> {
        var response: MutableLiveData<PaymentBookingResponse> = MutableLiveData()
        when {
            adultCount.isEmpty() || adultCount.equals("0") -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.adult_count_err))
            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                response = commonApi.callProceedToBooking(mSessionManager!!.getApiHeader(), propId,startDate,endDate,adultCount,childCount,infantCount)

            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callPostBookingRequest(bookingNo: String,message: String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if(message.isEmpty()){
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.message_empty_err))
        }else if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPostBookingRequest(mSessionManager!!.getApiHeader(), bookingNo,message)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetReviewAndPayDetails(bookingNo: String) : MutableLiveData<ReviewAndPayResponse> {
        var response: MutableLiveData<ReviewAndPayResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetReviewAndPayDetails(mSessionManager!!.getApiHeader(), bookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callApplyCouponCode(bookingNo: String,couponCode:String) : MutableLiveData<CouponResponse> {
        var response: MutableLiveData<CouponResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callApplyCoupon(mSessionManager!!.getApiHeader(), bookingNo,couponCode)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callContactHost(startDate:String,endDate:String,message:String,propId:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()
        when {
            startDate.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.booking_date_err))
            endDate.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.booking_date_err))
            message.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.message_empty_err))
            Util.isNetworkAvailable(mActivity) -> {

                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                response = commonApi.callContactHost(mSessionManager!!.getApiHeader(),startDate,endDate,message,propId)
            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

}
