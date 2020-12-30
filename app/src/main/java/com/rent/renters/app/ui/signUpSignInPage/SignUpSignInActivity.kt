package com.rent.renters.app.ui.signUpSignInPage


import android.app.Activity
import android.content.Intent
import android.os.Bundle



import android.view.View

import com.rent.renters.R
import com.rent.renters.app.ui.login.LoginActivity
import com.rent.renters.app.ui.registration.RegisterNameActivity
import kotlinx.android.synthetic.main.activity_sign_up_sign_in.*
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.method.LinkMovementMethod
import android.text.Spanned
import android.text.TextPaint

import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.rent.renters.app.data.model.LoginResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.app.ui.verification.ConfirmPhoneNumberActivity
import com.rent.renters.app.ui.webView.WebViewActivity

import kotlinx.android.synthetic.main.activity_sign_up_sign_in.tvLogin


class SignUpSignInActivity : BaseActivity(), View.OnClickListener ,GoogleApiClient.OnConnectionFailedListener{

    private lateinit var googleViewModel: GoogleSignInViewModel
    private var mSessionManager: SessionManager? = null
    private var isDeepLink: Boolean = false
    private var userid: String = ""
    private var code: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_sign_in)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()
        onDeepLinkIntent(intent)

    }

    private fun initViewModel() {
        googleViewModel = ViewModelProvider(this).get(GoogleSignInViewModel::class.java)
        googleViewModel.initMethod(this)
    }


    private fun onDeepLinkIntent(intent: Intent) {

        val action = intent.action

        val data = intent.dataString
        if (Intent.ACTION_VIEW == action && data != null) {
            isDeepLink = true
            val splitValues = data.split("/")
            code = splitValues[splitValues.size-1]
            userid = splitValues[splitValues.size-2]

            googleViewModel.callVerifyEmailId(userid,code).observe(this, Observer {
                baseViewModel.rentersLoader.postValue(false)
                baseViewModel.rentersError.postValue(it.message)

                if(it.status == "1"){
                    val loginIntent = Intent(this, HomeActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
            })
        }
    }
    private fun initView() {
        btnCreateAccount.setOnClickListener(this)
        btnGoogle.setOnClickListener(this)
        tvLogin.setOnClickListener(this)

        val spanString = SpannableString(getString(R.string.agree_terms_privacy_policy))
        val clickableSpanTermsAndCondition = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val webViewIntent = Intent(this@SignUpSignInActivity, WebViewActivity::class.java)
                webViewIntent.putExtra(Iconstants.URL,Iconstants.terms_and_condition_url)
                startActivity(webViewIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@SignUpSignInActivity,R.color.text_black)
                ds.isUnderlineText = true
            }
        }
        val clickableSpanPrivacyPolicy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val webViewIntent = Intent(this@SignUpSignInActivity, WebViewActivity::class.java)
                webViewIntent.putExtra(Iconstants.URL,Iconstants.privacy_policy_url)
                startActivity(webViewIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@SignUpSignInActivity,R.color.text_black)
                ds.isUnderlineText = true
            }
        }
        spanString.setSpan(clickableSpanTermsAndCondition, 55, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanString.setSpan(clickableSpanPrivacyPolicy, 80, spanString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvTermsAndPrivacy.text = spanString
        tvTermsAndPrivacy.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndPrivacy.highlightColor = Color.TRANSPARENT

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvLogin -> {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            }
            R.id.btnCreateAccount -> {
                val registerIntent = Intent(this, RegisterNameActivity::class.java)
                startActivity(registerIntent)
            }
            R.id.btnGoogle ->{
               callGoogleLogin()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Iconstants.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else if(resultCode == Activity.RESULT_OK && requestCode == Iconstants.CONFIRM_PHONE_REQUEST_CODE){

            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finish()
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            mSessionManager!!.setGoogleID(account?.id.toString())
            mSessionManager!!.setGoogleConnectID(account?.id.toString())
            googleViewModel.callGoogleRegistration(account?.email.toString(),account?.givenName.toString(),"",account?.photoUrl.toString()).observe(this, Observer<LoginResponse> {
                baseViewModel.rentersLoader.postValue(false)
                if (it.status == "1") {
                    //Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    mSessionManager?.createLoginSession(it.data.jwt_token,it.commonArr)
                    if(it.commonArr.phone_verified.equals("Yes",true)) {
                        val homeIntent = Intent(this, HomeActivity::class.java)
                        startActivity(homeIntent)
                        finishAffinity()
                    } else{
                        val confirmPhoneNumberIntent = Intent(this, ConfirmPhoneNumberActivity::class.java)
                        startActivityForResult(confirmPhoneNumberIntent,Iconstants.CONFIRM_PHONE_REQUEST_CODE)
                    }

                }else{
                    baseViewModel.rentersError.postValue(it.message)
                }
            })

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
        }

    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("Renters", "onConnectionFailed:$connectionResult")
    }
}

