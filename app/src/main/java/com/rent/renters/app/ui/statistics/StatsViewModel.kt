package com.rent.renters.app.ui.statistics

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.StatsResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel


class StatsViewModel : BaseViewModel() {


    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null





    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }
    fun callGetHostStats():MutableLiveData<StatsResponse>{
        var response: MutableLiveData<StatsResponse> = MutableLiveData()
        if (com.rent.renters.mylibrary.util.Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetHostStats(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return response
    }
}
