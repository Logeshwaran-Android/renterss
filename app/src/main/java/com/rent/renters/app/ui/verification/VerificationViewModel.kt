package com.rent.renters.app.ui.verification

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util
import java.io.File


class VerificationViewModel : BaseViewModel() {

    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }
    fun getVerificationStatus() : MutableLiveData<VerificationStatusResponse> {
        var verificationStatusResponse: MutableLiveData<VerificationStatusResponse> = MutableLiveData()
        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            verificationStatusResponse = commonApi.callGetVerificationStatus(mSessionManager!!.getApiHeader())
        }

        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return verificationStatusResponse

    }
    fun callVerifyID(countryCode:String, idType:String, frontImageFile: File, backImageFile: File) : MutableLiveData<IDVerificationResponse> {
         var verifyIDResponse: MutableLiveData<IDVerificationResponse> = MutableLiveData()
        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            verifyIDResponse = commonApi.callVerifyID(mSessionManager!!.getApiHeader(), countryCode, idType, frontImageFile, backImageFile)
        }

        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return verifyIDResponse

    }
    fun callVerifyEmail() : MutableLiveData<VerifiedEmailResponse> {
         var verifyEmailResponse: MutableLiveData<VerifiedEmailResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            verifyEmailResponse = commonApi.callVerifyEmailAddress(mSessionManager!!.getApiHeader())
        }

        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return verifyEmailResponse

    }
    fun callVerifyPhoneNumber(countryCode: String,mobileCode: String,phoneNumber:String) : MutableLiveData<VerifiedPhoneResponse> {
        var verifyPhoneResponse: MutableLiveData<VerifiedPhoneResponse> = MutableLiveData()

        when {
            countryCode.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.country_code_err))
            phoneNumber.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.phone_number_err))
            (mActivity as BaseActivity).validateUsing_libphonenumber(mobileCode,phoneNumber) -> {
                if(Util.isNetworkAvailable(mActivity)) {
                    (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                    verifyPhoneResponse = commonApi.callVerifyPhoneNumber(mSessionManager!!.getApiHeader(), countryCode, phoneNumber)

                }else
                    (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
            }

        }

        return verifyPhoneResponse

    }

    fun callVerifyPhoneNumberOtp(verifyCode: String,phoneNumber:String,mobileCode:String) : MutableLiveData<VerifiedPhoneResponse> {
        var verifyPhoneOtpResponse: MutableLiveData<VerifiedPhoneResponse> = MutableLiveData()
        when {
            verifyCode.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.otp_error))
            verifyCode.length < 6 -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.otp_error))
            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                verifyPhoneOtpResponse =  commonApi.callVerifyPhoneNumberOtp(mSessionManager!!.getApiHeader(),verifyCode,phoneNumber,mobileCode)
            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return verifyPhoneOtpResponse

    }

    fun callGoogleConnect(googleId: String) : MutableLiveData<CommonResponse> {
        var googleConnectResponse: MutableLiveData<CommonResponse> = MutableLiveData()
        when {

            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                googleConnectResponse =  commonApi.callGoogleConnect(mSessionManager!!.getApiHeader(),googleId)
            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return googleConnectResponse

    }
    fun callGoogleDisConnect(googleId: String) : MutableLiveData<CommonResponse> {
        var googleConnectResponse: MutableLiveData<CommonResponse> = MutableLiveData()
        when {

            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                googleConnectResponse =  commonApi.callGoogleDisConnect(mSessionManager!!.getApiHeader(),googleId)
            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return googleConnectResponse

    }
}