package com.rent.renters.app.ui.transactionHistory

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.TransactionListResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util


class TransactionViewModel : BaseViewModel() {

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null
    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }


    fun callGetTransactionList(type:String): MutableLiveData<TransactionListResponse> {
        var commonResponse: MutableLiveData<TransactionListResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callGetTransactionList(mSessionManager!!.getApiHeader(),(type))
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }
    fun callGetTransactionFilterList(type:String,keyword:String,fromDate:String,toDate:String,searchType:String): MutableLiveData<TransactionListResponse> {
        var commonResponse: MutableLiveData<TransactionListResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callGetTransactionFilterList(mSessionManager!!.getApiHeader(), type,keyword,fromDate,toDate,searchType)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }
    fun callRequestEarnings(): MutableLiveData<CommonResponse> {
        var commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            commonResponse = commonApi.callRequestEarnings(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return commonResponse

    }
}
