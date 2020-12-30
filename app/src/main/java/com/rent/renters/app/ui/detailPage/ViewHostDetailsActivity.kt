package com.rent.renters.app.ui.detailPage


import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.ProfileResponse
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.reviews.DetailPageReviewsListActivity
import com.rent.renters.app.ui.reviews.EditProfileReviewsActivity
import com.rent.renters.app.ui.userHomePage.SimilarSpaceTypeItemAdapter
import kotlinx.android.synthetic.main.activity_host_details.*
import kotlinx.android.synthetic.main.activity_host_details.clReviews
import kotlinx.android.synthetic.main.activity_host_details.imgBtnBack
import kotlinx.android.synthetic.main.activity_host_details.ivReviewProfile
import kotlinx.android.synthetic.main.activity_host_details.tvReviewDate
import kotlinx.android.synthetic.main.activity_host_details.tvReviewName
import kotlinx.android.synthetic.main.activity_host_details.tvReviews
import kotlinx.android.synthetic.main.activity_host_details.tvReviewsDesc
import java.text.SimpleDateFormat

import java.util.ArrayList

class ViewHostDetailsActivity : BaseActivity(), View.OnClickListener,SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener {
    override fun spaceTypeItemClick(item: SearchSpaceData) {
        val detailPageIntent = Intent(this, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID,item.id)
        startActivity(detailPageIntent)
    }

    private lateinit var detailPageViewModel: DetailPageViewModel
    private var spaceTypeItemAdapter: SimilarSpaceTypeItemAdapter? = null


