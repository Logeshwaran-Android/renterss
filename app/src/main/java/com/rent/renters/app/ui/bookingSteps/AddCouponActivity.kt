package com.rent.renters.app.ui.bookingSteps

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.os.Bundle
import com.rent.renters.R
import kotlinx.android.synthetic.main.header_layout.*
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.app.data.model.CouponResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_add_coupon.*


class AddCouponActivity : BaseActivity(), View.OnClickListener,Util.BottomSuccessClickListener {

    private lateinit var bookingViewModel: BookingViewModel
    private var mBookingNo =""
    private var mDiscountFee = ""
    private var mTotalFee = ""
    private lateinit var mSuccessListener : Util.BottomSuccessClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coupon)
        mSuccessListener = this
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)

    }
    private fun initView() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.BOOKING_NO)){
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
            }
        }

        ivNext.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
       // tvTitle.text = getString(R.string.add_coupon)
        imgBtnBack.setImageResource(R.drawable.ic_close)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivNext ->{
                bookingViewModel.callApplyCouponCode(mBookingNo,etCouponCode.text.toString()).observe(this, Observer<CouponResponse> {
                    baseViewModel.rentersLoader.postValue(false)


                    if(it.status == "1") {
                        mDiscountFee = it.data?.discount_fee!!
                        mTotalFee = it.data?.total_fee!!
                        val resultIntent = Intent()
                        resultIntent.putExtra(Iconstants.SPACE_PRICE,mTotalFee)
                        resultIntent.putExtra(Iconstants.DISCOUNT_FEE,mDiscountFee)
                        resultIntent.putExtra(Iconstants.COUPON,etCouponCode.text.toString())
                        setResult(Activity.RESULT_OK,resultIntent)
                        finish()
                        //val mMessage = getString(R.string.coupon_success).plus("\n").plus(getString(R.string.discount_fee)).plus(it.data?.discount_fee)
                       // Util.showSuccessDialog(this, mMessage,mSuccessListener,Iconstants.COUPON)
                    }else{
                        baseViewModel.rentersError.postValue(it.message)
                    }
                })
            }
            R.id.imgBtnBack -> finish()
        }

    }

    override fun onSuccessClick(isFrom: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(Iconstants.SPACE_PRICE,mTotalFee)
        resultIntent.putExtra(Iconstants.DISCOUNT_FEE,mDiscountFee)
        resultIntent.putExtra(Iconstants.COUPON,etCouponCode.text.toString())
        setResult(Activity.RESULT_OK,resultIntent)
        finish()

    }

}
