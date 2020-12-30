package com.rent.renters.app.ui.bookingSteps


import android.app.Activity
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.SpaceCalendarActivity
import kotlinx.android.synthetic.main.activity_message_host.*
import kotlinx.android.synthetic.main.header_layout.imgBtnBack
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MessageHostActivity : BaseActivity(), View.OnClickListener {

    private lateinit var bookingViewModel: BookingViewModel

    private var mFromDate: String = ""
    private var mToDate: String = ""
    private var mName: String = ""
    private var mPropId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_host)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        bookingViewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        bookingViewModel.initMethod(this)

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.FIRST_NAME))
                mName = intent.getStringExtra(Iconstants.FIRST_NAME)!!
            if(intent.hasExtra(Iconstants.ID))
                mPropId = intent.getStringExtra(Iconstants.ID)!!

        }

        imgBtnBack.setImageResource(R.drawable.ic_close)
        btnSendMessage.setOnClickListener(this)

        tvEntireSpace.text = getString(R.string.entire_space).plus(" ").plus(getString(R.string.hosted_by)).plus(" ").plus(mName)
        imgBtnBack.setOnClickListener(this)
        tvEditDate.setOnClickListener(this)
        tvDate.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvDate,R.id.tvEditDate -> {
                val calendarIntent = Intent(this, SpaceCalendarActivity::class.java)
                val bundle = Bundle()
                if(mFromDate.isNotEmpty() && mToDate.isNotEmpty()) {
                    bundle.putString(Iconstants.START_DATE, mFromDate)
                    bundle.putString(Iconstants.END_DATE, mToDate)
                }
                bundle.putString(Iconstants.ID,mPropId)
                bundle.putBoolean(Iconstants.SHOW_BLOCKED_DATES,true)
                calendarIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivityForResult(calendarIntent,Iconstants.REQUEST_CALENDAR_PAGE)
                //overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            R.id.imgBtnBack -> finish()
            R.id.btnSendMessage -> {
                bookingViewModel.callContactHost(mFromDate, mToDate, etYourMessage.text.toString(), mPropId).observe(this, Observer<CommonResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                    if(it.status == "1"){
                        Handler().postDelayed({
                            finish()
                        },1000)
                    }
                })
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == Iconstants.REQUEST_CALENDAR_PAGE) {

                val headerDateFormatter = DateTimeFormatter.ofPattern("d MMM YY")

                mFromDate = data?.getStringExtra(Iconstants.START_DATE)!!
                mToDate = data.getStringExtra(Iconstants.END_DATE)!!

                val mTempStartDate = LocalDate.parse(mFromDate, DateTimeFormatter.ISO_LOCAL_DATE)
                val mTempEndDate = LocalDate.parse(mToDate, DateTimeFormatter.ISO_LOCAL_DATE)

                val mText = headerDateFormatter.format(mTempStartDate).plus(" - ").plus(headerDateFormatter.format(mTempEndDate))
                tvDate.text = mText


            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
