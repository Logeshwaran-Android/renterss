package com.rent.renters.app.ui.settings


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.CurrencyListData
import com.rent.renters.app.data.model.CurrencyResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.signUpSignInPage.SignUpSignInActivity
import com.rent.renters.app.ui.statistics.StatsActivity
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.tvCurrency
import kotlinx.android.synthetic.main.header_layout.*
import java.util.ArrayList

class SettingsActivity : BaseActivity(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener,BottomDialogButtonInterface {


    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    private lateinit var settingsViewModel: SettingsViewModel
    private val mCurrencyCodeList =  ArrayList<Any>()
    private val mCurrencyList =  ArrayList<CurrencyListData>()
    private lateinit var mBottomCookieClickListener : BottomDialogButtonInterface
    lateinit var mSessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mBottomCookieClickListener = this
        mBottomDialogListener = this
        mSessionManager = SessionManager(this)

        initView()
        initViewModel()
    }


    private fun initView() {

        if(mSessionManager.getUserType().equals(Iconstants.HOST,true)){
            tvStats.visibility = View.VISIBLE
            viewStats.visibility = View.VISIBLE

        } else{
            tvStats.visibility = View.GONE
            viewStats.visibility = View.GONE
        }

        tvTitle.text = getString(R.string.settings)

        tvCurrencyCode.text = (mSessionManager.getCurrencyCode())

        imgBtnBack.setOnClickListener(this)
        tvNotification.setOnClickListener(this)
        tvStats.setOnClickListener(this)
        tvAccountSettings.setOnClickListener(this)
        tvSecurity.setOnClickListener(this)
        tvCurrency.setOnClickListener(this)
        tvLogout.setOnClickListener(this)
        tvAbout.setOnClickListener(this)
    }

    private fun initViewModel() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.initMethod(this)

    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvStats -> {
                val statsIntent = Intent(this, StatsActivity::class.java)
                startActivity(statsIntent)

            }
            R.id.tvAccountSettings -> {
                val accountSettingsIntent = Intent(this, AccountSettingsActivity::class.java)
                startActivity(accountSettingsIntent)
            }
            R.id.tvNotification ->{
                val notificationIntent = Intent(this, NotificationActivity::class.java)
                startActivity(notificationIntent)
            }
            R.id.tvSecurity ->{
                val securityIntent = Intent(this, SecuritySettingsActivity::class.java)
                startActivity(securityIntent)
            }
            R.id.imgBtnBack ->finish()
            R.id.tvCurrency ->{
                settingsViewModel.callGetCurrencyList().observe(this, Observer<CurrencyResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    if (it != null) {
                        if(it.status == "1"){
                            if(it.data?.currency!!.size >0){
                                mCurrencyList.clear()
                                mCurrencyCodeList.clear()
                                mCurrencyList.addAll(it.data?.currency!!)
                                for(item in it.data?.currency!!){
                                    mCurrencyCodeList.add((item.currency_type!!))
                                }
                            }
                            Util.showBottomDialog(this,mCurrencyCodeList, mBottomDialogListener,Iconstants.CURRENCY,false)

                        }

                    }
                })
            }
            R.id.tvLogout ->{
                Util.showBottomSheetDialogWithButtons(this,getString(R.string.logout),getString(R.string.logout_msg), mBottomCookieClickListener,true)
            }
            R.id.tvAbout ->{
               /* val webViewIntent = Intent(this@SettingsActivity, WebViewActivity::class.java)
                webViewIntent.putExtra(Iconstants.URL, Iconstants.about_us_url)
                startActivity(webViewIntent)*/
                val aboutIntent = Intent(this, AboutUsActivity::class.java)
                startActivity(aboutIntent)
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
    override fun onAdapterItemClick(listItem: Any, position: Int,isFrom:String) {
        Util.dismissBottomDialog()
        mSessionManager.updateCurrencyCode(mCurrencyList[position].currency_type!!, mCurrencyList[position].currency_symbols!!)
        tvCurrencyCode.text = listItem as String

    }
    override fun onBottomCookieItemClick() {
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
