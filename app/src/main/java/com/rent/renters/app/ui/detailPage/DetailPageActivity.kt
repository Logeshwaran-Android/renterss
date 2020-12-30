package com.rent.renters.app.ui.detailPage


import android.app.Activity
import android.content.Context
import android.content.Intent

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R

import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.amenities.AmenitiesImageAdapter
import com.rent.renters.app.ui.amenities.AmenitiesListActivity
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.bookingSteps.BookingPageActivity
import com.rent.renters.app.ui.bookingSteps.MessageHostActivity
import com.rent.renters.app.ui.reviews.DetailPageReviewsListActivity
import com.rent.renters.app.ui.userHomePage.SimilarSpaceTypeItemAdapter
import com.rent.renters.app.ui.userHomePage.SpaceCalendarActivity
import kotlinx.android.synthetic.main.activity_detail_page.*
import java.util.ArrayList
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.rent.renters.app.data.model.*
import com.rent.renters.mylibrary.util.Util.Companion.makeTextViewResizable
import kotlinx.android.synthetic.main.activity_detail_page.imgBtnBack
import kotlinx.android.synthetic.main.activity_detail_page.shimmer_view_container
import androidx.core.view.ViewCompat.getMinimumHeight

import com.google.android.material.appbar.AppBarLayout
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.rent.renters.app.ui.adapter.DetailPageImagePagerAdapter
import java.text.SimpleDateFormat


class DetailPageActivity : BaseActivity(), View.OnClickListener, OnMapReadyCallback, SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener {

     private var mIsOwnProperty = false

    override fun spaceTypeItemClick(item: SearchSpaceData) {
        val detailPageIntent = Intent(this, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID, item.id)
        startActivity(detailPageIntent)
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var detailPageViewModel: DetailPageViewModel
    private var spaceTypeItemAdapter: SimilarSpaceTypeItemAdapter? = null
    private var amenitiesImageAdapter: AmenitiesImageAdapter? = null
    private var mSessionManager: SessionManager? = null
    private var mAmenitiesList = ArrayList<MoreFilterData>()

    private lateinit var mListener: SimilarSpaceTypeItemAdapter.SpaceTypeItemClickListener

    private var mSearchSpaceList = ArrayList<SearchSpaceData>()
    private  var mDetailPageResponse: DetailPageResponse = DetailPageResponse("0","empty")
    private var mPropId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        mSessionManager = SessionManager(this)
        mListener = this

        initView()
        initViewModel()

    }

