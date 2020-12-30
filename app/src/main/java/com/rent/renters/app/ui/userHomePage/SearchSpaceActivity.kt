package com.rent.renters.app.ui.userHomePage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.data.model.CustomPlaceAddress
import com.rent.renters.app.data.model.FilterDefaultDataResponse
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.data.model.SearchSpaceResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.base.PaginationScrollListener
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.app.ui.filter.MoreFilterActivity
import com.rent.renters.app.ui.map.MapViewActivity
import com.rent.renters.app.ui.searchLocation.SearchActivity
import kotlinx.android.synthetic.main.activity_space_item_list.*
import kotlinx.android.synthetic.main.activity_space_item_list.imgBtnBack
import kotlinx.android.synthetic.main.activity_space_item_list.rvSpaceTypeItems
import kotlinx.android.synthetic.main.activity_space_item_list.shimmer_view_container
import kotlinx.android.synthetic.main.no_data_layout.*
import org.jetbrains.anko.textColor
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Exception


class SearchSpaceActivity : BaseActivity(), View.OnClickListener, SpaceTypeItemAdapter.SpaceTypeItemClickListener,BaseActivity.locationInterface {

    private var spaceTypeItemAdapter: SpaceTypeItemAdapter? = null
    private lateinit var mSpaceTypeListener: SpaceTypeItemAdapter.SpaceTypeItemClickListener
    private lateinit var homeViewModel: HomeViewModel
    private var mSearchSpaceList : ArrayList<SearchSpaceData> = ArrayList()
    private lateinit var mLocationListener: locationInterface
    private var mListId: String = ""
    private var mDefaultListId: String = ""
    private var mCity: String = ""
    private var mMinSquareFeet: Int = 1
    private var mMaxSquareFeet: Int = 1
    private var mMinPrice: Int = 1
    private var mMaxPrice: Int = 1
    private var mAdultCount: Int = 0
    private var mChildCount: Int = 0
    private var mInfantCount: Int = 0
    private var mGuestCount: Int = 0
    private var mInstantBook: String = ""
    private var mFromDate: String = ""
    private var mToDate: String = ""
    private var mAttribute: String = ""
    private  var mDetailPagePosition : Int ?=null

