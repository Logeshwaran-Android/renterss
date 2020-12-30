package com.rent.renters.app.ui.inbox

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class ChatViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity


    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()

    private var chatListResponse: MutableLiveData<ViewChatResponse> = MutableLiveData()


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetConversationDetails(bookingNo: String): MutableLiveData<ViewChatResponse> {

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            chatListResponse = commonApi.viewConversationDetails(mSessionManager!!.getApiHeader(), bookingNo)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return chatListResponse

    }

    fun callSendMessage(message: String,senderId: String,receiverId: String,bookingNo: String,bookingStatus: String): MutableLiveData<CommonResponse> {
         var response: MutableLiveData<CommonResponse> = MutableLiveData()
        when {
            message.trim().isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.message_empty_err))
            Util.isNetworkAvailable(mActivity) -> //(mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                response = commonApi.callSendMessage(mSessionManager!!.getApiHeader(), message,senderId,receiverId,bookingNo,bookingStatus)
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

    fun callGetGuestInbox(page: Int): MutableLiveData<MessageResponse> {
        var response: MutableLiveData<MessageResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetGuestInbox(mSessionManager!!.getApiHeader(),page)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }
    fun callGetHostInbox(page: Int): MutableLiveData<MessageResponse> {
        var response: MutableLiveData<MessageResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetHostInbox(mSessionManager!!.getApiHeader(),page)

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
 /*   fun callProceedToBooking(propId: String,startDate: String,endDate: String) : MutableLiveData<PaymentBookingResponse> {
        var response: MutableLiveData<PaymentBookingResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callProceedToBooking(mSessionManager!!.getApiHeader(), propId,startDate,endDate)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }*/
    fun callPostBookingRequest(bookingNo: String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

       if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPostBookingRequest(mSessionManager!!.getApiHeader(), bookingNo,"")

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
}
