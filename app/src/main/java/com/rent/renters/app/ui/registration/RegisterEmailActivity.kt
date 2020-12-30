package com.rent.renters.app.ui.registration


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.webView.WebViewActivity
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.activity_register_email.imgBtnBack
import kotlinx.android.synthetic.main.activity_register_email.ivNext

class RegisterEmailActivity : BaseActivity(), View.OnClickListener {

    private var mFirstName : String = ""
    private var mLastName : String = ""
    private var mCheckPolicyStatus : String = "No"
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)
        initView()
        getBundleValues()
        initViewModel()

    }

    private fun getBundleValues() {
        if(intent?.getBundleExtra(Iconstants.BUNDLE)!= null){
            val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
            mFirstName = bundle!!.getString(Iconstants.FIRST_NAME,"")
            mLastName = bundle.getString(Iconstants.LAST_NAME,"")
        }
    }
    private fun initViewModel() {
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationViewModel.initMethod(this)

    }


    private fun initView() {

        ivNext.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)

        switchCheckPolicy.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                mCheckPolicyStatus = "Yes"
            else
                mCheckPolicyStatus = "No"
        }
        val mFullString = getString(R.string.email_description)
        val spanString = SpannableString(getString(R.string.email_description))
        val clickableSpanTermsAndCondition = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val webViewIntent = Intent(this@RegisterEmailActivity, WebViewActivity::class.java)
                webViewIntent.putExtra(Iconstants.URL,Iconstants.terms_and_condition_url)
                startActivity(webViewIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@RegisterEmailActivity,R.color.text_black)
                ds.isUnderlineText = true
            }
        }
        val clickableSpanPrivacyPolicy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val webViewIntent = Intent(this@RegisterEmailActivity, WebViewActivity::class.java)
                webViewIntent.putExtra(Iconstants.URL,Iconstants.privacy_policy_url)
                startActivity(webViewIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@RegisterEmailActivity,R.color.text_black)
                ds.isUnderlineText = true
            }
        }
        spanString.setSpan(clickableSpanTermsAndCondition,  mFullString.indexOf(getString(R.string.terms_services)),
                mFullString.indexOf(getString(R.string.terms_services)) + getString(R.string.terms_services).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanString.setSpan(clickableSpanPrivacyPolicy, mFullString.indexOf(getString(R.string.privacy_policy)),
                mFullString.indexOf(getString(R.string.privacy_policy)) + getString(R.string.privacy_policy).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvEmailDesc.text = spanString
        tvEmailDesc.movementMethod = LinkMovementMethod.getInstance()
        tvEmailDesc.highlightColor = Color.TRANSPARENT
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBtnBack->{
                finish()
            }
            R.id.ivNext -> {
                if(etEmail.text.toString().isEmpty())
                    baseViewModel.rentersError.postValue(getString(R.string.email_err))
                else if(!Util.isValidEmail(etEmail.text.toString()))
                    baseViewModel.rentersError.postValue(getString(R.string.valid_email_err))
                else if(mCheckPolicyStatus.equals("No",true))
                    baseViewModel.rentersError.postValue(getString(R.string.accept_policy_err))
                else {

                    registrationViewModel.callVerifyEmailAddress(etEmail.text.toString()).observe(this, Observer<CommonResponse> {
                        baseViewModel.rentersLoader.postValue(false)
                        if(it.status == "1"){
                            val bundle = Bundle()
                            bundle.putString(Iconstants.FIRST_NAME,mFirstName)
                            bundle.putString(Iconstants.LAST_NAME,mLastName)
                            bundle.putString(Iconstants.EMAIL,etEmail.text.toString())
                            bundle.putString(Iconstants.CHECK_POLICY,mCheckPolicyStatus)

                            val passwordIntent = Intent(this, CreatePasswordActivity::class.java)
                            passwordIntent.putExtra(Iconstants.BUNDLE,bundle)
                            startActivity(passwordIntent)
                        } else{
                            baseViewModel.rentersError.postValue(it.message)
                        }
                    })

                }
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
