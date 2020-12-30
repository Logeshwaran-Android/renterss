package com.rent.renters.app.ui.reviews

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.BookingDetailsResponse
import com.rent.renters.app.data.model.ReviewCategoryItem
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.inbox.ChatViewModel
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.android.synthetic.main.header_layout.*
import java.lang.Exception
import java.text.SimpleDateFormat

class AddReviewActivity : BaseActivity(), View.OnClickListener,ReviewCategoryAdapter.ReviewCategoryInterface {

    private lateinit var reviewsViewModel: ReviewsViewModel
    private lateinit var chatViewModel: ChatViewModel
    private var mReviewCategoryList = ArrayList<ReviewCategoryItem>()
    private var mSelectedReviewCategoryVal =""
    private var mPropId =""
    private var mBookingNo =""
    private var mReviewCategoryAdapter: ReviewCategoryAdapter ?= null
    private lateinit var mListener :ReviewCategoryAdapter.ReviewCategoryInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        mListener = this
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.initMethod(this)
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.initMethod(this)
        callGetReviewCategory()
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
            tvHostRating.text = getString(R.string.let).plus(" ").plus(it?.firstname).plus(" , ").plus(getString(R.string.know_your_exp))
        }

    }


    private fun callGetReviewCategory(){
        reviewsViewModel.callGetReviewCategory().observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            it?.data?.review_category?.let{
                mReviewCategoryList.clear()
                mReviewCategoryList.addAll(it)
                setReviewCategoryAdapter()
            }
        })

    }

    private fun setReviewCategoryAdapter() {
        if(mReviewCategoryAdapter == null){
            mReviewCategoryAdapter = ReviewCategoryAdapter(this, mReviewCategoryList,Iconstants.ADD_REVIEW,mListener)
            rvReviewCategory.adapter = mReviewCategoryAdapter
        } else
            mReviewCategoryAdapter!!.notifyDataSetChanged()

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID))
                mPropId= intent.getStringExtra(Iconstants.ID)!!
            if(intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo= intent.getStringExtra(Iconstants.BOOKING_NO)!!
        }
        tvTitle.text = getString(R.string.review)
        imgBtnBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvReviewCategory.layoutManager = layoutManager

    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.imgBtnBack -> finish()
            R.id.btnSave ->{
                reviewsViewModel.callPostReview(mPropId,mBookingNo,etWriteReview.text.toString(),ratingBarReviews.rating.toString(),getSelectedReviewCategory(),etPrivateReview.text.toString()).observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                    if(it.status == "1") {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })

            }
        }

    }

    private fun getSelectedReviewCategory():String{
        mSelectedReviewCategoryVal = ""
        for(i in 0 until mReviewCategoryList.size){
            if(i == mReviewCategoryList.size-1)
                mSelectedReviewCategoryVal = mSelectedReviewCategoryVal.plus(mReviewCategoryList[i].id).plus("-").plus(mReviewCategoryList[i].rating)
            else
            mSelectedReviewCategoryVal = mSelectedReviewCategoryVal.plus(mReviewCategoryList[i].id).plus("-").plus(mReviewCategoryList[i].rating).plus(",")
        }
        return mSelectedReviewCategoryVal

    }

    override fun onReviewCategoryItemClick(item: ReviewCategoryItem, listPosition: Int) {
        mReviewCategoryList[listPosition] = item

    }

}
