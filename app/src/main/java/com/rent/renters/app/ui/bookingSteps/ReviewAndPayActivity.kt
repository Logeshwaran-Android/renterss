package com.rent.renters.app.ui.bookingSteps

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.ReviewAndPayResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.payment.SelectPaymentActivity
import kotlinx.android.synthetic.main.activity_review_and_pay.*
import kotlinx.android.synthetic.main.header_layout.*

class ReviewAndPayActivity : BaseActivity(), View.OnClickListener {

    private lateinit var bookingViewModel: BookingViewModel
    private var mSessionManager: SessionManager?= null
    private var mTotalVal = ""

    private var mBookingNo =""
    private var mIsCouponApplied = false
    private lateinit var mReviewAndPayResponse : ReviewAndPayResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_and_pay)
        mSessionManager = SessionManager(this)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)
        bookingViewModel.callGetReviewAndPayDetails(mBookingNo).observe(this, Observer<ReviewAndPayResponse> {
            baseViewModel.rentersLoader.postValue(false)
            mReviewAndPayResponse = it
            setData(it)
        })

    }

    private fun setData(it: ReviewAndPayResponse?) {
        tvPropertyName.text = it?.data?.property_det?.product_name
        tvHostName.text = getString(R.string.hosted_by).plus(" ").plus(it?.data?.owner_det?.firstname)
        loadImageWithGlide(ivProperty,it?.data?.property_det?.banner_photos,R.drawable.ic_empty_space)

        tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(it?.data?.payment_det?.base_price).plus(" x ").plus(it?.data?.payment_det?.days).plus(" ").plus(getString(R.string.days))

        tvPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(it?.data?.payment_det?.booking_fee)

        tvServiceFeeVal.text =mSessionManager!!.getCurrencySymbol().plus(it?.data?.payment_det?.service_fee)
        mTotalVal = it?.data?.price_original_val?.total_booking_fee!!
        tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(it.data.payment_det?.total_booking_fee)

        val mTotalTxt = getString(R.string.total).plus(" (").plus(mSessionManager!!.getCurrencyCode()).plus(")")
        tvTotalPrice.text = spannableColorString(mTotalTxt,mSessionManager!!.getCurrencyCode())

        tvCancellationPolicyVal.text = it?.data?.property_det?.cancellation_policy
        tvCancellationPolicyDesc.text = it?.data?.property_det?.cancellation_rules
    }

    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
        }
        // tvTitle.text = getString(R.string.review_and_pay)

        imgBtnBack.setOnClickListener(this)
        viewAddPay.setOnClickListener(this)
        tvAddCoupon.setOnClickListener(this)
        btnMakePayment.setOnClickListener(this)
        tvRemoveCoupon.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvAddCoupon -> {
                val couponIntent = Intent(this, AddCouponActivity::class.java)
                couponIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                startActivityForResult(couponIntent,Iconstants.ADD_COUPON_REQUEST_CODE)
            }
            R.id.viewAddPay -> {
                val selectPaymentIntent = Intent(this, SelectPaymentActivity::class.java)
                selectPaymentIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                startActivity(selectPaymentIntent)
            }
            R.id.btnMakePayment -> {
                val selectPaymentIntent1 = Intent(this, SelectPaymentActivity::class.java)
                selectPaymentIntent1.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                selectPaymentIntent1.putExtra(Iconstants.TOTAL,mTotalVal)
                startActivity(selectPaymentIntent1)
            }
            R.id.imgBtnBack -> {
                onBackPressed()
            }
            R.id.tvRemoveCoupon ->{
                bookingViewModel.callRemoveCoupon(mBookingNo).observe(this, Observer<CommonResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message!!)
                    if(it.status == "1"){
                        tvAddCoupon.text = getString(R.string.add_coupon)
                        tvRemoveCoupon.visibility = View.GONE
                        tvDiscountFee.visibility = View.GONE
                        tvDiscountFeeVal.visibility = View.GONE
                        setData(mReviewAndPayResponse)
                    }
                })
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Iconstants.ADD_COUPON_REQUEST_CODE){
                mIsCouponApplied = true
                mTotalVal = data?.getStringExtra(Iconstants.SPACE_PRICE)!!
                val mDiscountFee = data.getStringExtra(Iconstants.DISCOUNT_FEE)
                val mCouponCode = data.getStringExtra(Iconstants.COUPON)
                tvAddCoupon.text = getString(R.string.coupon_applied).plus(" (").plus(mCouponCode).plus(")")
                tvRemoveCoupon.visibility = View.VISIBLE
                // tvAddCoupon.isClickable = false
                //tvAddCoupon.isEnabled = false
                tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(mTotalVal)
                tvDiscountFee.visibility = View.VISIBLE
                tvDiscountFeeVal.visibility = View.VISIBLE
                tvDiscountFeeVal.text =  (mSessionManager!!.getCurrencySymbol()).plus(mDiscountFee)
            }
        }
    }

    private fun callRemoveCoupon(){
        bookingViewModel.callRemoveCoupon(mBookingNo).observe(this, Observer<CommonResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1")
                finish()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(mIsCouponApplied){
            callRemoveCoupon()

        } else
            finish()
    }
}
