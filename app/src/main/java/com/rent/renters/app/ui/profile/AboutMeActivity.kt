package com.rent.renters.app.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_about_me.*
import kotlinx.android.synthetic.main.header_layout.*

class AboutMeActivity : BaseActivity() {

    private val  mMaxWordCount = 150
    private var mAboutMe : String ?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        initView()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.about_me)
        imgBtnBack.setOnClickListener{finish()}
        if(intent!=null){
            if(intent.hasExtra(Iconstants.ABOUT_ME)){
                mAboutMe = intent.getStringExtra(Iconstants.ABOUT_ME)
                etAboutMe.setText( mAboutMe)
                etAboutMe.setSelection(etAboutMe.text.toString().length)
                if(etAboutMe.text.toString().isNotEmpty()) {
                    val input = etAboutMe.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvAboutMeWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }

            }

        }

        btnSave.setOnClickListener{
            mAboutMe = etAboutMe.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra(Iconstants.ABOUT_ME,mAboutMe)
            setResult(Activity.RESULT_OK,resultIntent)
            finish()
        }

        etAboutMe.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etAboutMe.text.toString().trim().replace("\n", " ")
                val wordCount = input.split(" ")
                if(wordCount.size < mMaxWordCount) {
                    etAboutMe.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etAboutMe.text.toString().length+1000)))

                    tvAboutMeWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    etAboutMe.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etAboutMe.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
