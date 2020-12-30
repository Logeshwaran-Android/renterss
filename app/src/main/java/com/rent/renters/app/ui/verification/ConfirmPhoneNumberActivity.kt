package com.rent.renters.app.ui.verification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.VerifiedPhoneResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_confirm_phone_number.*
import kotlinx.android.synthetic.main.header_layout.*



class ConfirmPhoneNumberActivity : BaseActivity(), View.OnClickListener {
    private lateinit var verificationViewModel: VerificationViewModel
    var mMobileCode =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_phone_number)
        initView()
        initViewModel()
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.PHONE_NUMBER))
                etPhoneNumber.setText(intent.getStringExtra(Iconstants.PHONE_NUMBER))
            if(intent.hasExtra(Iconstants.COUNTRY_CODE))
                ccpCountryCode.setCountryForNameCode((intent.getStringExtra(Iconstants.COUNTRY_CODE))!!)
            if(intent.hasExtra(Iconstants.COUNTRY_CODE))
                ccpCountryCode.setCountryForNameCode((intent.getStringExtra(Iconstants.COUNTRY_CODE))!!)
        }

       // tvTitle.text = getString(R.string.confirm_your_phone_number)
        btnNext.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    private fun initViewModel() {
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        verificationViewModel.initMethod(this)

    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnNext -> {
                verificationViewModel.callVerifyPhoneNumber(ccpCountryCode.selectedCountryNameCode,ccpCountryCode.selectedCountryPhoneCode,etPhoneNumber.text.toString()).observe(this, Observer<VerifiedPhoneResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                    if(it.status == "1") {
                        val otpPageIntent = Intent(this, OtpPageActivity::class.java)
                        otpPageIntent.putExtra(Iconstants.PHONE_NUMBER, etPhoneNumber.text.toString())
                        otpPageIntent.putExtra(Iconstants.OTP_CODE, it.data.verificationcode)
                        otpPageIntent.putExtra(Iconstants.COUNTRY_CODE, ccpCountryCode.selectedCountryNameCode)
                        otpPageIntent.putExtra(Iconstants.MOBILE_CODE, ccpCountryCode.selectedCountryPhoneCode)
                        startActivityForResult(otpPageIntent, Iconstants.OTP_REQUEST_CODE)
                    }

                })
            }
            R.id.imgBtnBack -> finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Iconstants.OTP_REQUEST_CODE){
            setResult(Activity.RESULT_OK)
            finish()
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
