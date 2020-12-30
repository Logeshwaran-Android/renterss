package com.rent.renters.app.ui.myBookings

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class MyBookingsViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }
    fun callGetTotalBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
    fun callGetCompletedBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"completed",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
    fun callGetPendingHostApprovalBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"pendingapproval",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetPendingPaymentBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"pendingpayment",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
    fun callGetUpcomingCheckinBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"upcomingcheckin",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetExpiredBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"expired",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetInvoice(bookingNo: String) : MutableLiveData<InvoiceResponse> {
        var response: MutableLiveData<InvoiceResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetInvoice(mSessionManager!!.getApiHeader(),bookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetBookingDetails(bookingNo: String): MutableLiveData<BookingDetailsResponse> {
        var response: MutableLiveData<BookingDetailsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            //(mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingDetails(mSessionManager!!.getApiHeader(),bookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

    fun callGetCancellationReson( ): MutableLiveData<CancellationReasonResponse> {
        var response: MutableLiveData<CancellationReasonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            //(mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetCancellationReason(mSessionManager!!.getApiHeader())

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

    fun callGetCancellationBooking(mBookingNo:String ): MutableLiveData<CancellationBookingResponse> {
        var response: MutableLiveData<CancellationBookingResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetCancellationBooking(mSessionManager!!.getApiHeader(),mBookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

    fun callCancelBooking(bookingNo: String,reasonId:String, cancellationDesc:String,accName:String,bankName:String,accNo:String ): MutableLiveData<CancellationReasonResponse> {
        var response: MutableLiveData<CancellationReasonResponse> = MutableLiveData()
        when {
            reasonId.isEmpty() ->
                (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.cancel_reason_err))
            accName.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.acc_name_err))
            bankName.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.acc_number_err))
            bankName.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.bank_name_err))
             (Util.isNetworkAvailable(mActivity))->{
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                response = commonApi.callCancelBooking(mSessionManager!!.getApiHeader(), bookingNo, reasonId, cancellationDesc, accName, bankName, accNo)

            } else ->
                (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        }

        return response

    }

    fun callGetDeclinedBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"declined",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
    fun callGetCancelledBookingList(page:Int) : MutableLiveData<MyTripsResponse> {
        var response: MutableLiveData<MyTripsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetBookingList(mSessionManager!!.getApiHeader(),"cancelled",page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
}