package com.rent.renters.app.ui.transactionHistory

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.MyTripsListData
import com.rent.renters.app.data.model.ReservationFilterData
import com.rent.renters.app.data.model.TransactionItem
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util

import kotlinx.android.synthetic.main.activity_transaction_list.*
import kotlinx.android.synthetic.main.activity_transaction_list.imgBtnFilter
import kotlinx.android.synthetic.main.activity_transaction_list.llDate
import kotlinx.android.synthetic.main.activity_transaction_list.noDataLayout
import kotlinx.android.synthetic.main.activity_transaction_list.rlFilter
import kotlinx.android.synthetic.main.activity_transaction_list.tvEndDate
import kotlinx.android.synthetic.main.activity_transaction_list.tvSearch
import kotlinx.android.synthetic.main.activity_transaction_list.tvStartDate
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TransactionListActivity : BaseActivity() ,TransactionHistoryAdapter.MyTransactionClickListener , CustomRecyclerViewAdapter.CustomItemClickListener,View.OnClickListener{

    private val mFilterOptions :ArrayList<Any> = ArrayList()
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var mSessionManager :SessionManager
    private var mTransactionList : ArrayList<TransactionItem> = ArrayList()
    private var mTransactionAdapter: TransactionHistoryAdapter? = null
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    private lateinit var mTransactionListener : TransactionHistoryAdapter.MyTransactionClickListener
    private var mType = ""
    private var mSearchType =""
    private var mFromDate =""
    private var mToDate =""
    var timer = Timer()


    private var mStartYear : String = ""
    private var mStartMonth : String = ""
    private var mStartDay : String = ""
    private var mEndYear : String = ""
    private var mEndMonth : String = ""
    private var mEndDay : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)
        mSessionManager = SessionManager(this)
        mBottomDialogListener = this
        mTransactionListener = this
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        transactionViewModel.initMethod(this)
        callGetTransactionList()

    }

    private fun callGetTransactionList() {
        transactionViewModel.callGetTransactionList(mType).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mTransactionList.clear()
                if(mType.equals(Iconstants.TRANSACTIONS,true)){
                    clRequestEarnings.visibility = View.VISIBLE
                    llBottomLayout.visibility = View.VISIBLE
                    it.data.total_earning?.let {
                        tvEarningsVal.text = mSessionManager.getCurrencySymbol().plus(it)
                    }
                }else if(mType.equals(Iconstants.COMPLETED_TRANSACTIONS,true) ||mType.equals(Iconstants.REQUESTED_TRANSACTIONS,true) ){
                    it.data.total_earning?.let {
                        tvTotalEarningsVal.text = mSessionManager.getCurrencySymbol().plus(it)
                    }
                    it.data.current_earning?.let {
                        tvMonthEarningVal.text = mSessionManager.getCurrencySymbol().plus(it)
                    }
                }
                if(it.data.transactions!= null) {
                    mTransactionList.addAll(it.data.transactions!!)
                    if(mTransactionList.size == 0) {
                        noDataLayout.visibility = View.VISIBLE
                        btnStartExploring.visibility = View.GONE
                        rlFilter.visibility = View.GONE
                        llBottomLayout.visibility = View.GONE
                    }else {

                        setTransactionAdapter()
                    }
                }else{
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.visibility = View.GONE
                    rlFilter.visibility = View.GONE
                    llBottomLayout.visibility = View.GONE
                }


            }
        })
    }

    fun initView(){
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_booking_no),"bno",true))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_property_name),"property",false))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_booking_date),"bookedDate",false))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_checkin),"checkin",false))
        mSearchType = "bno"
        if(intent != null){
            if(intent.hasExtra(Iconstants.TYPE))
                mType = intent.getStringExtra(Iconstants.TYPE)!!
        }

        when {
            mType.equals(Iconstants.COMPLETED_TRANSACTIONS,true) ->{
                tvTitle.text = getString(R.string.completed_transaction)
                clEarnings.visibility = View.VISIBLE
                val date = Calendar.getInstance().time
                tvMonthEarnings.text = SimpleDateFormat("MMMM").format(date).plus(" ").plus(getString(R.string.earnings))
            }
            mType.equals(Iconstants.REQUESTED_TRANSACTIONS,true) -> {
                tvTitle.text = getString(R.string.pending_transactions)
                clEarnings.visibility = View.VISIBLE
                val date = Calendar.getInstance().time
                tvMonthEarnings.text = SimpleDateFormat("MMMM").format(date).plus(" ").plus(getString(R.string.earnings))
            }
            else ->{
            tvTitle.text = getString(R.string.transactions) }

        }


        imgBtnBack.setOnClickListener{finish()
        }
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvTransactionList.layoutManager = layoutManager

        tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(tvSearch.text.toString())
                    return true
                }
                return false
            }
        })

        imgBtnFilter.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        btnRequestEarning.setOnClickListener(this)
    }

    private fun startSearch(searchText: String) {
        runOnUiThread {
            transactionViewModel.callGetTransactionFilterList(mType,searchText,mFromDate,mToDate,mSearchType).observe(this, Observer {
                baseViewModel.rentersLoader.postValue(false)
                if(it.status == "1"){
                    mTransactionList.clear()
                    it.data?.transactions.let {
                        mTransactionList.addAll(it!!)
                        if (mTransactionList.size > 0) {
                            noDataLayout.visibility = View.GONE
                            setTransactionAdapter()
                        } else {
                            noDataLayout.visibility = View.VISIBLE
                            btnStartExploring.visibility = View.GONE
                        }

                    }

                }
            })
        }
    }

    private fun setTransactionAdapter() {

            if (mTransactionAdapter == null) {
                mTransactionAdapter = TransactionHistoryAdapter(this, mTransactionList, mTransactionListener, mType)
                rvTransactionList.adapter = mTransactionAdapter

            } else {
                mTransactionAdapter!!.notifyDataSetChanged()
            }

    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    override fun transactionClick(item: MyTripsListData, isFrom: String) {

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.imgBtnFilter ->
                Util.showBottomDialog(this,mFilterOptions,mBottomDialogListener, Iconstants.RESERVATION,false)
            R.id.tvStartDate ->{
                val c = Calendar.getInstance()
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val year = c.get(Calendar.YEAR)
                showStartDateCalendarDialog(month, day,year)
            }
            R.id.tvEndDate ->{
                if(mFromDate.isNotEmpty()) {
                    showEndDateCalendarDialog(mStartMonth.toInt(), mStartDay.toInt(), mStartYear.toInt())
                }else{
                    baseViewModel.rentersError.postValue(getString(R.string.start_date_err))
                }
            }
            R.id.btnRequestEarning->{
                transactionViewModel.callRequestEarnings().observe(this, Observer {
                    baseViewModel.rentersLoader.postValue(false)
                    baseViewModel.rentersError.postValue(it.message)
                    callGetTransactionList()
                })
            }
        }

    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        mSearchType = (mFilterOptions[position] as ReservationFilterData).key.toString()
        for(i in 0 until mFilterOptions.size) {
            (mFilterOptions[i] as ReservationFilterData).isSelected = (i == position)
        }
        if(position == 3 || position == 2){
            llDate.visibility = View.VISIBLE
            tvSearch.visibility = View.GONE

        } else{
            llDate.visibility = View.GONE
            tvSearch.visibility = View.VISIBLE

        }
    }
    private fun showStartDateCalendarDialog(month: Int, day: Int,year:Int) {

        val dialog = DatePickerDialog(this, startDatePickerListener, (year), month, day)

        //dialog.datePicker.maxDate = System.currentTimeMillis()

        dialog.show()
    }

    private val startDatePickerListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        mStartYear = selectedYear.toString()
        if ((selectedMonth + 1).toString().length == 1)
            mStartMonth = "0" + (selectedMonth + 1).toString()
        else
            mStartMonth =(selectedMonth + 1).toString()
        mStartDay = selectedDay.toString()

        mFromDate = ("$mStartYear-").plus("$mStartMonth-").plus(mStartDay)

        tvStartDate.text = mFromDate
    }
    private fun showEndDateCalendarDialog(month: Int, day: Int,year:Int) {

        val dialog = DatePickerDialog(this, endDatePickerListener, (year), month, day)

        // dialog.datePicker.maxDate = System.currentTimeMillis()

        dialog.show()
    }

    private val endDatePickerListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        mEndYear = selectedYear.toString()
        if ((selectedMonth + 1).toString().length == 1)
            mEndMonth = "0" + (selectedMonth + 1).toString()
        else
            mEndMonth =(selectedMonth + 1).toString()
        mEndDay = selectedDay.toString()

        mToDate = ("$mEndYear-").plus("$mEndMonth-").plus(mEndDay)
        if(mEndYear.toInt() < mStartYear.toInt() ){
            baseViewModel.rentersError.postValue(getString(R.string.end_date_err))
        }else if( mEndYear.toInt() == mStartYear.toInt() && mEndMonth.toInt() < mStartMonth.toInt()){
            baseViewModel.rentersError.postValue(getString(R.string.end_date_err))
        }else{
            tvEndDate.text = mToDate
            startSearch("")
        }
    }


}
