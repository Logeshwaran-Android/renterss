package com.rent.renters.app.ui.login


import android.app.Activity
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo.*
import android.view.inputmethod.InputMethodManager

import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.LoginResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.app.ui.registration.ForgotPasswordActivity
import com.rent.renters.app.ui.verification.ConfirmPhoneNumberActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel
    private var mSessionManager: SessionManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.initMethod(this)
    }

    private fun initView() {

        tvForgotPw.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
        tvPwShow.setOnClickListener(this)

        etPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tvPwShow.text = getString(R.string.show)
                tvPwShow.visibility = View.VISIBLE
            }else{
                if(etPassword.text.toString().isEmpty())
                    tvPwShow.visibility = View.GONE
            }
        }

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if ( actionId == IME_ACTION_GO || actionId == IME_ACTION_NEXT || actionId == IME_ACTION_DONE ) {
                //do what you want on the press of 'done'
                hideKeyboard()
            }
            false
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvForgotPw -> {
                val forgotPwIntent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(forgotPwIntent)
            }
            R.id.btnLogin -> {
                validateFields()
            }
            R.id.tvPwShow -> if (tvPwShow.text.toString().equals(getString(R.string.show),  true)) {
                tvPwShow.text = getString(R.string.hide)
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etPassword.setSelection(etPassword.text.toString().length)
            } else {
                tvPwShow.text = getString(R.string.show)
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etPassword.setSelection(etPassword.text.toString().length)
            }
            R.id.imgBtnBack -> finish()
        }
    }

    private fun validateFields() {
            loginViewModel.postLogin(etEmail.text.toString(), etPassword.text.toString()).observe(this, Observer<LoginResponse> {
               baseViewModel.rentersLoader.postValue(false)

                    if (it.status == "1") {
                        if(it.data != null) {
                            mSessionManager?.createLoginSession(it.data.jwt_token, it.commonArr)
                            if (it.commonArr.phone_verified.equals("Yes", true)) {
                                val homeIntent = Intent(this, HomeActivity::class.java)
                                startActivity(homeIntent)
                                finishAffinity()
                            } else {
                                val confirmPhoneNumberIntent = Intent(this, ConfirmPhoneNumberActivity::class.java)
                                startActivityForResult(confirmPhoneNumberIntent, Iconstants.CONFIRM_PHONE_REQUEST_CODE)
                            }
                        }
                    } else {
                        baseViewModel.rentersError.postValue(it.message)
                    }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Iconstants.CONFIRM_PHONE_REQUEST_CODE){
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