    private var mPage: Int = 1
    private var mTotalItemCount: Int = 0

    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_item_list)
        mMinPrice = (applicationContext as RentersApplication).getMinPriceValue()
        mMinSquareFeet = (applicationContext as RentersApplication).getMinSpaceValue()
        mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
        mMaxSquareFeet = (applicationContext as RentersApplication).getMaxSpaceValue()
        mLocationListener = this
        initViewModel()
        initView()
    }


    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.initMethod(this)
        homeViewModel.callGetDefaultFilterValues().observe(this, Observer<FilterDefaultDataResponse> {
            if (it.status == "1") {
                ((applicationContext) as RentersApplication).setDefaultFilterData(it.data!!)
                mMinPrice = it.data?.min_price!!
                mMinSquareFeet = it.data?.min_sqft!!
                mMaxPrice = it.data?.max_price!!
                mMaxSquareFeet = it.data?.max_sqft!!
            } else {
                baseViewModel.rentersError.postValue(it.message)
            }
        })

    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.ID)) {
                mListId = intent.getStringExtra(Iconstants.ID)!!
                mDefaultListId = mListId
            }
            if(intent.hasExtra(Iconstants.LOCATION)) {
                mCity = intent.getStringExtra(Iconstants.LOCATION)!!
                tvSearch.text = mCity
                callGetSearchSpaceList()
            }

        }
        if(mCity.isEmpty()){
            getCurrentLocation(mLocationListener)
        }

        if(mListId.isNotEmpty())
            changeButtonColor(btnMoreFilters, getDrawable(R.drawable.rectangle_curve_button)!!,ContextCompat.getColor(this, R.color.white))




        mSpaceTypeListener = this

        btnDates.setOnClickListener(this)
        btnSpaceSize.setOnClickListener(this)
        btnGuest.setOnClickListener(this)
        btnMoreFilters.setOnClickListener(this)
        fabLocation.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
        tvSearch.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvSpaceTypeItems.layoutManager = layoutManager

        setSpaceTypeItemAdapter()

        rvSpaceTypeItems?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                if(mPage == mTotalItemCount){
                    isLastPage = true
                }
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true

                mPage += 1
                getMoreItems()

            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
           /* callResetAll()

            btnSpaceSize.text = getString(R.string.space_size)
            btnDates.text = getString(R.string.dates)
            btnMoreFilters.text = getString(R.string.more_filters)
            changeButtonColor(btnSpaceSize, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
            changeButtonColor(btnDates, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
            changeButtonColor(btnMoreFilters, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))*/
            callGetFilterList()
        }


    }

    private fun callResetAll() {
        mAttribute = ""
        mFromDate = ""
        mToDate = ""
        mListId = mDefaultListId
        mMinPrice = (applicationContext as RentersApplication).getMinPriceValue()
        mMinSquareFeet = (applicationContext as RentersApplication).getMinSpaceValue()
        mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
        mMaxSquareFeet = (applicationContext as RentersApplication).getMaxSpaceValue()

    }

    fun getMoreItems() {
        //after fetching your data assuming you have fetched list in your
        // recyclerview adapter assuming your recyclerview adapter is
        //rvAdapter
        //after getting your data you have to assign false to isLoading
        callGetSearchSpaceList()

       // spaceTypeItemAdapter!!.addData(list)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnDates -> {
                val dateCalendarIntent = Intent(this, SpaceCalendarActivity::class.java)
                val bundle = Bundle()
                if(mFromDate.isNotEmpty() && mToDate.isNotEmpty()) {
                    bundle.putString(Iconstants.START_DATE, mFromDate)
                    bundle.putString(Iconstants.END_DATE, mToDate)
                }
                bundle.putBoolean(Iconstants.SHOW_BLOCKED_DATES, false)
                dateCalendarIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivityForResult(dateCalendarIntent,Iconstants.REQUEST_CALENDAR_PAGE)

        }
            R.id.btnSpaceSize -> {
                val spaceSizeIntent = Intent(this, SpaceSizeActivity::class.java)

                    spaceSizeIntent.putExtra(Iconstants.MIN_SQ_FT, mMinSquareFeet)
                    spaceSizeIntent.putExtra(Iconstants.MAX_SQ_FT, mMaxSquareFeet)

                startActivityForResult(spaceSizeIntent,Iconstants.SPACE_SIZE_REQUEST_CODE)

            }
            R.id.btnMoreFilters -> {
                val moreFilterIntent = Intent(this, MoreFilterActivity::class.java)

                    val bundle = Bundle()
                    bundle.putString(Iconstants.ID,mListId)
                    bundle.putString(Iconstants.SELECTED_ID,mAttribute)
                    bundle.putInt(Iconstants.MIN_PRICE, mMinPrice)
                    bundle.putInt(Iconstants.MAX_PRICE, mMaxPrice)
                    bundle.putString(Iconstants.INSTANCE_BOOK,mInstantBook)

                moreFilterIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivityForResult(moreFilterIntent,Iconstants.MORE_FILTER_REQUEST_CODE)

            }
            R.id.btnGuest -> {
                val guestActivity = Intent(this, GuestActivity::class.java)
                guestActivity.putExtra(Iconstants.ADULT_COUNT, mAdultCount)
                guestActivity.putExtra(Iconstants.CHILD_COUNT, mChildCount)
                guestActivity.putExtra(Iconstants.INFANT_COUNT, mInfantCount)
                startActivityForResult(guestActivity,Iconstants.SPACE_GUEST_REQUEST_CODE)

            }
            R.id.fabLocation -> {
                val mapViewIntent = Intent(this, MapViewActivity::class.java)
                mapViewIntent.putExtra(Iconstants.ID,mListId)
                mapViewIntent.putExtra(Iconstants.LOCATION,mCity)
                startActivity(mapViewIntent)

            }
            R.id.tvSearch -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivityForResult(searchIntent,Iconstants.SEARCH_LOCATION_REQUEST_CODE)

            }
            R.id.imgBtnBack -> finish()
        }
    }

    private fun setSpaceTypeItemAdapter( ) {
        if (spaceTypeItemAdapter == null) {
            spaceTypeItemAdapter = SpaceTypeItemAdapter(this, mSearchSpaceList, mSpaceTypeListener)
            rvSpaceTypeItems.adapter = spaceTypeItemAdapter
        } else {
            spaceTypeItemAdapter!!.notifyDataSetChanged()
        }
    }


    override fun spaceTypeItemClick(item :SearchSpaceData,position: Int) {
        mDetailPagePosition = position
        val detailPageIntent = Intent(this, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID,item.id)
        startActivityForResult(detailPageIntent,Iconstants.DETAIL_PAGE_REQUEST_CODE)
    }

    override fun addRemoveWishList(item: SearchSpaceData,position: Int) {
        homeViewModel.callAddRemoveWishList(item.id!!).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSearchSpaceList[position].is_favorite = it.data?.is_favorite!!
                spaceTypeItemAdapter!!.notifyItemChanged(position)
                baseViewModel.rentersError.postValue(it.message)
            }
        })

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()


    }

    private fun callGetSearchSpaceList(){
        homeViewModel.callGetSearchSpaceList(mCity,mListId,mMinSquareFeet,mMaxSquareFeet,mMinPrice,mMaxPrice,mInstantBook,mFromDate,mToDate,mPage,mAttribute,mGuestCount).observe(this, Observer<SearchSpaceResponse> {
            isLoading = false
            if(it.status == "1") {
                try {
                    mTotalItemCount = it.data?.pagination_count!!.toInt()
                }catch (ex: Exception){
                    ex.printStackTrace()
                }
                if(mPage == 1)
                    mSearchSpaceList.clear()

                mSearchSpaceList.addAll(it.data?.search_results!!)

                if(mSearchSpaceList.size == 0){
                    swipeRefreshLayout.visibility = View.VISIBLE
                    fabLocation.visibility = View.GONE
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.visibility = View.GONE

                }else{
                    setSpaceTypeItemAdapter()
                    swipeRefreshLayout.visibility = View.VISIBLE
                    fabLocation.visibility = View.VISIBLE
                    noDataLayout.visibility = View.GONE

                }


            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
            if(shimmer_view_container.visibility == View.VISIBLE) {
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
            }
        })
    }

    override fun locationUpdate(customAddress: CustomPlaceAddress) {
        mCity = customAddress.city.plus(",").plus(customAddress.state).plus(",").plus(customAddress.country)
        tvSearch.text = mCity
        callGetSearchSpaceList()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Iconstants.REQUEST_CALENDAR_PAGE){

                val headerDateFormatter = DateTimeFormatter.ofPattern("d MMM")

                mFromDate = data?.getStringExtra(Iconstants.START_DATE)!!
                mToDate = data.getStringExtra(Iconstants.END_DATE)!!

                val mTempStartDate = LocalDate.parse(mFromDate)
                    val mTempEndDate = LocalDate.parse(mToDate)

                    val mBtnText = headerDateFormatter.format(mTempStartDate).plus(" - ").plus(headerDateFormatter.format(mTempEndDate))
                    btnDates.text = mBtnText

                changeButtonColor(btnDates, getDrawable(R.drawable.rectangle_curve_button)!!,ContextCompat.getColor(this, R.color.white))
                callGetFilterList()



            } else if(requestCode == Iconstants.SPACE_SIZE_REQUEST_CODE){
                mMinSquareFeet = data?.getIntExtra(Iconstants.MIN_SQ_FT,0)!!
                mMaxSquareFeet = data.getIntExtra(Iconstants.MAX_SQ_FT,0)
                val mBtnText = (mMinSquareFeet.toString()).plus(" - ").plus(mMaxSquareFeet.toString()).plus(getString(R.string.sq_feet))
                btnSpaceSize.text = mBtnText
                changeButtonColor(btnSpaceSize, getDrawable(R.drawable.rectangle_curve_button)!!,ContextCompat.getColor(this, R.color.white))
                callGetFilterList()

            } else if(requestCode == Iconstants.MORE_FILTER_REQUEST_CODE){
                mListId = data?.getStringExtra(Iconstants.ID)!!
                mAttribute = data.getStringExtra(Iconstants.SELECTED_ID)!!
                mMinPrice = data.getIntExtra(Iconstants.MIN_PRICE,1)
                mMaxPrice = data.getIntExtra(Iconstants.MAX_PRICE,(applicationContext as RentersApplication).getMaxPriceValue())
                mInstantBook = data.getStringExtra(Iconstants.INSTANCE_BOOK)!!

                changeButtonColor(btnMoreFilters, getDrawable(R.drawable.rectangle_curve_button)!!,ContextCompat.getColor(this, R.color.white))
                callGetFilterList()
            }else if(requestCode == Iconstants.SEARCH_LOCATION_REQUEST_CODE){
                val address = data?.getParcelableExtra<CustomPlaceAddress>("LOCATION")!!
                mCity = address.address!!
                tvSearch.text = mCity
                callGetFilterList()
            }else if(requestCode == Iconstants.DETAIL_PAGE_REQUEST_CODE){
                val isFavorite = data?.getBooleanExtra(Iconstants.IS_FAVORITE,false)!!
                mDetailPagePosition?.let{
                mSearchSpaceList[mDetailPagePosition!!].is_favorite = isFavorite
                    spaceTypeItemAdapter!!.notifyItemChanged(mDetailPagePosition!!)
                }
            }
            else if(requestCode == Iconstants.SPACE_GUEST_REQUEST_CODE){
                mAdultCount = data?.getIntExtra(Iconstants.ADULT_COUNT,0)!!
                mChildCount = data?.getIntExtra(Iconstants.CHILD_COUNT,0)
                mInfantCount = data?.getIntExtra(Iconstants.INFANT_COUNT,0)
                mGuestCount = mAdultCount + mChildCount
                changeButtonColor(btnGuest, getDrawable(R.drawable.rectangle_curve_button)!!,ContextCompat.getColor(this, R.color.white))
                callGetFilterList()
            }

        } else if(resultCode == Activity.RESULT_CANCELED){
            if(requestCode == Iconstants.REQUEST_CALENDAR_PAGE) {
                mFromDate = ""
                mToDate = ""
                btnDates.text = getString(R.string.dates)
                changeButtonColor(btnDates, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
               // callGetFilterList()
            } else if(requestCode == Iconstants.SPACE_SIZE_REQUEST_CODE){
                mMinSquareFeet = (applicationContext as RentersApplication).getMinSpaceValue()
                mMaxSquareFeet = (applicationContext as RentersApplication).getMaxSpaceValue()
                btnSpaceSize.text = getString(R.string.space_size)
                changeButtonColor(btnSpaceSize, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
               // callGetFilterList()

            } else if(requestCode == Iconstants.MORE_FILTER_REQUEST_CODE){
                mAttribute = ""
                mListId = mDefaultListId
                mMinPrice = (applicationContext as RentersApplication).getMinPriceValue()
                mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
                if(mListId.isEmpty())
                changeButtonColor(btnMoreFilters, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
                //callGetFilterList()
            }else if(requestCode == Iconstants.SPACE_GUEST_REQUEST_CODE){
                mAdultCount = 0
                mInfantCount = 0
                mChildCount = 0
                mGuestCount = 0
                changeButtonColor(btnGuest, getDrawable(R.drawable.rectangle_border_button)!!,ContextCompat.getColor(this, R.color.black))
            }

        }
    }

    private fun callGetFilterList() {
        mPage = 1
        mSearchSpaceList.clear()

        swipeRefreshLayout.visibility = View.GONE
        noDataLayout.visibility = View.GONE
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        callGetSearchSpaceList()

    }

    private fun changeButtonColor(button : TextView, background : Drawable, textColor : Int){
        button.textColor = textColor
        button.background = background
    }


}
