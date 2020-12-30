package com.rent.renters.app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.signUpSignInPage.SignUpSignInActivity
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.header_layout.*


class AccountSettingsActivity : BaseActivity(),BottomDialogButtonInterface {

    private lateinit var mBottomDialogClickListener: BottomDialogButtonInterface
    lateinit var mSessionManager : SessionManager

    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        mSessionManager = SessionManager(this)
        mBottomDialogClickListener = this
        customizeToolbar()
        initViewModel()
    }
    private fun customizeToolbar() {
        imgBtnBack.setOnClickListener{ finish()}
        btnDeleteAccount.setOnClickListener{
            Util.showBottomSheetDialogWithButtons(this, getString(R.string.delete_my_account),getString(R.string.delete_account_msg),mBottomDialogClickListener,true)

        }

        tvTitle.text = getString(R.string.account_settings)
    }
    private fun initViewModel() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.initMethod(this)

    }
    override fun onBottomCookieItemClick() {
        settingsViewModel.callDeActivateAccount().observe(this, Observer<CommonResponse> {
            if (it != null) {
                if (it.status == "1") {

                    baseViewModel.rentersError.postValue(it.message)
                    callLogout()
                }else{
                    baseViewModel.rentersError.postValue(it.message)
                }
            }
        })
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
