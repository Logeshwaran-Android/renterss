package com.rent.renters.app.ui.bookingSteps


import android.content.Intent


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.PaymentBookingResponse
import com.rent.renters.app.data.model.PaymentCalculationData
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_space_guest.*
import kotlinx.android.synthetic.main.header_layout.*


class SpaceGuestActivity : BaseActivity(), View.OnClickListener {

    private lateinit var bookingViewModel: BookingViewModel
    private var mPropId = ""
    private var mStartDate = ""
    private var mEndDate = ""
    private var mLocation = ""
    private var mHostName = ""
    private var mHostImage = ""
    private var mInstantBooking = ""
    private lateinit var mPaymentResponse: PaymentCalculationData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_guest)
        initViewModel()
        initView()
    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)

    }
    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)!!

                if (bundle.containsKey(Iconstants.ID))
                    mPropId = bundle.getString(Iconstants.ID, "")
                if (bundle.containsKey(Iconstants.START_DATE))
                    mStartDate = bundle.getString(Iconstants.START_DATE, "")
                if (bundle.containsKey(Iconstants.END_DATE))
                    mEndDate = bundle.getString(Iconstants.END_DATE, "")
                if (bundle.containsKey(Iconstants.ID))
                    mPropId = bundle.getString(Iconstants.ID, "")
                if (bundle.containsKey(Iconstants.PROPERTY_OWNER)) {
                    mHostName = bundle.getString(Iconstants.PROPERTY_OWNER, "")
                }
                if (bundle.containsKey(Iconstants.INSTANCE_BOOK)) {
                    mInstantBooking = bundle.getString(Iconstants.INSTANCE_BOOK, "")
                    if(!mInstantBooking.equals("Yes",true))
                        btnSave.text = getString(R.string.request_booking)
                }
                if (bundle.containsKey(Iconstants.LOCATION))
                    mLocation = bundle.getString(Iconstants.LOCATION, "")
                if (bundle.containsKey(Iconstants.PROFILE_IMAGE))
                    mHostImage = bundle.getString(Iconstants.PROFILE_IMAGE, "")

                if(bundle.containsKey(Iconstants.PAYMENT_DETAILS))
                    mPaymentResponse = bundle.getParcelable(Iconstants.PAYMENT_DETAILS)!!
            }

        }

        imgBtnBack.setImageResource(R.drawable.ic_close)
        ivPlusAdult.setOnClickListener(this)
        ivPlusChild.setOnClickListener(this)
        ivPlusInfant.setOnClickListener(this)

        ivMinusAdult.setOnClickListener(this)
        ivMinusChild.setOnClickListener(this)
        ivMinusInfant.setOnClickListener(this)

        btnSave.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivPlusAdult ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                    display(tvAdultCount.text.toString().toInt()+1,tvAdultCount)
            }
            R.id.ivPlusChild ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                    display(tvChildCount.text.toString().toInt()+1,tvChildCount)
            }
            R.id.ivPlusInfant ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                    display(tvInfantCount.text.toString().toInt()+1,tvInfantCount)
            }
            R.id.ivMinusAdult ->{
                if(tvAdultCount.text.toString().toInt() > 0)
                    display(tvAdultCount.text.toString().toInt()-1,tvAdultCount)

            }
            R.id.ivMinusChild ->{
                if(tvChildCount.text.toString().toInt() > 0)
                    display(tvChildCount.text.toString().toInt()-1,tvChildCount)

            }
            R.id.tvInfantCount ->{
                if(tvInfantCount.text.toString().toInt() > 0)
                    display(tvInfantCount.text.toString().toInt()-1,tvChildCount)

            }
            R.id.btnSave ->{
              callProceedToBooking()
            }
            R.id.imgBtnBack ->
                finish()
        }

    }

    private fun display(number: Int,textView: TextView) {
        textView.text = number.toString()
    }

    private fun callProceedToBooking(){
        bookingViewModel.callProceedToBooking(mPropId, mStartDate, mEndDate,tvAdultCount.text.toString(),tvChildCount.text.toString(),tvInfantCount.text.toString()).observe(this, Observer<PaymentBookingResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {

                    val tellHostIntent = Intent(this, TellHostActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(Iconstants.BOOKING_NO, it.data?.booking_no)
                    bundle.putString(Iconstants.INSTANCE_BOOK, it.data?.bookingDet?.instant_status)
                    bundle.putParcelable(Iconstants.PAYMENT_DETAILS, mPaymentResponse)
                    bundle.putString(Iconstants.LOCATION, mLocation)
                    bundle.putString(Iconstants.HOST, mHostName)
                    bundle.putString(Iconstants.PROFILE_IMAGE, mHostImage)
                    tellHostIntent.putExtra(Iconstants.BUNDLE, bundle)
                    startActivity(tellHostIntent)
                } else {
                    baseViewModel.rentersError.postValue(it.message)
                }

            }
        })
    }
}
