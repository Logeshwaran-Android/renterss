package com.rent.renters.app.ui.login



import android.app.Activity

import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.LoginResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class LoginViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity : Activity

    private var loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()

    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }


    fun postLogin(email: String, pw : String) : MutableLiveData<LoginResponse> {
        if (email.isEmpty()) {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.email_err))

        } else if (!Util.isValidEmail(email)) {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.valid_email_err))

        } else if (pw.isEmpty()){
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.password_err))
    }
        else if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            loginResponse = commonApi.postLogin(email, pw)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        }

        return loginResponse


    }

}