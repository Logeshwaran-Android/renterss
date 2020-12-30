package com.rent.renters.app.ui.bookingSteps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.PaymentCalculationData
import com.rent.renters.app.data.model.PaymentCalculationResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.SpaceCalendarActivity
import kotlinx.android.synthetic.main.activity_booking_page.*
import kotlinx.android.synthetic.main.header_layout.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class BookingPageActivity : BaseActivity(), View.OnClickListener {

    private lateinit var bookingViewModel: BookingViewModel
    private var mSessionManager: SessionManager? = null

    private  var mPaymentResponse: PaymentCalculationData = PaymentCalculationData()
    val headerDateFormatter = DateTimeFormatter.ofPattern("d MMM")
    val apiFormatter = DateTimeFormatter.ofPattern(Iconstants.API_DATE_TIME_FORMAT)

    private var mPropId = ""
    private var mStartDate = ""
    private var mEndDate = ""
    private var mLocation = ""
    private var mHostName = ""
    private var mHostImage = ""
    private var mInstantBooking = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_page)
        mSessionManager = SessionManager(this)
        initViewModel()
        initView()

    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)

    }

    private fun setPaymentData(data: PaymentCalculationData?) {

        mPaymentResponse = data!!

        tvServiceFee.visibility = View.VISIBLE
        tvTotalPriceDesc.visibility = View.VISIBLE
        viewPrice.visibility = View.VISIBLE
        viewTotalPrice.visibility = View.VISIBLE

        if (data.days == "1") {
            tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(data.base_price).plus(" x ").plus(data.days).plus(" ").plus(getString(R.string.day))
        } else {
            tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(data.base_price).plus(" x ").plus(data.days).plus(" ").plus(getString(R.string.days))
        }

        tvPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(data.booking_fee)

        tvServiceFeeVal.text = mSessionManager!!.getCurrencySymbol().plus(data.service_fee)
        tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(data.total_booking_fee)

        val mTotalTxt = getString(R.string.total).plus(" (").plus(mSessionManager!!.getCurrencyCode()).plus(")")
        tvTotalPrice.text = spannableColorString(mTotalTxt, mSessionManager!!.getCurrencyCode())

        if(!data.week_disc_percentage.isNullOrEmpty()){
            tvDiscountPrice.visibility = View.VISIBLE
            tvDiscountPriceVal.visibility = View.VISIBLE
            tvDiscountPrice.text = data.week_disc_percentage.plus("% ").plus(getString(R.string.week_price_disc))
            tvDiscountPriceVal.text =  ("-").plus(mSessionManager!!.getCurrencySymbol()).plus(data.week_discount)
        } else if(!data.month_disc_percentage.isNullOrEmpty()){
            tvDiscountPrice.visibility = View.VISIBLE
            tvDiscountPriceVal.visibility = View.VISIBLE
            tvDiscountPrice.text = data.month_disc_percentage.plus("% ").plus(getString(R.string.month_price_disc))
            tvDiscountPriceVal.text =  ("-").plus(mSessionManager!!.getCurrencySymbol()).plus(data.month_discount)
        }

    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)!!

                    if (bundle.containsKey(Iconstants.ID))
                        mPropId = bundle.getString(Iconstants.ID, "")
                    if (bundle.containsKey(Iconstants.PROPERTY_NAME))
                        tvPropertyName.text = bundle.getString(Iconstants.PROPERTY_NAME, "")
                    if (bundle.containsKey(Iconstants.PROPERTY_OWNER)) {
                        mHostName = bundle.getString(Iconstants.PROPERTY_OWNER, "")
                        tvHostName.text = getString(R.string.hosted_by).plus(" ").plus(mHostName)
                    }
                if (bundle.containsKey(Iconstants.INSTANCE_BOOK)) {
                    mInstantBooking = bundle.getString(Iconstants.INSTANCE_BOOK, "")
                    if(!mInstantBooking.equals("Yes",true))
                        btnSave.text = getString(R.string.request_booking)
                }
                    if (bundle.containsKey(Iconstants.PROPERTY_IMAGE))
                        loadImageWithGlide(ivProperty, bundle.getString(Iconstants.PROPERTY_IMAGE, ""), R.drawable.ic_empty_space)
                    if (bundle.containsKey(Iconstants.LOCATION))
                        mLocation = bundle.getString(Iconstants.LOCATION, "")
                if (bundle.containsKey(Iconstants.PROFILE_IMAGE))
                    mHostImage = bundle.getString(Iconstants.PROFILE_IMAGE, "")

            }
        }

        imgBtnBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        rlCheckInDate.setOnClickListener(this)
        rlCheckOutDate.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnSave -> {
                validateFields()

                //overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            R.id.imgBtnBack -> finish()
            R.id.rlCheckInDate -> {
                val dateCalendarIntent = Intent(this, SpaceCalendarActivity::class.java)
                val bundle = Bundle()
                if (mStartDate.isNotEmpty() && mEndDate.isNotEmpty()) {
                    bundle.putString(Iconstants.START_DATE, mStartDate)
                    bundle.putString(Iconstants.END_DATE, mEndDate)
                }
                bundle.putString(Iconstants.ID, mPropId)
                bundle.putBoolean(Iconstants.SHOW_BLOCKED_DATES, true)
                dateCalendarIntent.putExtra(Iconstants.BUNDLE, bundle)
                startActivityForResult(dateCalendarIntent, Iconstants.REQUEST_CALENDAR_PAGE)

            }
            R.id.rlCheckOutDate -> {
                val dateCalendarIntent = Intent(this, SpaceCalendarActivity::class.java)
                val bundle = Bundle()
                if (mStartDate.isNotEmpty() && mEndDate.isNotEmpty()) {
                    bundle.putString(Iconstants.START_DATE, mStartDate)
                    bundle.putString(Iconstants.END_DATE, mEndDate)
                }
                bundle.putString(Iconstants.ID, mPropId)
                bundle.putBoolean(Iconstants.SHOW_BLOCKED_DATES, true)
                dateCalendarIntent.putExtra(Iconstants.BUNDLE, bundle)
                startActivityForResult(dateCalendarIntent, Iconstants.REQUEST_CALENDAR_PAGE)

            }
        }
    }

    private fun validateFields() {
        if (tvCheckInVal.text.toString().isNotEmpty() && tvCheckOutVal.text.toString().isNotEmpty()) {
            val intent = Intent(this, SpaceGuestActivity::class.java)
            val bundle = Bundle()
            bundle.putString(Iconstants.ID,mPropId)
            bundle.putString(Iconstants.START_DATE,mStartDate)
            bundle.putString(Iconstants.END_DATE,mEndDate)
            bundle.putParcelable(Iconstants.PAYMENT_DETAILS, mPaymentResponse)
            bundle.putString(Iconstants.LOCATION, mLocation)
            bundle.putString(Iconstants.HOST, mHostName)
            bundle.putString(Iconstants.PROFILE_IMAGE, mHostImage)
            intent.putExtra(Iconstants.BUNDLE, bundle)
            startActivity(intent)

        }else{
            baseViewModel.rentersError.postValue(getString(R.string.select_date_err))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Iconstants.REQUEST_CALENDAR_PAGE) {
                mStartDate = data?.getStringExtra(Iconstants.START_DATE)!!
                mEndDate = data.getStringExtra(Iconstants.END_DATE)!!

                val mTempStartDate = LocalDate.parse(mStartDate)
                val mTempEndDate = LocalDate.parse(mEndDate)

                tvCheckInVal.text = headerDateFormatter.format(mTempStartDate)
                tvCheckOutVal.text = headerDateFormatter.format(mTempEndDate)

                callGetPriceCalculation()

            }
        }
    }

    private fun callGetPriceCalculation() {
        bookingViewModel.callGetPaymentCalculation(mPropId, mStartDate, mEndDate).observe(this, Observer<PaymentCalculationResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {
                    setPaymentData(it.data)
                    btnSave.isEnabled = true

                }else{
                    baseViewModel.rentersError.postValue(it.message)
                    btnSave.isEnabled = false
                }

            }
        })
    }
}