    private var mSpaceTypeList = ArrayList<SearchSpaceData>()
    private lateinit var mProfileResponse : ProfileResponse
    private var mUserId : String = ""
    private lateinit var mListener : SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_details)
        mListener   = this
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        detailPageViewModel = ViewModelProvider(this).get(DetailPageViewModel::class.java)
        detailPageViewModel.initMethod(this)
        detailPageViewModel.callGetHostProfileDetails(mUserId).observe(this, Observer<ProfileResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {
                    mProfileResponse = it
                    setProfileData(mProfileResponse)
                    mSpaceTypeList.clear()
                    if(it.data?.listing_details!=null)
                        mSpaceTypeList.addAll(it.data?.listing_details!!)


                }

            }
        })
    }

    private fun setProfileData(mProfileResponse: ProfileResponse) {
        tvHostName.text = mProfileResponse.data?.UserDetails?.firstname
        loadCircleImageWithGlide(ivHost,mProfileResponse.data?.UserDetails?.profile_image,R.drawable.ic_default_circle_profile_img)
        if( mProfileResponse.data?.UserDetails?.address!!.isNotEmpty()) {
            tvCity.text = mProfileResponse.data?.UserDetails?.address
        } else{
            tvCity.visibility = View.GONE
        }
        mProfileResponse.data?.responserate?.let{
            tvResponseRateVal.text = it.plus("%")
        }
        mProfileResponse.data?.responsetime?.let{
            if(it.contains("within",true))
            tvResponseTimeVal.text = it
            else
                tvResponseTimeVal.text = getString(R.string.within).plus(" ").plus(it)
        }
        var mStatus = ""
        if(mProfileResponse.data?.UserDetails?.is_verified.equals("yes",true))
            mStatus = getString(R.string.verified)

        if(mProfileResponse.data?.UserDetails?.about_you!!.isNotEmpty()) {
            tvAboutHost.text = mProfileResponse.data?.UserDetails?.about_you
        } else{
            viewAboutHost.visibility = View.GONE
            tvAboutHost.visibility = View.GONE
            tvAboutHost.text = mProfileResponse.data?.UserDetails?.about_you
        }
        mProfileResponse.data?.UserDetails?.created?.let {
            val mTempDate = SimpleDateFormat("yyyy-MM-dd").parse(mProfileResponse.data?.UserDetails?.created)
            val memberDate= SimpleDateFormat("MMMM yyyy").format(mTempDate!!)
            tvMember.text = getString(R.string.member_since).plus(" ").plus(memberDate)

        }

        tvLanguageVal.text = mProfileResponse.data?.user_language

        if(mProfileResponse.data?.reviews!!.size > 0){
            clReviews.visibility = View.VISIBLE
            loadImageWithGlide(ivReviewProfile, mProfileResponse.data?.reviews!![0].profile_image,R.drawable.ic_default_profile_img)
            tvReviewName.text = mProfileResponse.data?.reviews!![0].firstname!!
            tvReviewsDesc.text = mProfileResponse.data?.reviews!![0].review_description
            if(mProfileResponse.data?.reviews!![0].dateAdded!!.isNotEmpty()) {
                val reviewDate = SimpleDateFormat("yyyy-MM-dd").parse(mProfileResponse.data?.reviews!![0].dateAdded)
                tvReviewDate.text = SimpleDateFormat("MMMM yyyy").format(reviewDate!!)
            }

            if(mProfileResponse.data?.review_count?.toInt()!! >= 1) {
                tvReviewsCount.visibility = View.VISIBLE
                if(mProfileResponse.data?.review_count?.toInt()!! == 1){
                    tvReviewsCount.text =(mProfileResponse.data?.review_count).plus(" ").plus(getString(R.string.review))
                }else
                tvReviewsCount.text =(mProfileResponse.data?.review_count).plus(" ").plus(getString(R.string.reviews))
                tvSeeAllReviews.visibility = View.VISIBLE
                viewSeeAllReview.visibility = View.VISIBLE
            }
            else {
                tvReviewsCount.visibility = View.GONE
                tvSeeAllReviews.visibility = View.GONE
                viewSeeAllReview.visibility = View.GONE
            }


        }else{
            clReviews.visibility = View.GONE
            tvSeeAllReviews.text = getString(R.string.no_reviews_yet)
            tvSeeAllReviews.isClickable = false
        }

        if(mProfileResponse.data?.listing_count!!.toInt() > 0){
            tvSpace.visibility = View.VISIBLE
            tvSpace.text = mProfileResponse.data?.listing_count.plus(" ").plus(getString(R.string.space))
            setSpaceTypeItemAdapter()
        } else{
            tvSpace.visibility = View.GONE
        }
        if(mProfileResponse.data?.reviews!!.size > 0) {
            if(mStatus.isNotEmpty()) {
                val mTempText = (mProfileResponse.data?.review_count).plus(" ").plus(getString(R.string.reviews)).plus(" - ").plus(mStatus)
                tvReviews.text = (spannableColorString(mTempText,mStatus))
            }
            else
                tvReviews.text = (mProfileResponse.data?.review_count).plus(" ").plus(getString(R.string.reviews))

        }else {

            if(mStatus.isNotEmpty()) {
                val mTempText = (getString(R.string.no_reviews)).plus(" ").plus(" - ").plus(mStatus)
                tvReviews.text = (spannableColorString(mTempText,mStatus))
            }
            else
                tvReviews.text = (getString(R.string.no_reviews))
        }

        var mVerifiedVal = ""
        if(mProfileResponse.data?.UserDetails?.phone_verified.equals(getString(R.string.yes),true))
            mVerifiedVal = getString(R.string.phone_number)
        if(mProfileResponse.data?.UserDetails?.email_verified.equals(getString(R.string.yes),true)) {
            if(mVerifiedVal.isNotEmpty())
            mVerifiedVal = mVerifiedVal.plus(",")
            mVerifiedVal = mVerifiedVal.plus(getString(R.string.email))
        }
        tvVerifiedVal.text = mVerifiedVal

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID))
                mUserId = intent.getStringExtra(Iconstants.ID)!!
        }


        tvSeeAllReviews.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSpaces.layoutManager = layoutManager
        setSpaceTypeItemAdapter()
    }

    private fun setSpaceTypeItemAdapter() {
        if (spaceTypeItemAdapter == null) {
            spaceTypeItemAdapter = SimilarSpaceTypeItemAdapter(this, mListener ,mSpaceTypeList)
            rvSpaces.adapter = spaceTypeItemAdapter
        } else {
            spaceTypeItemAdapter!!.notifyDataSetChanged()
        }
    }



    override fun onClick(view: View?) {
        when(view?.id){
            R.id.imgBtnBack -> finish()
            R.id.tvSeeAllReviews ->{
                val reviewsListIntent = Intent(this, EditProfileReviewsActivity::class.java)
                reviewsListIntent.putExtra(Iconstants.ID,mUserId)
                reviewsListIntent.putExtra(Iconstants.IS_FROM,Iconstants.HOST)
                startActivity(reviewsListIntent)
            }
        }

    }
    override fun addRemoveWishList(item: SearchSpaceData,position: Int) {
        detailPageViewModel.callAddRemoveWishList(item.id!!).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSpaceTypeList[position].is_favorite = it.data?.is_favorite!!
                setSpaceTypeItemAdapter()

            }
            baseViewModel.rentersError.postValue(it.message)
        })

    }
}
