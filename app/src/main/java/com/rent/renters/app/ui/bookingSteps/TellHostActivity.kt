package com.rent.renters.app.ui.bookingSteps


import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.PaymentCalculationData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import kotlinx.android.synthetic.main.activity_tell_host.*
import kotlinx.android.synthetic.main.header_layout.*

class TellHostActivity : BaseActivity(), View.OnClickListener {
    private lateinit var bookingViewModel: BookingViewModel

    private lateinit var mPaymentResponse : PaymentCalculationData
    private var mSessionManager: SessionManager?= null

    private var mBookingNo =""
    private var mLocation =""
    private var mInstantBook =""
    private var mHostName =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tell_host)
        mSessionManager= SessionManager(this)
        initViewModel()
        initView()

    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)!!
                if (bundle.containsKey(Iconstants.BOOKING_NO))
                    mBookingNo = bundle.getString(Iconstants.BOOKING_NO)!!
                if (bundle.containsKey(Iconstants.INSTANCE_BOOK))
                    mInstantBook = bundle.getString(Iconstants.INSTANCE_BOOK)!!
                if(bundle.containsKey(Iconstants.PAYMENT_DETAILS))
                    mPaymentResponse = bundle.getParcelable(Iconstants.PAYMENT_DETAILS)!!
                if (bundle.containsKey(Iconstants.LOCATION))
                    mLocation = bundle.getString(Iconstants.LOCATION)!!
                if (bundle.containsKey(Iconstants.HOST))
                    mHostName = bundle.getString(Iconstants.HOST)!!
                if (bundle.containsKey(Iconstants.PROFILE_IMAGE))
                    loadCircleImageWithGlide(ivChatProfile, bundle.getString(Iconstants.PROFILE_IMAGE)!!,R.drawable.ic_default_circle_profile_img)

            }
        }
        if(!mInstantBook.equals("Yes",true)) {
            btnNext.text = getString(R.string.submit)
            btnNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }

        tvHostMessage.text = getString(R.string.tell_host_msg).plus("\n\n").plus(mHostName)

        val mTempText =  mSessionManager!!.getCurrencySymbol().plus(mPaymentResponse.total_booking_fee).plus(" ").plus(getString(R.string.str_for)).plus(" ").plus(mPaymentResponse.days).plus(" ").plus(getString(R.string.days))
        tvPrice.text = spannableBoldString(mTempText,mSessionManager!!.getCurrencySymbol().plus(mPaymentResponse.total_booking_fee) )

     //   tvTitle.text = getString(R.string.tell_host)
        tvSeePricingDetails.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSeePricingDetails -> {
                val paymentBreakdownIntent = Intent(this, PaymentBreakdownActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Iconstants.LOCATION,mLocation)
                bundle.putParcelable(Iconstants.PAYMENT_DETAILS,mPaymentResponse)
                paymentBreakdownIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivity(paymentBreakdownIntent)
            }
            R.id.btnNext -> {
                callPostBookingRequest()
            }
            R.id.imgBtnBack -> finish()
        }

    }

    private fun callPostBookingRequest() {
        bookingViewModel.callPostBookingRequest(mBookingNo,etYourMessage.text.toString()).observe(this, Observer<CommonResponse> {
            baseViewModel.rentersLoader.postValue(false)

            if(it.status == "1"){
                if(mInstantBook.equals("Yes",true)){
                    val reviewAndPayIntent = Intent(this, ReviewAndPayActivity::class.java)
                    reviewAndPayIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                    startActivity(reviewAndPayIntent)
                }else{
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_SUCCESS)
                    startActivity(homeIntent)
                    finishAffinity()

                }

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
}

