package com.rent.renters.app.ui.bookingSteps


import android.content.Context

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.rent.renters.R
import com.rent.renters.app.data.model.PaymentCalculationData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_payment_breakdown.*
import kotlinx.android.synthetic.main.header_layout.*

class PaymentBreakdownActivity : BaseActivity() {

    private lateinit var mPaymentResponse : PaymentCalculationData
    private var mSessionManager: SessionManager?= null
    private var mLocation =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_breakdown)
        mSessionManager= SessionManager(this)
        initView()
    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)!!
                if(bundle.containsKey(Iconstants.PAYMENT_DETAILS)) {
                    mPaymentResponse = bundle.getParcelable(Iconstants.PAYMENT_DETAILS)!!
                    setPaymentData(mPaymentResponse)

                }
                if (bundle.containsKey(Iconstants.LOCATION)) {
                    mLocation = bundle.getString(Iconstants.LOCATION)!!
                    if(mLocation.isNotEmpty()) {
                        if(mPaymentResponse.days == "1")
                            tvDays.text = mPaymentResponse.days.plus(" ").plus(getString(R.string.day)).plus(" ").plus(getString(R.string.str_in)).plus(" ").plus(mLocation)
                        else
                        tvDays.text = mPaymentResponse.days.plus(" ").plus(getString(R.string.days)).plus(" ").plus(getString(R.string.str_in)).plus(" ").plus(mLocation)
                    }
                    else
                        if(mPaymentResponse.days == "1")
                            tvDays.text = mPaymentResponse.days.plus(" ").plus(getString(R.string.day))
                    else
                        tvDays.text = mPaymentResponse.days.plus(" ").plus(getString(R.string.days))

                }

            }
        }


       // tvTitle.text = getString(R.string.payment_breakdown)
        imgBtnBack.setImageResource(R.drawable.ic_close)
        imgBtnBack.setOnClickListener{finish()}
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    private fun setPaymentData(data: PaymentCalculationData?) {

        mPaymentResponse = data!!

        tvServiceFee.visibility = View.VISIBLE
        viewPrice.visibility = View.VISIBLE
        viewTotalPrice.visibility = View.VISIBLE

        if(data.days == "1"){
            tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(data.base_price).plus(" x ").plus(data.days).plus(" ").plus(getString(R.string.day))
        }else {
            tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(data.base_price).plus(" x ").plus(data.days).plus(" ").plus(getString(R.string.days))
        }
        tvPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(data.booking_fee)

        tvServiceFeeVal.text =mSessionManager!!.getCurrencySymbol().plus(data.service_fee)
        tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(data.total_booking_fee)

        val mTotalTxt = getString(R.string.total).plus(" (").plus(mSessionManager!!.getCurrencyCode()).plus(")")
        tvTotalPrice.text = spannableColorString(mTotalTxt,(mSessionManager!!.getCurrencyCode()))

    }
}
