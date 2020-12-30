package com.rent.renters.app.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.rent.renters.R
import kotlinx.android.synthetic.main.header_layout.*
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.signUpSignInPage.SignUpSignInActivity
import kotlinx.android.synthetic.main.activity_security_settings.*


class SecuritySettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var settingsViewModel: SettingsViewModel
    private var isDeepLink: Boolean = false
    private var userid: String = ""
    private lateinit var mSessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_settings)
        mSessionManager = SessionManager(this)
        customizeToolbar()
        initView()
        initViewModel()
        onDeepLinkIntent(intent)
    }

    private fun initViewModel() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.initMethod(this)

    }

    private fun initView() {
        imgBtnBack.setOnClickListener(this)
        btnUpdatePassword.setOnClickListener(this)
        btnOldPwShow.setOnClickListener(this)
        btnNewPwShow.setOnClickListener(this)
        btnConfirmPwShow.setOnClickListener(this)

        if(mSessionManager.getGoogleId().isNotEmpty())
        tvGooglePwChangeNote.text = getString(R.string.google_pw_update_hint)

        etOldPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                btnOldPwShow.text = getString(R.string.show)
                btnOldPwShow.visibility = View.VISIBLE
            }else{
                if(etOldPassword.text.toString().isEmpty()){
                    btnOldPwShow.visibility = View.GONE
                }
            }
        }
        etNewPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                btnNewPwShow.text = getString(R.string.show)
                btnNewPwShow.visibility = View.VISIBLE
            }else{
                if(etNewPassword.text.toString().isEmpty()){
                    btnNewPwShow.visibility = View.GONE
                }
            }
        }

        etConfirmPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                btnConfirmPwShow.text = getString(R.string.show)
                btnConfirmPwShow.visibility = View.VISIBLE
            }else{
                if(etConfirmPassword.text.toString().isEmpty()){
                    btnConfirmPwShow.visibility = View.GONE
                }
            }
        }

    }

    private fun customizeToolbar() {
        tvTitle.text = getString(R.string.security)
    }

    private fun onDeepLinkIntent(intent: Intent) {

        val action = intent.action

        val data = intent.dataString
        if (Intent.ACTION_VIEW == action && data != null) {
            tvOldPassword.visibility = View.GONE
            isDeepLink = true
            userid = data.substring(data.lastIndexOf("/") + 1)

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBtnBack -> finish()
            R.id.btnUpdatePassword ->
                if (isDeepLink) {
                    settingsViewModel.callUpdatePassword(userid, etNewPassword.text.toString(), etConfirmPassword.text.toString()).observe(this, Observer<CommonResponse> {
                        baseViewModel.rentersLoader.postValue(false)
                        baseViewModel.rentersError.postValue(it.message)
                        if(it.status == "1") {
                            clearFields()
                            mSessionManager.deleteLoginSession()
                            val singInIntent = Intent(this, SignUpSignInActivity::class.java)
                            singInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(singInIntent)
                            finishAffinity()
                        }


                    })

                } else {
                    settingsViewModel.callChangePassword(etOldPassword.text.toString(), etNewPassword.text.toString(), etConfirmPassword.text.toString()).observe(this, Observer<CommonResponse> {
                        baseViewModel.rentersLoader.postValue(false)
                        baseViewModel.rentersError.postValue(it.message)

                        if(it.status == "1") {
                            clearFields()
                            callLogout()
                        }

                    })
                }
            R.id.btnOldPwShow -> if (btnOldPwShow.text.toString().equals(getString(R.string.show), true)) {
                btnOldPwShow.text = getString(R.string.hide)
                etOldPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etOldPassword.setSelection(etOldPassword.text.toString().length)
            } else {
                btnOldPwShow.text = getString(R.string.show)
                etOldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etOldPassword.setSelection(etOldPassword.text.toString().length)
            }
            R.id.btnNewPwShow -> if (btnNewPwShow.text.toString().equals(getString(R.string.show), true)) {
                btnNewPwShow.text = getString(R.string.hide)
                etNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etNewPassword.setSelection(etNewPassword.text.toString().length)
            } else {
                btnNewPwShow.text = getString(R.string.show)
                etNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etNewPassword.setSelection(etNewPassword.text.toString().length)
            }
            R.id.btnConfirmPwShow -> if (btnConfirmPwShow.text.toString().equals(getString(R.string.show), true)) {
                btnConfirmPwShow.text = getString(R.string.hide)
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etConfirmPassword.setSelection(etConfirmPassword.text.toString().length)
            } else {
                btnConfirmPwShow.text = getString(R.string.show)
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etConfirmPassword.setSelection(etConfirmPassword.text.toString().length)
            }
        }
    }

    private fun clearFields() {
        etOldPassword.setText("")
        etConfirmPassword.setText("")
        etNewPassword.setText("")
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun callLogout(){
        settingsViewModel.callLogout().observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSessionManager.deleteLoginSession()
                val singInIntent = Intent(this, SignUpSignInActivity::class.java)
                singInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(singInIntent)
                finishAffinity()
            }
        })
    }
}
