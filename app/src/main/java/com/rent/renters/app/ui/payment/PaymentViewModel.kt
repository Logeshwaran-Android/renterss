package com.rent.renters.app.ui.payment

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.PaymentGateWayResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class PaymentViewModel  : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()



    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetAvailablePaymentMethod() : MutableLiveData<PaymentGateWayResponse> {
        var response: MutableLiveData<PaymentGateWayResponse> = MutableLiveData()
        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetPaymentGateWays(mSessionManager!!.getApiHeader())

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }

    fun callPaymentByCard(bookingNo: String,paymentType: String,cardType: String,cardNumber: String,cardMonth: String,cardYear: String,securityCode: String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()
        if(cardNumber.trim().length<16)
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.card_number_err))
        else if(cardType.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.card_type_err))
        else if(securityCode.length != 3)
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.security_code_err))
        else if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPaymentByCard(mSessionManager!!.getApiHeader(),bookingNo,paymentType,cardType,cardNumber,cardMonth,cardYear,securityCode)

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }


    fun callPaymentByPaypal(bookingNo:String,transactionId:String,status:String,message:String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPaymentByPaypal(mSessionManager!!.getApiHeader(),bookingNo,transactionId,status,message)

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response

    }
}