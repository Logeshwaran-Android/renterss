package com.rent.renters.app.ui.filter

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util

class FilterViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private var filterResponse: MutableLiveData<MoreFilterResponse> = MutableLiveData()
    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()

    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }
    fun callGetMoreFilterItems(id:String): MutableLiveData<MoreFilterResponse> {

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            filterResponse = commonApi.callGetMoreFilterItems(mSessionManager!!.getApiHeader(),id)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return filterResponse

    }

}

