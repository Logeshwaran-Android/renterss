package com.rent.renters.app.ui.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.RegistrationResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.app.ui.verification.ConfirmPhoneNumberActivity
import kotlinx.android.synthetic.main.activity_create_password.*
import kotlinx.android.synthetic.main.activity_create_password.etPassword
import kotlinx.android.synthetic.main.activity_create_password.imgBtnBack
import kotlinx.android.synthetic.main.activity_create_password.tvPwShow
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo


class CreatePasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var registrationViewModel: RegistrationViewModel
    private  var mFirstName :String = ""
    private  var mLastName :String = ""
    private var mCheckPolicyStatus : String = "No"
    private  var mEmail :String = ""
    private var mSessionManager: SessionManager? = null
    private  var mPwShowText :String = ""
    private  var mConfirmPwShowText :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_password)
        mSessionManager = SessionManager(this)
        getBundleValues()
        initView()
        initViewModel()
    }

    private fun getBundleValues() {
        if(intent?.getBundleExtra(Iconstants.BUNDLE)!= null){
            val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
            mFirstName = bundle!!.getString(Iconstants.FIRST_NAME,"")
            mLastName = bundle.getString(Iconstants.LAST_NAME,"")
            mEmail = bundle.getString(Iconstants.EMAIL,"")
            mCheckPolicyStatus = bundle.getString(Iconstants.CHECK_POLICY,"")
        }
    }

    private fun initViewModel() {
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationViewModel.initMethod(this)

    }


    private fun initView() {
        mPwShowText = getString(R.string.show)
        mConfirmPwShowText = getString(R.string.show)

        etPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard()
                tvPwShow.text = mPwShowText
                tvPwShow.visibility = View.VISIBLE
            } else{
                if(etPassword.text.toString().isEmpty())
                    tvPwShow.visibility = View.GONE

            }
        }
        etConfirmPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard()
                tvConfirmPwShow.text = mConfirmPwShowText
                tvConfirmPwShow.visibility = View.VISIBLE
            }else{
                if(etConfirmPassword.text.toString().isEmpty())
                    tvConfirmPwShow.visibility = View.GONE

            }
        }

        etConfirmPassword.setOnEditorActionListener { _, actionId, _ ->
            if ( actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT || actionId == IME_ACTION_DONE ) {
                //do what you want on the press of 'done'
                hideKeyboard()
            }
            false
        }

        imgBtnBack.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        tvConfirmPwShow.setOnClickListener(this)
        tvPwShow.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnSignUp -> {
                validateFields()
            }
            R.id.imgBtnBack -> finish()
            R.id.tvConfirmPwShow -> if (tvConfirmPwShow.text.toString().equals(getString(R.string.show),  true)) {
                mConfirmPwShowText = getString(R.string.hide)
                tvConfirmPwShow.text = mConfirmPwShowText
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etConfirmPassword.setSelection(etConfirmPassword.text.toString().length)
            } else {
                mConfirmPwShowText = getString(R.string.show)
                tvConfirmPwShow.text = mConfirmPwShowText
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etConfirmPassword.setSelection(etConfirmPassword.text.toString().length)
            }
            R.id.tvPwShow -> if (tvPwShow.text.toString().equals(getString(R.string.show),  true)) {
                mPwShowText = getString(R.string.hide)
                tvPwShow.text = mPwShowText
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etPassword.setSelection(etPassword.text.toString().length)
            } else {
                mPwShowText = getString(R.string.show)
                tvPwShow.text = mPwShowText
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etPassword.setSelection(etPassword.text.toString().length)
            }

        }
    }

    private fun validateFields() {

            registrationViewModel.callRegistration(mFirstName, mLastName, mEmail, mCheckPolicyStatus, etPassword.text.toString(),etConfirmPassword.text.toString()).observe(this, Observer<RegistrationResponse> {
                baseViewModel.rentersLoader.postValue(false)
                        if (it.status == "1") {
                            mSessionManager?.createLoginSession(it.data.jwt_token,it.commonArr)
                            if(it.commonArr.phone_verified.equals("Yes",true)) {
                                val homeIntent = Intent(this, ConfirmPhoneNumberActivity::class.java)
                                startActivity(homeIntent)
                                finish()
                            } else{
                                val confirmPhoneNumberIntent = Intent(this, ConfirmPhoneNumberActivity::class.java)
                                startActivityForResult(confirmPhoneNumberIntent,Iconstants.CONFIRM_PHONE_REQUEST_CODE)
                            }
                        } else {
                            baseViewModel.rentersError.postValue(it.message)
                        }


                })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Iconstants.CONFIRM_PHONE_REQUEST_CODE){
           /* if(data!!.hasExtra("MESSAGE")) {
                val mMessage = data.getStringExtra("MESSAGE")
                baseViewModel.rentersError.postValue( mMessage!!)
            }*/

            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finishAffinity()
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
