package com.rent.renters.app.ui.signUpSignInPage

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.LoginResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class GoogleSignInViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private var loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()

    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(mActivity)
    }


    fun callGoogleRegistration(email: String, firstName : String,lastName : String,googleImage : String) : MutableLiveData<LoginResponse> {
        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            loginResponse = commonApi.callGoogleRegistration(email, firstName,lastName, googleImage)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return loginResponse

    }

    fun callVerifyEmailId(userId: String, code : String) : MutableLiveData<CommonResponse> {
         var loginResponse: MutableLiveData<CommonResponse> = MutableLiveData()
        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            loginResponse = commonApi.callVerifyEmailId(mSessionManager!!.getApiHeader(),userId,code)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return loginResponse

    }


}