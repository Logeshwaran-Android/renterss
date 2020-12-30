package com.rent.renters.app.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.PayoutPreferenceResponse
import com.rent.renters.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail_page.imgBtnBack
import kotlinx.android.synthetic.main.activity_payout_preference.*
import kotlinx.android.synthetic.main.header_layout.*

class PayoutPreferenceActivity : BaseActivity(), View.OnClickListener {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payout_preference)
        initView()
        initViewModel()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.payout_preferences)
        btnSave.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }
    private fun initViewModel() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.initMethod(this)
        settingsViewModel.callViewPayoutPreference().observe(this, Observer<PayoutPreferenceResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                setPayoutData(it)


            }
        })

    }

    private fun setPayoutData(response: PayoutPreferenceResponse?) {
        response?.data?.UserDetails?.let { it ->
            it.pay_account_name?.let{
                if(it.isNotEmpty())
                    etAccountName.setText(it)
                etAccountName.setSelection(etAccountName.text.toString().length)
            }
            it.pay_account_number?.let{
                if(it.isNotEmpty())
                    etAccountNumber.setText(it)
            }
            it.pay_bank_name?.let{
                if(it.isNotEmpty())
                    etPayBank.setText(it)
            }
        }

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.imgBtnBack ->{
                finish()
            }
            R.id.btnSave ->{
                settingsViewModel.callAddPayoutPreference(etAccountName.text.toString(),etAccountNumber.text.toString(),etPayBank.text.toString()).observe(this,Observer{
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                    finish()

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
