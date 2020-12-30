package com.rent.renters.app.ui.registration

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.ForgotPasswordResponse
import com.rent.renters.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.imgBtnBack


class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initView()
        initViewModel()
    }

    private fun initView() {
        imgBtnBack.setOnClickListener(this)
        btnSendResetLink.setOnClickListener(this)
    }
    private fun initViewModel() {
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationViewModel.initMethod(this)

    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.imgBtnBack ->{
                finish()
            }
            R.id.btnSendResetLink ->{
                registrationViewModel.callForgotPassword(etEmail.text.toString()).observe(this, Observer<ForgotPasswordResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                      if(it.status=="1"){
                          baseViewModel.rentersError.postValue(it.message)
                          finish()
                      } else
                          baseViewModel.rentersError.postValue(it.message)


                })

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
