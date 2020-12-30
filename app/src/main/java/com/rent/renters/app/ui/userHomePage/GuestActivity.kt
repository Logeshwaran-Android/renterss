package com.rent.renters.app.ui.userHomePage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_guest.*
import kotlinx.android.synthetic.main.header_layout.*

class GuestActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)
        initView()
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ADULT_COUNT))
                tvAdultCount.text = intent.getIntExtra(Iconstants.ADULT_COUNT,0).toString()
            if(intent.hasExtra(Iconstants.CHILD_COUNT))
                    tvChildCount.text = intent.getIntExtra(Iconstants.CHILD_COUNT,0).toString()
            if(intent.hasExtra(Iconstants.INFANT_COUNT))
                    tvInfantCount.text = intent.getIntExtra(Iconstants.INFANT_COUNT,0).toString()
        }
        imgBtnBack.setImageResource(R.drawable.ic_close)
        ivPlusAdult.setOnClickListener(this)
        ivPlusChild.setOnClickListener(this)
        ivPlusInfant.setOnClickListener(this)

        ivMinusAdult.setOnClickListener(this)
        ivMinusChild.setOnClickListener(this)
        ivMinusInfant.setOnClickListener(this)

        btnSave.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivPlusAdult ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                display(tvAdultCount.text.toString().toInt()+1,tvAdultCount)
            }
            R.id.ivPlusChild ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                display(tvChildCount.text.toString().toInt()+1,tvChildCount)
            }
            R.id.ivPlusInfant ->{
                if(tvAdultCount.text.toString().toInt() < 100)
                display(tvInfantCount.text.toString().toInt()+1,tvInfantCount)
            }
            R.id.ivMinusAdult ->{
                if(tvAdultCount.text.toString().toInt() > 0)
                    display(tvAdultCount.text.toString().toInt()-1,tvAdultCount)

            }
            R.id.ivMinusChild ->{
                if(tvChildCount.text.toString().toInt() > 0)
                    display(tvChildCount.text.toString().toInt()-1,tvChildCount)

            }
            R.id.tvInfantCount ->{
                if(tvInfantCount.text.toString().toInt() > 0)
                    display(tvInfantCount.text.toString().toInt()-1,tvChildCount)

            }
            R.id.btnSave ->{
                val resultIntent = Intent()
                resultIntent.putExtra(Iconstants.ADULT_COUNT,tvAdultCount.text.toString().toInt())
                resultIntent.putExtra(Iconstants.CHILD_COUNT,tvChildCount.text.toString().toInt())
                resultIntent.putExtra(Iconstants.INFANT_COUNT,tvInfantCount.text.toString().toInt())
                setResult(Activity.RESULT_OK,resultIntent)
                finish()
            }
            R.id.imgBtnBack ->
                finish()
        }

    }

    private fun display(number: Int,textView: TextView) {
        textView.text = number.toString()
    }
}
