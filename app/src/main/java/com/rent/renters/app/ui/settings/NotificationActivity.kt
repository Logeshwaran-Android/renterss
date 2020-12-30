package com.rent.renters.app.ui.settings

import android.content.Context

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.data.model.NotificationResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.header_layout.*


class NotificationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mSessionManager : SessionManager
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.initMethod(this)
        settingsViewModel.callUserNotification().observe(this, Observer<NotificationResponse> {
            baseViewModel.rentersLoader.postValue(false)
                if (it.status == "1") {
                    setNotificationData(it)

                }
        })

    }

    private fun setNotificationData(notificationResponse: NotificationResponse) {
        if (notificationResponse.data.current_user_data.email_notify.equals("Yes", true)) {
            ckEmailNotify.isChecked = true

        }
        if (notificationResponse.data.current_user_data.text_notify.equals("Yes", true)) {

            ckTextNotify.isChecked = true

        }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.notification)
        imgBtnBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        tvEmailVal.text = mSessionManager.getUserDetails().email
        var mMobileCode =mSessionManager.getMobileCode()
        mMobileCode =   mMobileCode.replace("\n","")
        mMobileCode = mMobileCode.replace("+","")
        tvPhoneNumberVal.text =("+").plus(mMobileCode).plus(" ").plus(mSessionManager.getPhoneNumber())

    }

      override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBtnBack -> finish()
            R.id.btnSave ->

                settingsViewModel.callUpdateUserNotification(ckEmailNotify.isChecked,false,  ckTextNotify.isChecked).observe(this, Observer<CommonResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                        if (it.status == "1") {
                            baseViewModel.rentersError.postValue(it.message)
                            finish()
                        } else {
                            baseViewModel.rentersError.postValue(it.message)
                        }


                })
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
