package com.rent.renters.app.ui.myBookings


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R
import com.rent.renters.app.data.model.MyTripsListData
import com.rent.renters.app.data.model.MyTripsResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.base.PaginationScrollListener
import com.rent.renters.app.ui.bookingSteps.ReviewAndPayActivity
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.app.ui.inbox.DefaultMessageActivity
import com.rent.renters.app.ui.reviews.AddReviewActivity
import com.rent.renters.app.ui.reviews.ViewReviewActivity
import com.rent.renters.app.ui.userHomePage.SearchSpaceActivity
import kotlinx.android.synthetic.main.fragment_completed_booking.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.lang.Exception


class DeclinedBookingsFragment : Fragment() ,MyBookingsAdapter.MyBookingsClickListener{
    private lateinit var myBookingsViewModel: MyBookingsViewModel
    private var myBookingAdapter: MyBookingsAdapter? = null

    private var mBookingList : ArrayList<MyTripsListData> = ArrayList()
    private lateinit var mBookingListener : MyBookingsAdapter.MyBookingsClickListener
    private var mPage: Int = 1
    private var mTotalItemCount: Int = 1

    var isLastPage: Boolean = false
    var isLoading: Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBookingListener = this
        initView()
        initViewModel()
    }

    private fun initView() {

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvBookingList.layoutManager = layoutManager

        // setBookingAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            mPage = 1
            callGetBookings()
        }
        rvBookingList?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
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
                callGetBookings()

            }
        })




    }
    private fun initViewModel() {
        myBookingsViewModel = ViewModelProvider(this).get(MyBookingsViewModel::class.java)
        myBookingsViewModel.initMethod(context as Activity)
        callGetBookings()

    }
    private fun callGetBookings(){
        myBookingsViewModel.callGetDeclinedBookingList(mPage).observe(viewLifecycleOwner, Observer<MyTripsResponse> {
            (activity as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            isLoading = false
            try {
                it.data?.pagination_count?.let {
                    mTotalItemCount = it.toInt()
                }
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            if(mPage == 1)
                mBookingList.clear()
            if(it.data?.your_trips!=null) {
                if (it.data?.your_trips!!.size > 0) {

                    mBookingList.addAll(it.data?.your_trips!!)
                    setBookingAdapter()
                } else {
                    swipeRefreshLayout.visibility = View.GONE
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.setOnClickListener{
                        val spaceSizeIntent = Intent(context, SearchSpaceActivity::class.java)
                        startActivity(spaceSizeIntent)
                    }
                }
            } else{
                (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            }

        })


    }

    private fun setBookingAdapter() {
        if (myBookingAdapter == null) {
            myBookingAdapter = MyBookingsAdapter(context!!,mBookingList,mBookingListener)
            rvBookingList?.adapter = myBookingAdapter

        } else {
            myBookingAdapter!!.notifyDataSetChanged()
        }
    }

    override fun bookingClick(item: MyTripsListData, isFrom: String, position:Int) {
        when(isFrom){
            Iconstants.MESSAGE ->{
                val messageIntent = Intent(context, DefaultMessageActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Iconstants.BOOKING_NO,item.booking_no)
                bundle.putString(Iconstants.ID,item.userid)
                bundle.putString(Iconstants.STATUS,item.status)
                bundle.putString(Iconstants.IMAGE,item.user_image)
                bundle.putString(Iconstants.FIRST_NAME,item.firstname)
                messageIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivity(messageIntent)
            }
            Iconstants.INVOICE ->{
                val invoiceIntent = Intent(context,InvoiceActivity::class.java)
                invoiceIntent.putExtra(Iconstants.BOOKING_NO,item.booking_no)
                startActivity(invoiceIntent)
            }
            Iconstants.PAY->{
                val selectPaymentIntent1 = Intent(context, ReviewAndPayActivity::class.java)
                selectPaymentIntent1.putExtra(Iconstants.BOOKING_NO,item.booking_no)
                //  selectPaymentIntent1.putExtra(Iconstants.TOTAL,item.total_booking_fee)
                startActivity(selectPaymentIntent1)
            }
            Iconstants.ADD_REVIEW->{
                val addReviewIntent = Intent(context, AddReviewActivity::class.java)
                addReviewIntent.putExtra(Iconstants.BOOKING_NO,item.booking_no)
                addReviewIntent.putExtra(Iconstants.ID,item.property_id)
                startActivity(addReviewIntent)
            }
            Iconstants.VIEW_REVIEW ->{
                val viewReviewIntent = Intent(context, ViewReviewActivity::class.java)
                viewReviewIntent.putExtra(Iconstants.BOOKING_NO,item.booking_no)
                startActivity(viewReviewIntent)
            }
            Iconstants.PROPERTY ->{
                val detailPageIntent = Intent(context, DetailPageActivity::class.java)
                detailPageIntent.putExtra(Iconstants.ID, item.property_id)
                startActivity(detailPageIntent)
            }
        }

    }



}
