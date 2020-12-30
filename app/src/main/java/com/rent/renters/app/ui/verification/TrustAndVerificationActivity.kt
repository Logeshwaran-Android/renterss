package com.rent.renters.app.ui.verification

import android.app.Activity
import android.content.Intent
import android.graphics.Color

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.VerificationDetailsData
import com.rent.renters.app.data.model.VerificationStatusResponse
import com.rent.renters.app.data.model.VerifiedEmailResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_trust_and_verification.*
import kotlinx.android.synthetic.main.header_layout.*

class TrustAndVerificationActivity : BaseActivity(), View.OnClickListener {


    private lateinit var verificationViewModel: VerificationViewModel
    private var mPhoneNumber = ""
    private var mEmailAddress = ""
    private lateinit var mVerificationDetails: VerificationDetailsData
    private lateinit var mSessionManager: SessionManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trust_and_verification)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        verificationViewModel.initMethod(this)
        callGetTrustAndVerification()

    }


    private fun callGetTrustAndVerification() {
        verificationViewModel.getVerificationStatus().observe(this, Observer<VerificationStatusResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {
                    mVerificationDetails = it.data.verification
                    setVerificationData(mVerificationDetails)
                } else {
                    baseViewModel.rentersError.postValue((it.message))
                }
            }
        })
    }

    private fun setVerificationData(mVerificationDetails: VerificationDetailsData) {

        mPhoneNumber = mVerificationDetails.phone_number
        mEmailAddress = mVerificationDetails.email
        tvProvideId.text = mVerificationDetails.description
        btnProvideID.isEnabled = (mVerificationDetails.is_verified.equals("no", true) || mVerificationDetails.is_verified.equals("declined", true))

        if (mVerificationDetails.google.equals("yes", true)) {
            btnConnectGoogle.text = getString(R.string.disconnect)
        } else {
            btnConnectGoogle.text = getString(R.string.connect)
        }

        if (mVerificationDetails.phone_verified.equals("no", true) && mVerificationDetails.email_verified.equals("no", true)) {
            cvVerifiedInfo.visibility = View.GONE
            cvNotVerifiedInfo.visibility = View.VISIBLE
        } else if ((mVerificationDetails.phone_verified.equals("yes", true) && mVerificationDetails.email_verified.equals("yes", true))) {
            cvVerifiedInfo.visibility = View.VISIBLE
            cvNotVerifiedInfo.visibility = View.GONE
        }

        if (!mVerificationDetails.is_verified.equals("no", true)) {
            if (mVerificationDetails.is_verified.equals("yes", true))
                btnProvideID.text = getString(R.string.verified)
            else if (mVerificationDetails.is_verified.equals("pending", true))
                btnProvideID.text = getString(R.string.in_progress)
            else if (mVerificationDetails.is_verified.equals("declined", true))
                btnProvideID.text = getString(R.string.provide_id)
            //btnProvideID.text = (mVerificationDetails.is_verified)
            if (mVerificationDetails.front_id_verify_image.isNotEmpty())
                loadImageWithGlide(ivFrontImage, mVerificationDetails.front_id_verify_image, R.drawable.ic_card_front)
            if (mVerificationDetails.back_id_verify_image.isNotEmpty())
                loadImageWithGlide(ivBackImage, mVerificationDetails.back_id_verify_image, R.drawable.ic_card_back)


        }

        if (mVerificationDetails.email_verified.equals("Yes", true)) {
            tvEmailVerified.visibility = View.VISIBLE
            tvEmailVerifiedInfo.visibility = View.VISIBLE
            tvEmailNotVerified.visibility = View.GONE
            tvEmailNotVerifiedInfo.visibility = View.GONE
            val mEmailVerified = getString(R.string.verified_email_msg).plus(" ").plus(mEmailAddress).plus(". ").plus(getString(R.string.verified_email_use))
            tvEmailVerifiedInfo.text = spannableBoldString(mEmailVerified, mEmailAddress)
        } else {
            tvEmailVerified.visibility = View.GONE
            tvEmailVerifiedInfo.visibility = View.GONE
            tvEmailNotVerified.visibility = View.VISIBLE
            tvEmailNotVerifiedInfo.visibility = View.VISIBLE

            val mEmailNotVerified = getString(R.string.not_verified_email_msg).plus(" ").plus(mEmailAddress).plus(". ").plus(getString(R.string.email_verification_click))
            val spanString = SpannableString(mEmailNotVerified)
            val clickableEmailNotVerified = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    verificationViewModel.callVerifyEmail().observe(this@TrustAndVerificationActivity, Observer<VerifiedEmailResponse> {
                        baseViewModel.rentersLoader.postValue(false)
                        baseViewModel.rentersError.postValue(it.message)


                    })
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@TrustAndVerificationActivity, R.color.text_app_color)
                    ds.isUnderlineText = true
                }
            }
            spanString.setSpan(clickableEmailNotVerified, spanString.indexOf(getString(R.string.email_verification_click)),
                    spanString.indexOf(getString(R.string.email_verification_click)) + getString(R.string.email_verification_click).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvEmailNotVerifiedInfo.text = spanString
            tvEmailNotVerifiedInfo.movementMethod = LinkMovementMethod.getInstance()
            tvEmailNotVerifiedInfo.highlightColor = Color.TRANSPARENT
        }

        if (mVerificationDetails.phone_verified.equals("Yes", true)) {
            val mPhoneVerified = getString(R.string.verified_phone_number_msg).plus(" ").plus(mPhoneNumber).plus(". ").plus(getString(R.string.verified_phone_number_use))
            tvPhoneVerifiedInfo.text = mPhoneVerified

        } else {
            tvPhoneVerified.visibility = View.GONE
            tvPhoneVerifiedInfo.visibility = View.GONE
        }

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnConnectGoogle -> {
                if (mSessionManager.getGoogleConnectId().isNotEmpty()) {
                    if (btnConnectGoogle.text.toString().equals(getString(R.string.connect), true)) {

                        verificationViewModel.callGoogleConnect(mSessionManager.getGoogleConnectId()).observe(this, Observer<CommonResponse> {
                            baseViewModel.rentersLoader.postValue(false)
                            if(it.status == "1") {
                                btnConnectGoogle.text = getString(R.string.disconnect)
                            }
                            baseViewModel.rentersError.postValue((it.message))

                        })
                    } else if (btnConnectGoogle.text.toString().equals(getString(R.string.disconnect), true)) {
                        verificationViewModel.callGoogleDisConnect(mSessionManager.getGoogleConnectId()).observe(this, Observer<CommonResponse> {
                            baseViewModel.rentersLoader.postValue(false)
                            if(it.status == "1") {
                                btnConnectGoogle.text = getString(R.string.connect)
                            }
                            baseViewModel.rentersError.postValue((it.message))

                        })
                    }
                } else{
                    callGoogleLogin()
                }
            }


        }

    }

    private fun initView() {

        btnConnectGoogle.setOnClickListener(this)
        imgBtnBack.setOnClickListener { finish() }
        tvTitle.text = getString(R.string.trust_and_verification)
        btnProvideID.setOnClickListener {
            val trustAndVerificationIntent = Intent(this, IDVerificationActivity::class.java)
            startActivityForResult(trustAndVerificationIntent, Iconstants.ID_VERIFICATION_REQUEST_CODE)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (btnConnectGoogle.text.toString().equals(getString(R.string.connect), true)) {

                verificationViewModel.callGoogleConnect(account?.id.toString()).observe(this, Observer<CommonResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    if(it.status == "1") {
                        mSessionManager.setGoogleConnectID(account?.id.toString())
                        btnConnectGoogle.text = getString(R.string.disconnect)
                    } else{
                        mSessionManager.setGoogleConnectID("")
                    }
                    baseViewModel.rentersError.postValue((it.message))

                })
            } else if (btnConnectGoogle.text.toString().equals(getString(R.string.disconnect), true)) {
                verificationViewModel.callGoogleDisConnect(mSessionManager.getGoogleConnectId()).observe(this, Observer<CommonResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    if(it.status == "1") {
                        mSessionManager.setGoogleConnectID("")
                        btnConnectGoogle.text = getString(R.string.connect)
                    }
                    baseViewModel.rentersError.postValue((it.message))

                })
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Iconstants.ID_VERIFICATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    callGetTrustAndVerification()

                }

            }
        }
        if (requestCode == Iconstants.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

}
