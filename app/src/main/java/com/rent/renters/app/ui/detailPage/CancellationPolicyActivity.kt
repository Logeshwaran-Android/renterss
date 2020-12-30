package com.rent.renters.app.ui.detailPage


import android.os.Bundle
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_cancellation_policy.*
import kotlinx.android.synthetic.main.header_layout.*

class CancellationPolicyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancellation_policy)
        customizeToolbar()
        initView()
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.CANCELLATION_POLICY))
                tvCancellationPolicyVal.text = intent.getStringExtra(Iconstants.CANCELLATION_POLICY)
            if(intent.hasExtra(Iconstants.CANCELLATION_RULES))
                tvCancellationPolicyDesc.text = intent.getStringExtra(Iconstants.CANCELLATION_RULES)
        }
        imgBtnBack.setOnClickListener { finish() }
    }

    private fun customizeToolbar() {
       imgBtnBack.setImageResource(R.drawable.ic_close)
    }
}