    private fun initViewModel() {
        detailPageViewModel = ViewModelProvider(this).get(DetailPageViewModel::class.java)
        detailPageViewModel.initMethod(this)
        detailPageViewModel.callGetDetailPageDetails(mPropId).observe(this, Observer<DetailPageResponse> {
           // baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {
                    mDetailPageResponse = it

                    mSearchSpaceList.clear()
                    mSearchSpaceList.addAll(it.data?.similar_listing!!)

                    setDetailPageData(mDetailPageResponse)
                    if (mSearchSpaceList.size > 0) {
                        setSpaceTypeItemAdapter()
                    } else {
                        rvSimilarListing.visibility = View.GONE
                        tvSimilarListing.visibility = View.GONE
                    }
                } else{
                    if(shimmer_view_container.visibility == View.VISIBLE) {
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

  /*  override fun onPause() {
        super.onPause()
        shimmer_view_container.stopShimmer()

    }*/
    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()


    }


    private fun setDetailPageData(response: DetailPageResponse?) {

        response!!.data?.propDet?.about_neighborhood?.let{
            if(it.isEmpty()){
                tvAboutNeighborhood.visibility = View.GONE
                tvAboutNeighborhoodView.visibility = View.GONE
                viewNeighborhood.visibility = View.GONE
            }
        }
        response.data?.propDet?.cancellation_policy?.let{
            if(it.trim().isEmpty()){
                tvCancellationPolicy.visibility = View.GONE
                tvReadCancellationPolicy.visibility = View.GONE
                viewCancellationPolicy.visibility = View.GONE
            }
        }

        response.data?.propDet?.space_rules?.let{
            if(it.trim().isEmpty()){
                tvSpaceRules.visibility = View.GONE
                tvReadSpaceRules.visibility = View.GONE
                viewSpaceRules.visibility = View.GONE
            }
        }

        response.data?.propDet?.other_things_to_note?.let{
            if(it.trim().isEmpty()){
                tvOtherThings.visibility = View.GONE
                tvOtherThingsToNoteView.visibility = View.GONE
                viewOtherThingsToNote.visibility = View.GONE
            }
        }


        response!!.data?.propDet?.guest_check_in_form?.let{
            var mTempTxt: String
            if(it.isNotEmpty() && it.trim() != "0") {
                mTempTxt = getString(R.string.check_in).plus(" : ").plus(it)
                response.data?.propDet?.guest_check_in_to?.let {
                    mTempTxt = mTempTxt.plus(" - ").plus(it)
                }
                tvCheckin.text= mTempTxt
            } else{
                tvThingsToKeep.visibility = View.GONE
                tvCheckin.visibility = View.GONE
            }
        }
        response!!.data?.propDet?.guest_check_in_form_before?.let{
            if(it.isNotEmpty() && it.trim() != "0") {
                tvCheckOut.text = getString(R.string.check_out).plus(" : ").plus(it)
            }else{
                tvCheckOut.visibility = View.GONE
                viewThingsToNote.visibility = View.GONE
            }
        }
        response.data?.propDet?.guest_count?.let{
            if(it.isNotEmpty()) {
                when {
                    it.trim() == "0" -> tvGuest.visibility = View.GONE
                    it.trim() == "1" -> tvGuest.text = it.plus(" ").plus(getString(R.string.guest_val))
                    else -> tvGuest.text = it.plus(" ").plus(getString(R.string.guests_val))
                }

            }else{
                tvGuest.visibility = View.GONE
            }
        }
        response.data?.propDet?.bedroom_count?.let{
            if(it.isNotEmpty()) {
                when {
                    it.trim() == "0" -> tvBedroom.visibility = View.GONE
                    it.trim() == "1" -> tvBedroom.text = it.plus(" ").plus(getString(R.string.bedroom))
                    else -> tvBedroom.text = it.plus(" ").plus(getString(R.string.bedrooms))
                }

            }else{
                tvBedroom.visibility = View.GONE
            }
        }

        response.data?.propDet?.bed_count?.let{
            if(it.isNotEmpty()) {
                when {
                    it.trim() == "0" -> tvBeds.visibility = View.GONE
                    it.trim() == "1" -> tvBeds.text = it.plus(" ").plus(getString(R.string.bed))
                    else -> tvBeds.text = it.plus(" ").plus(getString(R.string.beds))
                }

            }else{
                tvBeds.visibility = View.GONE
            }
        }


        if(response!!.data?.ownerDet?.id.equals(mSessionManager?.getUserDetails()!!.id)){
            mIsOwnProperty = true
            tvContactHost.visibility = View.GONE
            tvMessageHost.visibility = View.GONE
            viewContactHost.visibility = View.GONE
        }


        if(response!!.data?.propDet?.is_favorite != null) {
            if (response.data?.propDet?.is_favorite!!) {
                imgBtnFavorite.setImageResource(R.drawable.ic_favorite_red)
            }
            else
                imgBtnFavorite.setImageResource(R.drawable.ic_favorite)
        }

        if(response.data?.propDet?.instant_booking!=null){
            if(response.data?.propDet?.instant_booking.equals("Yes",true))
                imgBtnInstant.visibility = View.VISIBLE
            else
                imgBtnInstant.visibility = View.GONE

        }

        if (response.data?.gallery_image != null) {
            viewPager.adapter = DetailPageImagePagerAdapter(this, response.data?.gallery_image!!)
            viewPager.offscreenPageLimit = response.data?.gallery_image!!.size
            indicator.setViewPager(viewPager)
        }

        if(response.data?.propDet?.summary!!.isNotEmpty()) {
            tvSpace.visibility = View.VISIBLE
            tvSpaceDesc.visibility = View.VISIBLE
            tvSpaceDesc.text = response.data?.propDet?.summary
            if(response.data?.propDet?.summary!!.length > 200 )
                makeTextViewResizable(tvSpaceDesc, 2, getString(R.string.read_more), true)
        }else{
            tvSpace.visibility = View.GONE
            tvSpaceDesc.visibility = View.GONE
        }

        tvPropertyName.text = response.data?.propDet?.product_name
        tvSpaceType.text = response.data?.propDet?.listspace_name
        response.data?.propDet?.listspace_value?.let {
            tvSpaceTypeVal.text = it
        }
        response.data?.propDet?.address?.city?.let{
            tvSpaceCity.text = it

        }
        tvSpaceSizeVal.text = response.data?.propDet?.property_size.plus(getString(R.string.sq_feet))
        val mText = getString(R.string.hosted_by).plus(" ").plus(response.data?.ownerDet?.firstname)
        tvHostedBy.text = spannableColorString(mText,response.data?.ownerDet?.firstname!!)
        loadCircleImageWithGlide(ivProfile, response.data?.ownerDet?.profile_image, R.drawable.ic_default_circle_profile_img)
        if (response.data?.propDet?.about_your_place!!.isNotEmpty()) {
            tvAboutSpace.visibility = View.VISIBLE
            tvAboutSpaceDes.visibility = View.VISIBLE
            tvAboutSpaceDes.text = response.data?.propDet?.about_your_place
            if(response.data?.propDet?.about_your_place!!.length > 200 )
                makeTextViewResizable(tvAboutSpaceDes, 2, getString(R.string.read_more), true)
        }
        else {
            if(response.data?.propDet?.summary!!.isEmpty())
                viewTheSpace.visibility= View.GONE
            tvAboutSpace.visibility = View.GONE
            tvAboutSpaceDes.visibility = View.GONE
        }
        if (response.data?.attributes != null) {
            for (i in 0 until response.data?.attributes!!.size) {
                mAmenitiesList.addAll(response.data?.attributes!![i].values!!)
            }
            setAmenitiesAdapter()
        }

        if (mAmenitiesList.size > 5) {
            tvAmenitiesMore.visibility = View.VISIBLE
            tvAmenitiesMore.text = ("+").plus((
                    (mAmenitiesList.size) - 5).toString())
        } else {
            tvAmenitiesMore.visibility = View.VISIBLE
            tvAmenitiesMore.text = getString(R.string.view)
            tvAmenitiesMore.textSize = 18F
        }

        if (response.data?.review?.review_det!!.size > 0) {
            clReviews.visibility = View.VISIBLE
            loadCircleImageWithGlide(ivReviewProfile, response.data?.review?.review_det!![0].profile_image!!, R.drawable.ic_default_circle_profile_img)
            tvReviewName.text = response.data?.review?.review_det!![0].firstname
            if(response.data?.review?.review_det!![0].dateAdded!!.isNotEmpty()) {
                val reviewDate = SimpleDateFormat("yyyy-MM-dd").parse(response.data?.review?.review_det!![0].dateAdded!!)
                tvReviewDate.text = SimpleDateFormat("MMMM yyyy").format(reviewDate!!)
            }
            tvReviewsDesc.text = response.data?.review?.review_det!![0].review_description

            if (response.data?.review?.review_det!!.size > 1) {
                tvReadAllReviews.visibility = View.VISIBLE
                tvReadAllReviews.text = getString(R.string.read_all).plus(" ").plus(response.data?.review?.tot_review).plus(" ").plus(getString(R.string.reviews))
            } else{
                tvReadAllReviews.visibility = View.GONE
                ivReviews.visibility = View.GONE
            }

        } else {
            tvReadAllReviews.text = getString(R.string.no_reviews_found)
            ivReviews.visibility = View.GONE
            tvReadAllReviews.isClickable = false
            clReviews.visibility = View.GONE
        }

        val mTempText = mSessionManager?.getCurrencySymbol().plus(response.data?.propDet?.base_price).plus(" ").plus(getString(R.string.per_day))
        tvPriceVal.text = spannableBoldString(mTempText, mSessionManager?.getCurrencySymbol().plus(response.data?.propDet?.base_price))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(response.data?.propDet?.latitude!!.toDouble(), response.data?.propDet?.longitude!!.toDouble()), 9.0f))

        val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_circle)
        val markerOptions = MarkerOptions().position(LatLng(response.data?.propDet?.latitude!!.toDouble(), response.data?.propDet?.longitude!!.toDouble()))
                .icon(icon).title(response.data?.propDet?.address?.city)
        googleMap.addMarker(markerOptions)

        if(shimmer_view_container.visibility == View.VISIBLE) {
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
        }
        detailLayout.visibility = View.VISIBLE
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false


    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.ID))
                mPropId = intent.getStringExtra(Iconstants.ID)!!
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ivProfile.setOnClickListener(this)
        tvHostedBy.setOnClickListener(this)
        tvAmenitiesMore.setOnClickListener(this)
        tvReadAllReviews.setOnClickListener(this)
        tvReadSpaceRules.setOnClickListener(this)
        tvReadCancellationPolicy.setOnClickListener(this)
        tvViewAvailability.setOnClickListener(this)
        tvMessageHost.setOnClickListener(this)
        btnRequestMoreInfo.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
        imgBtnShare.setOnClickListener(this)
        imgBtnFavorite.setOnClickListener(this)
        tvAboutNeighborhoodView.setOnClickListener(this)
        tvOtherThingsToNoteView.setOnClickListener(this)

        nestedScrollView.setPadding(0, 0, 0, 80)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSimilarListing.layoutManager = layoutManager
        rvAmenities.layoutManager = layoutManager1
        setAmenitiesAdapter()

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (collapsingToolbar.height + verticalOffset < 2 * getMinimumHeight(collapsingToolbar)) {
                    imgBtnBack.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP)
                    imgBtnShare.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP)
                } else {
                    imgBtnBack.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
                    imgBtnShare.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
                }
            }else {

                if (collapsingToolbar.height + verticalOffset < 2 * getMinimumHeight(collapsingToolbar)) {
                    imgBtnBack.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this, R.color.black), BlendMode.SRC_ATOP)
                    imgBtnShare.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this, R.color.black), BlendMode.SRC_ATOP)
                } else {
                    imgBtnBack.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this, R.color.white), BlendMode.SRC_ATOP)
                    imgBtnShare.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this, R.color.white), BlendMode.SRC_ATOP)

                }
            }
        })

    }

    private fun setAmenitiesAdapter() {
        if (amenitiesImageAdapter == null) {
            amenitiesImageAdapter = AmenitiesImageAdapter(this, mAmenitiesList)
            rvAmenities.adapter = amenitiesImageAdapter
        } else {
            amenitiesImageAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setSpaceTypeItemAdapter() {
        if (spaceTypeItemAdapter == null) {
            spaceTypeItemAdapter = SimilarSpaceTypeItemAdapter(this, mListener, mSearchSpaceList)
            rvSimilarListing.adapter = spaceTypeItemAdapter
        } else {
            spaceTypeItemAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvHostedBy,R.id.ivProfile-> {
                val hostDetailsIntent = Intent(this, ViewHostDetailsActivity::class.java)
                hostDetailsIntent.putExtra(Iconstants.ID, mDetailPageResponse.data?.ownerDet?.id)
                startActivity(hostDetailsIntent)

            }
            R.id.tvAmenitiesMore -> {
                val amenitiesListIntent = Intent(this, AmenitiesListActivity::class.java)
                amenitiesListIntent.putExtra(Iconstants.AMENITIES_LIST, mDetailPageResponse.data?.attributes)
                startActivity(amenitiesListIntent)

            }
            R.id.tvReadAllReviews -> {
                val reviewsListIntent = Intent(this, DetailPageReviewsListActivity::class.java)
                reviewsListIntent.putExtra(Iconstants.ID, mDetailPageResponse.data?.propDet?.id)
                reviewsListIntent.putExtra(Iconstants.IS_FROM, Iconstants.PROPERTY)
                startActivity(reviewsListIntent)

            }
            R.id.tvReadSpaceRules -> {
                val spaceRulesIntent = Intent(this, SpaceRulesActivity::class.java)
                spaceRulesIntent.putExtra(Iconstants.SPACE_RULES, mDetailPageResponse.data?.propDet?.space_rules)
                spaceRulesIntent.putExtra(Iconstants.SPACE_TITLE, mDetailPageResponse.data?.ownerDet?.firstname.plus(" ").plus(getString(R.string.space_rules)))
                startActivity(spaceRulesIntent)

            }
            R.id.tvAboutNeighborhoodView ->{
                val spaceRulesIntent = Intent(this, SpaceRulesActivity::class.java)
                mDetailPageResponse.data?.propDet?.about_neighborhood?.let {
                    spaceRulesIntent.putExtra(Iconstants.SPACE_RULES, it)
                }
                spaceRulesIntent.putExtra(Iconstants.SPACE_TITLE, (getString(R.string.about_neighborhood)))
                startActivity(spaceRulesIntent)

            }
            R.id.tvOtherThingsToNoteView->{
                val spaceRulesIntent = Intent(this, SpaceRulesActivity::class.java)
                mDetailPageResponse.data?.propDet?.other_things_to_note?.let {
                    spaceRulesIntent.putExtra(Iconstants.SPACE_RULES, it)
                }
                spaceRulesIntent.putExtra(Iconstants.SPACE_TITLE, (getString(R.string.other_things_to_note)))
                startActivity(spaceRulesIntent)

            }
            R.id.tvReadCancellationPolicy -> {
                val cancellationPolicyIntent = Intent(this, CancellationPolicyActivity::class.java)
                mDetailPageResponse.data?.propDet?.cancellation_name?.let {
                    cancellationPolicyIntent.putExtra(Iconstants.CANCELLATION_POLICY, it)
                }
                mDetailPageResponse.data?.propDet?.cancellation_rules?.let {
                    cancellationPolicyIntent.putExtra(Iconstants.CANCELLATION_RULES, it)
                }
                startActivity(cancellationPolicyIntent)

            }
            R.id.tvViewAvailability -> {
                val calendarIntent = Intent(this, SpaceCalendarActivity::class.java)
                val bundle = Bundle()
                bundle.putBoolean(Iconstants.SHOW_BLOCKED_DATES, true)
                bundle.putBoolean(Iconstants.DISABLE_INTERACTION, true)
                bundle.putString(Iconstants.ID, mPropId)
                calendarIntent.putExtra(Iconstants.BUNDLE, bundle)
                startActivityForResult(calendarIntent, Iconstants.REQUEST_CALENDAR_PAGE)

            }
            R.id.tvMessageHost -> {
                val messageHostIntent = Intent(this, MessageHostActivity::class.java)
                messageHostIntent.putExtra(Iconstants.FIRST_NAME,mDetailPageResponse.data?.ownerDet?.firstname)
                messageHostIntent.putExtra(Iconstants.ID,mDetailPageResponse.data?.propDet?.id)
                startActivity(messageHostIntent)

            }
            R.id.btnRequestMoreInfo -> {
                if(!mIsOwnProperty) {

                    val bookingIntent = Intent(this, BookingPageActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(Iconstants.ID, mPropId)
                    bundle.putString(Iconstants.PROPERTY_NAME, mDetailPageResponse.data?.propDet?.product_name)
                    bundle.putString(Iconstants.PROPERTY_IMAGE, mDetailPageResponse.data?.propDet?.banner_photos)
                    bundle.putString(Iconstants.PROPERTY_OWNER, mDetailPageResponse.data?.ownerDet?.firstname)
                    bundle.putString(Iconstants.PROFILE_IMAGE, mDetailPageResponse.data?.ownerDet?.profile_image)
                    bundle.putString(Iconstants.INSTANCE_BOOK, mDetailPageResponse.data?.propDet?.instant_booking)

                    bundle.putString(Iconstants.LOCATION, mDetailPageResponse.data?.propDet?.address?.city)
                    bookingIntent.putExtra(Iconstants.BUNDLE, bundle)
                    startActivity(bookingIntent)
                } else{
                    baseViewModel.rentersError.postValue(getString(R.string.cant_book_own_prop))
                }

            }
            R.id.imgBtnBack -> {
                onBackPressed()
            }
            R.id.imgBtnFavorite -> {
                detailPageViewModel.callAddRemoveWishList(mPropId).observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    if (it.status == "1") {
                        val resultIntent = Intent()
                        resultIntent.putExtra(Iconstants.IS_FAVORITE,it.data?.is_favorite)
                        setResult(Activity.RESULT_OK,resultIntent)
                        mDetailPageResponse.data?.propDet?.is_favorite = it.data?.is_favorite!!
                        if (it.data?.is_favorite!!) {
                            imgBtnFavorite.setImageResource(R.drawable.ic_favorite_red)
                        }
                        else
                            imgBtnFavorite.setImageResource(R.drawable.ic_favorite)
                        baseViewModel.rentersError.postValue(it.message)
                    }
                })

            }
            R.id.imgBtnShare ->{
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, Iconstants.shareURL + mDetailPageResponse.data?.propDet?.seo_url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    override fun addRemoveWishList(item: SearchSpaceData,position: Int) {
        detailPageViewModel.callAddRemoveWishList(item.id!!).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSearchSpaceList[position].is_favorite = it.data?.is_favorite!!
                setSpaceTypeItemAdapter()

            }
            baseViewModel.rentersError.postValue(it.message)
        })

    }
}

