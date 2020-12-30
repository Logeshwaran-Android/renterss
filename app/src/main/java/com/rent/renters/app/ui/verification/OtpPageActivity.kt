package com.rent.renters.app.ui.verification



import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.VerifiedPhoneResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_otp_page.*
import kotlinx.android.synthetic.main.activity_otp_page.btnNext
import kotlinx.android.synthetic.main.header_layout.*

class OtpPageActivity : BaseActivity(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener,BottomDialogButtonInterface{

    private lateinit var verificationViewModel: VerificationViewModel
    private var mPhoneNumber : String ?=""
    private var mOTP : String ?=""
    private var mCountryCode : String ?=""
    private var mMobileCode : String ?=""
    private var mSessionManager: SessionManager? = null
    private  var mBottomListValues : ArrayList<Any> = ArrayList()
    private lateinit var mBottomDialogListener : CustomRecyclerViewAdapter.CustomItemClickListener
    private lateinit var mBottomDialogButtonClickListener : BottomDialogButtonInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_page)
        mBottomDialogListener = this
        mBottomDialogButtonClickListener = this
        mSessionManager = SessionManager(this)
        initView()
        customizeToolbar()
        initViewModel()
    }

    private fun initViewModel() {
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        verificationViewModel.initMethod(this)

    }

    private fun customizeToolbar() {
        tvRightOption.text = getString(R.string.get_help)
        tvRightOption.visibility = VISIBLE
    }

    private fun initView() {
        mBottomListValues.add(getString(R.string.change_number))
        mBottomListValues.add(getString(R.string.send_code_again))

        if(intent!=null){

            if(intent.hasExtra(Iconstants.PHONE_NUMBER))
                mPhoneNumber = intent.getStringExtra(Iconstants.PHONE_NUMBER)
            if(intent.hasExtra(Iconstants.OTP_CODE))
                mOTP = intent.getStringExtra(Iconstants.OTP_CODE)
            if(intent.hasExtra(Iconstants.COUNTRY_CODE))
                mCountryCode = intent.getStringExtra(Iconstants.COUNTRY_CODE)
            if(intent.hasExtra(Iconstants.MOBILE_CODE))
                mMobileCode = intent.getStringExtra(Iconstants.MOBILE_CODE)


        }

        val mOtpTxt = getString(R.string.we_send_code).plus(" ").plus(mPhoneNumber).plus(" ").plus(getString(R.string.enter_code_below))
        tvOtpDesc.text = mOtpTxt
        etOtp.setText(mOTP)
        btnNext.setOnClickListener(this)
        tvRightOption.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvRightOption -> Util.showBottomDialog(this,mBottomListValues, mBottomDialogListener,Iconstants.OTP,false)
            R.id.btnNext -> {
                verificationViewModel.callVerifyPhoneNumberOtp(etOtp.text.toString(),mPhoneNumber!!,mMobileCode!!).observe(this, Observer<VerifiedPhoneResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                        if (it.status == "1") {

                            mSessionManager!!.updateVerifyStatus("Yes")
                            mSessionManager!!.updatePhoneNumberCountryCode(mPhoneNumber!!,mCountryCode!!,mMobileCode!!)
                            setResult(Activity.RESULT_OK)
                            finish()
                          //  Util.showBottomSheetDialogWithButtons(this, getString(R.string.success), it.message, mBottomDialogButtonClickListener, false)

                        } else{
                            mSessionManager!!.updateVerifyStatus("No")
                            baseViewModel.rentersError.postValue(it.message)
                        }
                })

            }
            R.id.imgBtnBack -> finish()
        }

    }



    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAdapterItemClick(listItem: Any, position : Int, isFrom:String) {
        Util.dismissBottomDialog()
       if(position == 0){
           finish()

       } else if(position == 1){
           verificationViewModel.callVerifyPhoneNumber(mCountryCode!!,mMobileCode!!,mPhoneNumber!!).observe(this, Observer<VerifiedPhoneResponse> {
               baseViewModel.rentersLoader.postValue(false)
                   if(it.status == "1") {
                       etOtp.setText(it.data.verificationcode)


                   }
                       baseViewModel.rentersError.postValue(it.message)
           })
       }

    }

    override fun onBottomCookieItemClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}

