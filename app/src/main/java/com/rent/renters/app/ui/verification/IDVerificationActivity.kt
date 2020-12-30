package com.rent.renters.app.ui.verification

import android.content.Intent

import android.os.Bundle
import android.view.View
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_idverification.*
import kotlinx.android.synthetic.main.activity_set_the_scene.btnNext
import kotlinx.android.synthetic.main.header_layout.*


class IDVerificationActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedIDType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idverification)
        initView()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.trust_and_verification)
        btnNext.setOnClickListener(this)
        rgTypesOfID.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbDriverLicense -> {
                    mSelectedIDType = getString(R.string.driver_license)
                }
                R.id.rbPassport -> {
                    mSelectedIDType = getString(R.string.passport)
                }
                R.id.rbIdentityCard -> {
                    mSelectedIDType = getString(R.string.identity_card)
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnNext ->{
                validateFields()

            }
        }

    }

    private fun validateFields() {
        if(mSelectedIDType.isEmpty())
            baseViewModel.rentersError.postValue(getString(R.string.id_type_err))
        else {
            val imageVerificationIntent = Intent(this, IDImageVerificationActivity::class.java)
            val bundle = Bundle()
            bundle.putString(Iconstants.TYPE, mSelectedIDType)
            bundle.putString(Iconstants.COUNTRY_CODE, ccpCountry.selectedCountryNameCode)
            imageVerificationIntent.putExtra(Iconstants.BUNDLE, bundle)
            startActivityForResult(imageVerificationIntent, Iconstants.ID_VERIFICATION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Iconstants.ID_VERIFICATION_REQUEST_CODE) {
            setResult(resultCode, data)
            finish()
        }
    }
}
