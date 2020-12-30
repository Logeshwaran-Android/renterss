package com.rent.renters.app.ui.reviews

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.ReviewCategoryResponse
import com.rent.renters.app.data.model.ReviewsResponse
import com.rent.renters.app.data.model.ViewBookingReviewResponse
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util



class ReviewsViewModel : BaseViewModel() {

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null


    private var reviewsResponse: MutableLiveData<ReviewsResponse> = MutableLiveData()

    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }


    fun callGetReviewsList(userId:String, page:Int) : MutableLiveData<ReviewsResponse> {
        var profileReviewsResponse: MutableLiveData<ReviewsResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)

            profileReviewsResponse = commonApi.callGetReviews(mSessionManager!!.getApiHeader(),userId,page)

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return profileReviewsResponse

    }

    fun callGetReviewCategory() : MutableLiveData<ReviewCategoryResponse> {
        var reviewCategoryResponse: MutableLiveData<ReviewCategoryResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)

            reviewCategoryResponse = commonApi.callGetReviewCategory(mSessionManager!!.getApiHeader())

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return reviewCategoryResponse

    }


    fun callGetPropertyReviewsList(propId:String, page: Int) : MutableLiveData<ReviewsResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)

            reviewsResponse = commonApi.callGetPropertyReviews(mSessionManager!!.getApiHeader(),propId,page)

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return reviewsResponse

    }
    fun callGetUserReviewsList(userId:String, page: Int) : MutableLiveData<ReviewsResponse> {

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)

            reviewsResponse = commonApi.callGetUserReviews(mSessionManager!!.getApiHeader(),userId,page)

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return reviewsResponse

    }

    fun callPostReview(propId: String,bookingNo:String,reviewDescription:String,reviewRating:String,reviewCat :String,privateReview:String) : MutableLiveData<CommonResponse> {
        var reviewsResponse: MutableLiveData<CommonResponse> = MutableLiveData()
        if(reviewDescription.isEmpty())
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.review_desc_err))
        else if(reviewRating.isEmpty() || reviewRating.equals("0.0",true))
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.review_rating_err))

        else if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            reviewsResponse = commonApi.callPostReview(mSessionManager!!.getApiHeader(),propId,reviewRating,reviewDescription,bookingNo,reviewCat,privateReview)

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return reviewsResponse

    }

    fun callGetBookingReview(bookingNo:String) : MutableLiveData<ViewBookingReviewResponse> {
        var reviewsResponse: MutableLiveData<ViewBookingReviewResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)

            reviewsResponse = commonApi.callGetBookingReview(mSessionManager!!.getApiHeader(),bookingNo)

        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return reviewsResponse

    }
}