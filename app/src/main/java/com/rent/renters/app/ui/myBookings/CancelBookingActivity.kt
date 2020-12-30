package com.rent.renters.app.ui.myBookings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.BookingDetailsResponse
import com.rent.renters.app.data.model.CancellationBookingData
import com.rent.renters.app.data.model.CancellationReasonData
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_cancel_booking.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat

class CancelBookingActivity : BaseActivity(),CancellationReasonAdapter.CancellationClickListener, View.OnClickListener {


    private lateinit var bookingViewModel: MyBookingsViewModel

    private var mCancellationReasonList = ArrayList<CancellationReasonData>()

    private var mCancellationReasonAdapter: CancellationReasonAdapter ?= null

    private var mBookingNo =""

    private var mReasonId =""
    private var mReasonDesc =""
    private lateinit var mListener : CancellationReasonAdapter.CancellationClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_booking)
        mListener = this
        initView()
        initViewModel()
    }

    private fun initView() {
        if(intent != null) {
            if (intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
        }
        tvTitle.text = getString(R.string.booking_cancellation)
        imgBtnBack.setOnClickListener{ finish()}

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCancelReason.layoutManager = layoutManager
        btnCancel.setOnClickListener(this)

    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(MyBookingsViewModel::class.java)
        bookingViewModel.initMethod(this)
        callGetCancellationBooking()
       /* bookingViewModel.callGetBookingDetails(mBookingNo).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                setBookingData(it)
            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
        })*/
        callGetCancellationReason()
    }

    private fun callGetCancellationBooking() {
        bookingViewModel.callGetCancellationBooking(mBookingNo).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                setCancellationData(it.data)
            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
        })
    }


    private fun callCancelBooking() {
        bookingViewModel.callCancelBooking(mBookingNo,mReasonId,mReasonDesc,etAccountName.text.toString(),etPayBank.text.toString(),etAccountNumber.text.toString()).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            baseViewModel.rentersError.postValue(it.message)
            if(it.status == "1"){
                finish()
            }
        })
    }
    private fun setCancellationData(it: CancellationBookingData?) {
        tvSpaceName.text = it!!.product_name
        loadImageWithGlide(ivSpace,it.banner_photos,R.drawable.ic_default_circle_profile_img)
        try {
            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(it.check_in!!)
            val checkout = SimpleDateFormat("yyyy-MM-dd").parse(it.check_out!!)
            tvBookedDate.text = SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
        }catch (ex: Exception){
            ex.printStackTrace()
        }

        it.cancellation_message?.let {
            tvCancellation.text = it
        }



    }

    private fun callGetCancellationReason(){
        bookingViewModel.callGetCancellationReson().observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                it.data?.cancList?.let{
                    mCancellationReasonList.clear()
                    mCancellationReasonList.addAll(it)
                    setCancellationReasonAdapter()
                }
            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
        })
    }

    private fun setCancellationReasonAdapter() {
        if(mCancellationReasonAdapter== null){
            mCancellationReasonAdapter = CancellationReasonAdapter(this,mCancellationReasonList,mListener)
            rvCancelReason.adapter = mCancellationReasonAdapter
        }else{
            mCancellationReasonAdapter!!.notifyDataSetChanged()
        }

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

    }

    override fun cancellationClick(item: CancellationReasonData, position: Int) {
        mReasonId = item.to_id!!
        mReasonDesc = item.reason!!

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btnCancel ->{
                callCancelBooking()
            }
        }

    }


}
