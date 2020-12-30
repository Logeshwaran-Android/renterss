package com.rent.renters.app.ui.reviews

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R
import com.rent.renters.app.data.model.ReviewAverageCategoryItem
import com.rent.renters.app.data.model.ReviewsListData
import com.rent.renters.app.data.model.ReviewsResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.base.PaginationScrollListener

import kotlinx.android.synthetic.main.activity_reviews_list.*
import kotlinx.android.synthetic.main.activity_space_item_list.*
import kotlinx.android.synthetic.main.header_layout.imgBtnBack
import java.util.ArrayList

class DetailPageReviewsListActivity : BaseActivity() {

    private var mReviewsListAdapter: DetailPageReviewsListAdapter? = null
    private var mReviewsAverageCategoryAdapter: ReviewAverageCategoryAdapter? = null

    private val mReviewsList =  ArrayList<ReviewsListData>()
    private val mReviewsCategoryList =  ArrayList<ReviewAverageCategoryItem>()
    private lateinit var reviewsViewModel: ReviewsViewModel
    private var mId : String = ""
    private var mIsFrom : String = ""
    private var mPage : Int = 1


    private var mTotalItemCount: Int = 1

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews_list)
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.initMethod(this)
        callGetReviewList()

    }

    private fun callGetReviewList(){
        if(mIsFrom.equals(Iconstants.PROPERTY,true)) {
            reviewsViewModel.callGetPropertyReviewsList(mId, mPage).observe(this, Observer<ReviewsResponse> {
                baseViewModel.rentersLoader.postValue(false)
                isLoading = false
                if (it.status == "1") {
                    val mReviewCount = it.data?.review?.tot_review!!.toInt()
                    if(mReviewCount >= 5)
                    mTotalItemCount = it.data?.review?.tot_review!!.toInt() / 5
                    mReviewsList.addAll(it.data?.review?.review_det!!)
                    it.data?.review?.tot_avg_review?.let {
                        tvTotalReview.text = String.format("%.1f", it.toDouble()).plus(" (").plus(mReviewCount).plus(" ").plus(getString(R.string.reviews)).plus(")")
                    }
                    setReviewsAdapter()
                    it.data?.review?.review_cat?.let{
                        mReviewsCategoryList.clear()
                        mReviewsCategoryList.addAll(it)
                        setReviewsAverageCategroyAdapter(Iconstants.PROPERTY)

                    }
                }
            })
        } else if(mIsFrom.equals(Iconstants.USER,true)){
            reviewsViewModel.callGetUserReviewsList(mId, mPage).observe(this, Observer<ReviewsResponse> {
                baseViewModel.rentersLoader.postValue(false)
                isLoading = false
                if (it.status == "1") {
                    var mReviewCount = 0
                    if(it.data?.review?.tot_review!!.isNotEmpty())
                     mReviewCount = it.data?.review?.tot_review!!.toInt()
                    if(mReviewCount >= 5)
                    mTotalItemCount = it.data?.review?.tot_review!!.toInt() / 5
                    mReviewsList.addAll(it.data?.review?.review_det!!)
                    it.data?.review?.tot_avg_review?.let {
                        tvTotalReview.text = String.format("%.1f", it.toDouble()).plus(" (").plus(mReviewCount).plus(" ").plus(getString(R.string.reviews)).plus(")")
                    }


                    setReviewsAdapter()
                    it.data?.review?.review_det!![0]?.review_cat?.let{
                        mReviewsCategoryList.clear()
                        mReviewsCategoryList.addAll(it)
                        setReviewsAverageCategroyAdapter(Iconstants.USER)

                    }
                }
            })
        }
    }

    private fun setReviewsAverageCategroyAdapter(isFrom : String) {
        if(mReviewsAverageCategoryAdapter == null){
            mReviewsAverageCategoryAdapter = ReviewAverageCategoryAdapter(this,mReviewsCategoryList)
            rvReviewsAverage.adapter = mReviewsAverageCategoryAdapter
        }else{
            mReviewsAverageCategoryAdapter!!.notifyDataSetChanged()
        }

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID))
                mId = intent.getStringExtra(Iconstants.ID)!!
            if(intent.hasExtra(Iconstants.IS_FROM))
            mIsFrom = intent.getStringExtra(Iconstants.IS_FROM)!!
        }

       // tvTitle.text = getString(R.string.reviews)
        imgBtnBack.setOnClickListener{finish()}
       // imgBtnBack.setImageResource(R.drawable.ic_close)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvReviews.layoutManager = layoutManager

        val layoutManager1 = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvReviewsAverage.layoutManager = layoutManager1


        rvSpaceTypeItems?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if(mPage >= mTotalItemCount){
                    isLastPage = true
                }
                mPage += 1
                //you have to call loadmore items to get more data
                callGetReviewList()
            }
        })
    }


    private fun setReviewsAdapter() {
        if (mReviewsListAdapter == null) {
            mReviewsListAdapter = DetailPageReviewsListAdapter(this,mReviewsList)
            rvReviews.adapter = mReviewsListAdapter
        } else {
            mReviewsListAdapter!!.notifyDataSetChanged()
        }
    }
}
