package com.rent.renters.app.ui.profile


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

class ProfileViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity : Activity
    private var profileImageResponse: MutableLiveData<ProfileImageResponse> = MutableLiveData()
    private var languageResponse: MutableLiveData<LanguageResponse> = MutableLiveData()
    private var timeZoneResponse: MutableLiveData<TimeZoneResponse> = MutableLiveData()
    private var profileResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    private val commonApi : CommonApiRepository = CommonApiRepository.getInstance()

    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun getProfileDetails(userId:String) : MutableLiveData<ProfileResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            profileResponse = commonApi.callGetProfile(mSessionManager!!.getApiHeader(),userId)

        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return profileResponse

    }

    fun getTimeZone() : MutableLiveData<TimeZoneResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            timeZoneResponse = commonApi.callGetTimeZone(mSessionManager!!.getApiHeader())
        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return timeZoneResponse

    }
    fun getLanguage() : MutableLiveData<LanguageResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            languageResponse = commonApi.callGetUserLanguage(mSessionManager!!.getApiHeader())
        }
        else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }


        return languageResponse

    }

    fun callUpdateProfileImage(imageFile : File) : MutableLiveData<ProfileImageResponse> {
        if(Util.isNetworkAvailable(mActivity!!)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            profileImageResponse = commonApi.callUpdateProfileImage(mSessionManager!!.getApiHeader(), imageFile)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return profileImageResponse

    }
    fun callUpdateProfile(firstName : String, lastName: String, gender: String,dob:String, about:String, city:String, timeZone:String,langKnown:String ) : MutableLiveData<ProfileResponse> {
        when {
            firstName.isEmpty() ->
                (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.first_name_err))
            Util.isNetworkAvailable(mActivity) -> {
                (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
                profileResponse = commonApi.callUpdateProfile(mSessionManager!!.getApiHeader(), firstName, lastName, gender, dob, about, city, timeZone, langKnown)
            }
            else ->
                (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }
        return profileResponse

    }



}