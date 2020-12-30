package com.rent.renters.app.ui.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_card_payment.*
import kotlinx.android.synthetic.main.header_layout.*

import com.rent.renters.app.ui.base.BaseActivity

import com.rent.renters.app.ui.userHomePage.HomeActivity
import kotlinx.android.synthetic.main.activity_card_payment.btnSave

import java.util.*
import android.widget.DatePicker
import com.rent.renters.mylibrary.util.MonthYearPickerDialog


class CardPaymentActivity : BaseActivity(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener,Util.BottomSuccessClickListener ,DatePickerDialog.OnDateSetListener   {

    private val mCardTypeList = ArrayList<Any>()
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var mSuccessListener : Util.BottomSuccessClickListener

    private var mBookingNo = ""
    private var mPaymentType = ""

    private var mYear: String = ""
    private var mMonth: String = ""
    private var mDay: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)
        mBottomDialogListener = this
        mSuccessListener = this
        initViewModel()
        initView()
    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
            if (intent.hasExtra(Iconstants.TYPE))
                mPaymentType = intent.getStringExtra(Iconstants.TYPE)!!
        }

        if(mPaymentType.contains("credit")){
            tvTitle.text = Iconstants.CREDIT_CARD
        }else {
            tvTitle.text = Iconstants.STRIPE
        }
        mCardTypeList.add("American Express")
        mCardTypeList.add("MasterCard")
        mCardTypeList.add("Visa")

        etCardNumber.addTextChangedListener(CreditCardNumberFormattingTextWatcher())

        etValidDate.setOnClickListener(this)
        etCardType.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        imgBtnBack.setOnClickListener(this)
    }

    private fun initViewModel() {
        paymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        paymentViewModel.initMethod(this)
    }

    @SuppressLint("NewApi")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.etCardType ->
                Util.showBottomDialog(this, mCardTypeList, mBottomDialogListener, Iconstants.CARD_PAYMENT, false)
            R.id.btnSave -> {

                var cardType =  etCardType.text.toString()
                if (etCardType.text.toString().equals("American Express", true)) {
                    cardType = "american_express"
                } else if (etCardType.text.toString().equals("MasterCard", true)) {
                    cardType = "mastercard"
                } else if (etCardType.text.toString().equals("Visa", true)) {
                    cardType = "visa"
                }

                paymentViewModel.callPaymentByCard(mBookingNo, mPaymentType,cardType, etCardNumber.text.toString(), mMonth, mYear, etCVV.text.toString()).observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    val mMessage = (it.message)
                    if (it.status == "1") {

                        Util.showSuccessDialog(this, getString(R.string.success),mMessage, mSuccessListener, Iconstants.BOOKING_SUCCESS)
                    }else{
                        Util.showSuccessDialog(this, getString(R.string.unsuccess),mMessage, mSuccessListener, Iconstants.BOOKING_UNSUCCESS)
                    }
                       /* val homeIntent = Intent(this, HomeActivity::class.java)
                        homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_PAGE)
                        homeIntent.putExtra(Iconstants.MESSAGE,it.message)
                        homeIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                        startActivity(homeIntent)
                        finishAffinity()
*/
                 /*   }
                    else{
                        baseViewModel.rentersError.postValue(it.message)
                        val homeIntent = Intent(this, HomeActivity::class.java)
                        homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_PAGE)
                        homeIntent.putExtra(Iconstants.MESSAGE,it.message)
                        startActivity(homeIntent)
                        finishAffinity()

                        val mMessage = (it.message)
                        Util.showSuccessDialog(this, mMessage,mSuccessListener,Iconstants.PAYMENT_DETAILS)
                    }
*/
                })
            }
            R.id.etValidDate -> {
                val c = Calendar.getInstance()
                var month = c.get(Calendar.MONTH)+1
                val day = c.get(Calendar.DAY_OF_MONTH)
                var year = c.get(Calendar.YEAR).toString().substring(0,2).toInt()
                if(etValidDate.text.toString() !=null && etValidDate.text.toString().length>0
                        && etValidDate.text.toString().contains("/")){
                    etValidDate.text.toString().isNotEmpty().let {
                        month = etValidDate.text.toString().split("/")[0].toInt()
                    }
                    etValidDate.text.toString().isNotEmpty().let {
                        year = etValidDate.text.toString().split("/")[1].toInt()
                    }
                }

               // showCalendarDialog(month, day, year)
                val pd = MonthYearPickerDialog()
                pd.setListener(this,month,year)
                pd.show(supportFragmentManager, "MonthYearPickerDialog")

            }
            R.id.imgBtnBack ->
                finish()
        }

    }



  /*  private fun showCalendarDialog(month: Int, day: Int, year: Int) {

        val dialog = DatePickerDialog(this@CardPaymentActivity, datePickerListener, year, month, day)
       // val dialog = createDialogWithoutDateField(month)
                dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.datePicker.updateDate(year,month,day)

        dialog.show()
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        mYear = selectedYear.toString()
        if ((selectedMonth + 1).toString().length == 1)
            mMonth = "0" + (selectedMonth + 1).toString()
        else
            mMonth = (selectedMonth + 1).toString()
        mDay = selectedDay.toString()

        val mDate = ("$mMonth/").plus((mYear))

        etValidDate.text = mDate

    }*/

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val mDate = p2.toString()+"/"+p1.toString().substring(2,p1.toString().length)
        mMonth = p2.toString()
        mYear = p1.toString()

        etValidDate.text = mDate
    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        etCardType.text = listItem as String
    }

    class CreditCardNumberFormattingTextWatcher : TextWatcher {
        private var current = ""

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString() != current) {
                val userInput = s.toString().replace("[^\\d]".toRegex(), "")
                if (userInput.length <= 16) {
                    val sb = StringBuilder()
                    for (i in userInput.indices) {
                        if (i % 4 == 0 && i > 0) {
                            sb.append(" ")
                        }
                        sb.append(userInput[i])
                    }
                    current = sb.toString()

                    s.filters = arrayOfNulls<InputFilter>(0)
                }
                s.replace(0, s.length, current, 0, current.length)
            }
        }
    }
    override fun onSuccessClick(isFrom: String) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        homeIntent.putExtra(Iconstants.IS_FROM,isFrom)
        homeIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
        startActivity(homeIntent)
        Util.dismissSuccessDialog()
        finishAffinity()
    }


}
