package com.rent.renters.app.ui.userHomePage


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.rent.renters.R
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.RangeSeekBar
import kotlinx.android.synthetic.main.activity_space_size.*
import kotlinx.android.synthetic.main.header_layout.*

class SpaceSizeActivity : BaseActivity() {

    private lateinit var rangeSeekBarSpaceSize: RangeSeekBar<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_size)
        initView()
        customizeToolbar()
    }

    private fun initView() {
        rangeSeekBarSpaceSize = findViewById(R.id.rangeSeekBarSpaceSize)
        rangeSeekBarSpaceSize.setTextAboveThumbsColor(ContextCompat.getColor(this,R.color.text_black))
        rangeSeekBarSpaceSize.setRangeValues((applicationContext as RentersApplication).getMinSpaceValue(),(applicationContext as RentersApplication).getMaxSpaceValue())
        getIntentValues()

        btnSave.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra(Iconstants.MIN_SQ_FT,rangeSeekBarSpaceSize.selectedMinValue)
            resultIntent.putExtra(Iconstants.MAX_SQ_FT,rangeSeekBarSpaceSize.selectedMaxValue)
            setResult(Activity.RESULT_OK,resultIntent)
            finish()
        }

        rangeSeekBarSpaceSize.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
            if(minValue > 1 || maxValue < (applicationContext as RentersApplication).getMaxSpaceValue())
                tvRightOption.visibility = View.VISIBLE
            else if(minValue == 1 || maxValue == (applicationContext as RentersApplication).getMaxSpaceValue())
                tvRightOption.visibility = View.GONE
        }

    }


    private fun getIntentValues() {
        if (intent != null) {
            if (intent.hasExtra(Iconstants.MIN_SQ_FT)) {
                rangeSeekBarSpaceSize.selectedMinValue  = intent.getIntExtra(Iconstants.MIN_SQ_FT,1)

            }
            if (intent.hasExtra(Iconstants.MAX_SQ_FT)) {
                rangeSeekBarSpaceSize.selectedMaxValue = intent.getIntExtra(Iconstants.MAX_SQ_FT,(applicationContext as RentersApplication).getMaxSpaceValue())


            }
        }

        if(rangeSeekBarSpaceSize.selectedMinValue == (applicationContext as RentersApplication).getMinSpaceValue() &&  rangeSeekBarSpaceSize.selectedMaxValue==(applicationContext as RentersApplication).getMaxSpaceValue()){
            tvRightOption.visibility = View.GONE
        }else{
            tvRightOption.visibility = View.VISIBLE
        }
    }

    private fun customizeToolbar() {
        imgBtnBack.setImageResource(R.drawable.ic_close)
        tvRightOption.text = getString(R.string.reset)
        tvRightOption.setOnClickListener{
            rangeSeekBarSpaceSize.setRangeValues((applicationContext as RentersApplication).getMinSpaceValue(),(applicationContext as RentersApplication).getMaxSpaceValue())
            rangeSeekBarSpaceSize.selectedMinValue = (applicationContext as RentersApplication).getMinSpaceValue()
            rangeSeekBarSpaceSize.selectedMaxValue = (applicationContext as RentersApplication).getMaxSpaceValue()
            tvRightOption.visibility = View.GONE
        }
        imgBtnBack.setOnClickListener{

            finish()
        }
    }
}