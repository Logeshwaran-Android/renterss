package com.rent.renters.app.ui.reviews



import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.ReviewsListData
import com.rent.renters.app.data.model.ReviewsResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_edit_profile_reviews.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.util.ArrayList

class EditProfileReviewsActivity : BaseActivity() {

    private var mReviewsListAdapter: EditProfileReviewsListAdapter? = null

    private var mFromPage : String ?= ""

    private var mUserId =""

    private val mReviewsList :ArrayList<ReviewsListData> =  ArrayList<ReviewsListData>()
    private var mSessionManager: SessionManager? = null
    private lateinit var reviewsViewModel: ReviewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_reviews)
        mSessionManager= SessionManager(this)
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.initMethod(this)

        if(mFromPage.equals(getString(R.string.reviews_about_you)) || mFromPage == Iconstants.REVIEWS) {
            reviewsViewModel.callGetUserReviewsList(mUserId,1).observe(this, Observer<ReviewsResponse> {
                baseViewModel.rentersLoader.postValue(false)

                if (it.status == "1") {
                    mReviewsList.clear()
                    if(it.data?.review?.review_det!!.size > 0) {
                        mReviewsList.addAll(it.data?.review?.review_det!!)

                        if (mReviewsList.size == 0) {
                            noDataLayout.visibility = View.VISIBLE
                            btnStartExploring.visibility = View.GONE

                        } else {
                            noDataLayout.visibility = View.GONE
                            setReviewsAdapter()

                        }
                    }else{
                        noDataLayout.visibility = View.VISIBLE
                        btnStartExploring.visibility = View.GONE
                    }

                }

            })

        } else if(mFromPage.equals(getString(R.string.reviews_by_you))) {
            reviewsViewModel.callGetReviewsList(mSessionManager!!.getUserDetails().id,1).observe(this, Observer<ReviewsResponse> {
                baseViewModel.rentersLoader.postValue(false)

                if (it.status == "1") {
                    if(it.data?.review?.review_det!!.size > 0) {
                        mReviewsList.clear()
                        mReviewsList.addAll(it.data?.review?.review_det!!)

                        if (mReviewsList.size == 0) {
                            noDataLayout.visibility = View.VISIBLE
                            btnStartExploring.visibility = View.GONE

                        } else {
                            noDataLayout.visibility = View.GONE
                            setReviewsAdapter()

                        }
                    } else{
                        noDataLayout.visibility = View.VISIBLE
                        btnStartExploring.visibility = View.GONE

                    }
                }

            })
        }



    }

    private fun initView() {

        imgBtnBack.setImageResource(R.drawable.ic_arrow_back_black)
        imgBtnBack.setOnClickListener{finish()}
        mUserId = mSessionManager!!.getUserDetails().id

        if(intent!=null){
            if(intent.hasExtra(Iconstants.IS_FROM)) {
                mFromPage = intent.getStringExtra(Iconstants.IS_FROM)
                tvTitle.text = mFromPage
            }
            if(intent.hasExtra(Iconstants.ID)) {
                mUserId = intent.getStringExtra(Iconstants.ID)!!
            }

        }

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvReviews.layoutManager = layoutManager

        setReviewsAdapter()
    }



    private fun setReviewsAdapter() {
        if (mReviewsListAdapter == null) {
            mReviewsListAdapter = EditProfileReviewsListAdapter(this,mReviewsList,mFromPage)
            rvReviews.adapter = mReviewsListAdapter
        } else {
            mReviewsListAdapter!!.notifyDataSetChanged()
        }
    }
}
