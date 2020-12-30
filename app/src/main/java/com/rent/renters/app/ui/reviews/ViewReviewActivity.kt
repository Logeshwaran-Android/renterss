package com.rent.renters.app.ui.reviews

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.inbox.ChatViewModel
import kotlinx.android.synthetic.main.activity_view_review.*
import kotlinx.android.synthetic.main.activity_view_review.ratingBarReviews
import kotlinx.android.synthetic.main.activity_view_review.rvReviewCategory
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat


class ViewReviewActivity : BaseActivity() {

    private lateinit var reviewsViewModel: ReviewsViewModel
    private var mBookingNo =""
    private lateinit var chatViewModel: ChatViewModel

    private var mReviewCategoryList = ArrayList<ReviewCategoryItem>()
    private var mReviewCategoryAdapter: ReviewCategoryAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_review)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.initMethod(this)
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.initMethod(this)

        reviewsViewModel.callGetBookingReview(mBookingNo).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)

            if(it.status == "1")
                setReviewData(it.data?.reviewDet)
            else
                baseViewModel.rentersError.postValue(it.message)
        })
        callGetBookingDetails()

    }

    private fun callGetBookingDetails() {
        chatViewModel.callGetBookingDetails(mBookingNo).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                setBookingData(it)
            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
        })
    }

    private fun setBookingData(it: BookingDetailsResponse?) {

        it?.data?.details?.let {
            tvSpaceName.text = it.property_name
            loadImageWithGlide(ivSpace,it.PropImage,R.drawable.ic_default_circle_profile_img)
            try {
                val checkin = SimpleDateFormat("yyyy-MM-dd").parse(it.check_in!!)
                val checkout = SimpleDateFormat("yyyy-MM-dd").parse(it.check_out!!)
                tvBookedDate.text = SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            tvBookingNo.text = it.booking_no


        }
        it?.owner_details?.let {
            tvHostRating.text = getString(R.string.exp_shared).plus(" ").plus(it.firstname).plus(" ! ")
        }

    }
    private fun setReviewCategoryAdapter() {
        if(mReviewCategoryAdapter == null){
            mReviewCategoryAdapter = ReviewCategoryAdapter(this, mReviewCategoryList,Iconstants.VIEW_REVIEW,null)
            rvReviewCategory.adapter = mReviewCategoryAdapter
        } else
            mReviewCategoryAdapter!!.notifyDataSetChanged()

    }


    private fun setReviewData(data: ViewBookingReviewDetails?) {
        tvPublicReviewVal.text = data?.review_description
        if(data?.review_rating !=null)
        ratingBarReviews.rating = data.review_rating!!.toFloat()
        data?.review_category?.let{
            mReviewCategoryList.clear()
            mReviewCategoryList.addAll(it)
            setReviewCategoryAdapter()
        }

    }


    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo= intent.getStringExtra(Iconstants.BOOKING_NO)!!
        }
        tvTitle.text = getString(R.string.your_public_review)
        imgBtnBack.setOnClickListener{finish()}

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvReviewCategory.layoutManager = layoutManager
    }

}
