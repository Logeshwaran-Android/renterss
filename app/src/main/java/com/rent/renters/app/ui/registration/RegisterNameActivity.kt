package com.rent.renters.app.ui.registration


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager


import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterNameActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_name)
        initView()
    }

    private fun initView() {

        ivNext.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBtnBack -> finish()
            R.id.ivNext -> {
                when {
                    etFirstName.text.toString().trim().isEmpty() -> baseViewModel.rentersError.postValue(getString(R.string.first_name_err))
                    etLastName.text.toString().trim().isEmpty() -> baseViewModel.rentersError.postValue(getString(R.string.last_name_err))
                    else -> {
                        val bundle = Bundle()
                        bundle.putString(Iconstants.FIRST_NAME,etFirstName.text.toString())
                        bundle.putString(Iconstants.LAST_NAME,etLastName.text.toString())
                        val emailIntent = Intent(this, RegisterEmailActivity::class.java)
                        emailIntent.putExtra(Iconstants.BUNDLE,bundle)
                        startActivity(emailIntent)
                    }
                }
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
