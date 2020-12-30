package com.rent.renters.app.ui.registration

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.ForgotPasswordResponse
import com.rent.renters.app.data.model.RegistrationResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class RegistrationViewModel : BaseViewModel() {

    private var registrationResponse: MutableLiveData<RegistrationResponse> = MutableLiveData()

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity

    fun initMethod(activity: Activity) {
        mActivity = activity

    }

    fun callRegistration(firstName: String, lastName: String, email: String, checkPolicy: String, password: String, confirmPassword: String): MutableLiveData<RegistrationResponse> {
        if (password.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_err))
        else if (confirmPassword.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_err))
        else if (!confirmPassword.equals(password, true))
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.confirm_pw_error_message))
        else if (!Util.isValidPassword(password))
                (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_error_message))
         else if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            registrationResponse = commonApi.callRegistration(firstName, lastName, email, checkPolicy, password)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return registrationResponse

    }

    fun callForgotPassword(email: String): MutableLiveData<ForgotPasswordResponse> {
        var forgotPasswordResponse: MutableLiveData<ForgotPasswordResponse> = MutableLiveData()
        if (email.isEmpty() || !Util.isValidEmail(email)) {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.valid_email_err))
        } else if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            forgotPasswordResponse = commonApi.callForgotPassword(email)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return forgotPasswordResponse

    }

    fun callVerifyEmailAddress(email : String) : MutableLiveData<CommonResponse>{
        var response: MutableLiveData<CommonResponse> = MutableLiveData()
      if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callVerifyEmailAddress(email)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return response

    }

}