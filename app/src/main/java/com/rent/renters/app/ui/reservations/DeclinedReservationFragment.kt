package com.rent.renters.app.ui.reservations


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.MyReservationListData
import com.rent.renters.app.data.model.MyReservationResponse
import com.rent.renters.app.data.model.ReservationFilterData
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.base.PaginationScrollListener
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.app.ui.inbox.DefaultMessageActivity
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.fragment_completed_reservations.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class DeclinedReservationFragment : Fragment() , View.OnClickListener, CustomRecyclerViewAdapter.CustomItemClickListener,MyReservationsAdapter.MyReservationClickListener, Util.BottomApproveClickListener  {


    private lateinit var myReservationViewModel: ReservationViewModel
    private var myReservationsAdapter: MyReservationsAdapter? = null
    private var mReservationList : ArrayList<MyReservationListData> = ArrayList()

    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    private lateinit var mReservationListener: MyReservationsAdapter.MyReservationClickListener
    private lateinit var mBottomApproveListener: Util.BottomApproveClickListener

    private val mFilterOptions :ArrayList<Any> = ArrayList()


    private var mType = "declined"
    private var mFromDate =""
    private var mToDate =""
    private var mSearchType =""

    private var mStartYear : String = ""
    private var mStartMonth : String = ""
    private var mStartDay : String = ""
    private var mEndYear : String = ""
    private var mEndMonth : String = ""
    private var mEndDay : String = ""
    private var mPage: Int = 1
    private var mTotalItemCount: Int = 1

    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this
        mReservationListener = this
        mBottomApproveListener = this
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        myReservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
        myReservationViewModel.initMethod(context as Activity)
        callGetReservationList()

    }

    private fun callGetReservationList(){
        myReservationViewModel.callGetDeclinedReservationList(mPage).observe(viewLifecycleOwner, Observer<MyReservationResponse> {
            (activity as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            isLoading = false
            try {
                it.data?.pagination_count?.let {
                    mTotalItemCount = it.toInt()
                }
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            if(mPage == 1)
                mReservationList.clear()
            if(it.data?.reservations!=null) {
                if (it.data?.reservations!!.size > 0) {
                    mReservationList.addAll(it.data?.reservations!!)
                    setReservationAdapter()
                } else {
                    swipeRefreshLayout.visibility = View.GONE
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.visibility = View.GONE
                }
            } else{
                (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            }

        })
    }
    fun initView(){


        mFilterOptions.add(ReservationFilterData(getString(R.string.by_booking_no),"bno",true))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_property_name),"property",false))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_guest),"guest",false))
        mFilterOptions.add(ReservationFilterData(getString(R.string.by_checkin),"bookedDate",false))
        mSearchType = "bno"
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReservationList.layoutManager = layoutManager
        setReservationAdapter()
        imgBtnFilter.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)


        tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(tvSearch.text.toString())
                    return true
                }
                return false
            }
        })
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            mPage = 1
            callGetReservationList()

        }

        rvReservationList?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                if(mPage == mTotalItemCount){
                    isLastPage = true
                }
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true

                mPage += 1
                callGetReservationList()

            }
        })
    }

    private fun startSearch(searchText: String) {
        (context as Activity).runOnUiThread {
            myReservationViewModel.callGetReservationFilterList(mType,searchText,mFromDate,mToDate,mSearchType).observe(this, Observer {
                if(it.status == "1"){
                    mReservationList.clear()
                    if(it.data?.reservations!=null) {
                        mReservationList.addAll(it.data?.reservations!!)
                        if (mReservationList.size > 0) {
                            setReservationAdapter()
                        } else {
                            swipeRefreshLayout.visibility = View.GONE
                            noDataLayout.visibility = View.VISIBLE
                            btnStartExploring.visibility = View.GONE
                        }
                    }

                }
            })
        }
    }
    private fun setReservationAdapter() {
        if (myReservationsAdapter == null) {
            myReservationsAdapter = MyReservationsAdapter(context!!,mReservationList,mReservationListener)
            rvReservationList.adapter = myReservationsAdapter

        } else {
            myReservationsAdapter!!.notifyDataSetChanged()
        }
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.imgBtnFilter ->
                Util.showBottomDialog(context!!,mFilterOptions,mBottomDialogListener, Iconstants.RESERVATION,false)
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
                    (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.start_date_err))
                }
            }
        }

    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        mSearchType = (mFilterOptions[position] as ReservationFilterData).key.toString()
        for(i in 0 until mFilterOptions.size) {
            (mFilterOptions[i] as ReservationFilterData).isSelected = (i == position)
        }
        if(position == 3){
            llDate.visibility = View.VISIBLE
            tvSearch.visibility = View.GONE

        } else{
            llDate.visibility = View.GONE
            tvSearch.visibility = View.VISIBLE

        }
    }
    private fun showStartDateCalendarDialog(month: Int, day: Int,year:Int) {

        val dialog = DatePickerDialog(context!!, startDatePickerListener, (year), month, day)

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

        val dialog = DatePickerDialog(context!!, endDatePickerListener, (year), month, day)

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
        if(mEndYear.toInt() < mStartYear.toInt() || mEndMonth.toInt() < mStartMonth.toInt()){
            (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.end_date_err))
        }else {
            tvEndDate.text = mToDate
            startSearch("")
        }
    }
    override fun reseravtionClick(item: MyReservationListData, isFrom: String, position: Int) {
        when(isFrom){
            Iconstants.MESSAGE ->{
                val messageIntent = Intent(context, DefaultMessageActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Iconstants.BOOKING_NO,item.booking_no)
                bundle.putString(Iconstants.ID,item.userid)
                bundle.putString(Iconstants.STATUS,item.booking_status)
                bundle.putString(Iconstants.IMAGE,item.user_image)
                bundle.putString(Iconstants.FIRST_NAME,item.firstname)
                messageIntent.putExtra(Iconstants.BUNDLE,bundle)
                startActivity(messageIntent)
            }
            Iconstants.PRE_APPROVE->{
                Util.showAcceptDeclineDialog(context!!,mBottomApproveListener)
            }
            Iconstants.PROPERTY ->{
                val detailPageIntent = Intent(context, DetailPageActivity::class.java)
                detailPageIntent.putExtra(Iconstants.ID, item.propid)
                startActivity(detailPageIntent)
            }

        }

    }

    override fun onApproveClick(message: String,isFrom: String) {

    }

}
