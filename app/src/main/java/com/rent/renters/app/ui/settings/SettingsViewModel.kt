package com.rent.renters.app.ui.settings

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.CurrencyResponse
import com.rent.renters.app.data.model.NotificationResponse
import com.rent.renters.app.data.model.PayoutPreferenceResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class SettingsViewModel : BaseViewModel() {

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null


    private var currencyResponse: MutableLiveData<CurrencyResponse> = MutableLiveData()
    private var notificationResponse: MutableLiveData<NotificationResponse> = MutableLiveData()



    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }


    fun callDeActivateAccount(): MutableLiveData<CommonResponse> {
         var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callDeActivateAccount(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }

    fun callGetCurrencyList(): MutableLiveData<CurrencyResponse> {

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            currencyResponse = commonApi.callGetCurrencyList(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return currencyResponse

    }

    fun callUpdatePassword(userId:String,newPassword: String, confirmPassword: String): MutableLiveData<CommonResponse> {
        var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        if (newPassword.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_err))
        else if (!Util.isValidPassword(newPassword))
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_error_message))
        else if (confirmPassword.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_err))
        else if (!confirmPassword.equals(newPassword, true)) {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_error_message))
        } else if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callUpdatePassword(mSessionManager!!.getApiHeader(),  newPassword, userId)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse
    }

    fun callChangePassword(currentPassword: String, newPassword: String, confirmPassword: String): MutableLiveData<CommonResponse> {
        var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        if (newPassword.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_err))
        else if (!Util.isValidPassword(newPassword))
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_error_message))
        else if (confirmPassword.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_err))
        else if (!confirmPassword.equals(newPassword, true)) {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_error_message))
        } else if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callChangePassword(mSessionManager!!.getApiHeader(), currentPassword, newPassword)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse
    }

    fun callUserNotification(): MutableLiveData<NotificationResponse> {

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            notificationResponse = commonApi.callUserNotification(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return notificationResponse

    }

    fun callLogout(): MutableLiveData<CommonResponse> {
        var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callLogout(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }

    fun callUpdateUserNotification(emailNotify: Boolean, textNotify: Boolean, pushNotify: Boolean): MutableLiveData<CommonResponse> {
        var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()
        var mEmailNotify = "No"
        var mTextNotify = "No"
        var mPushNotify = "No"

        if (emailNotify)
            mEmailNotify = "Yes"
        if (textNotify)
            mTextNotify = "Yes"
        if (pushNotify)
            mPushNotify = "Yes"

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callUpdateUserNotification(mSessionManager!!.getApiHeader(), mEmailNotify, mTextNotify, mPushNotify)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }

    fun callAddPayoutPreference(accountName:String,accountNumber:String,bankName:String): MutableLiveData<PayoutPreferenceResponse> {
        var commonResponse: MutableLiveData<PayoutPreferenceResponse> = MutableLiveData()

        when {
            accountName.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.acc_name_err))
            accountNumber.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.acc_number_err))
            bankName.isEmpty() -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.bank_name_err))
            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                commonResponse = commonApi.callAddPayoutPreference(mSessionManager!!.getApiHeader(),accountName,accountNumber,bankName)
            }
            else -> (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return commonResponse

    }

    fun callViewPayoutPreference(): MutableLiveData<PayoutPreferenceResponse> {
        var commonResponse: MutableLiveData<PayoutPreferenceResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callViewPayoutPreference(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }
}