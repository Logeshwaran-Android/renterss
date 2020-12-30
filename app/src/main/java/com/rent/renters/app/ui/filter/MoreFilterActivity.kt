package com.rent.renters.app.ui.filter


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.data.model.MoreFilterData
import com.rent.renters.app.data.model.MoreFilterList
import com.rent.renters.app.data.model.MoreFilterResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants

import com.rent.renters.mylibrary.util.RangeSeekBar
import kotlinx.android.synthetic.main.activity_more_filter.*
import kotlinx.android.synthetic.main.header_layout.*



import java.util.ArrayList

class MoreFilterActivity : BaseActivity(), View.OnClickListener,MoreFilterAdapter.MoreFilterInterface {

    private lateinit var rangeSeekBarFilter: RangeSeekBar<Int>

    private lateinit var mSessionManager :SessionManager

    private  var mFilterAdapter: MoreFilterListAdapter? = null
    private  var mSpaceFilterAdapter: MoreFilterAdapter? = null
    private lateinit var filterViewModel: FilterViewModel
    private var mFilterList : ArrayList<MoreFilterList> = ArrayList()
    private var mSpaceFilterList : ArrayList<MoreFilterData> = ArrayList()
    private lateinit var mFilterListener: MoreFilterAdapter.MoreFilterInterface
    private var mSpaceId : String = ""
    private var mSelectedAttributeId : String =""
    private var mInstantBook : String =""
    private var mMinPrice : Int = 1
    private var mMaxPrice : Int = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_filter)
        mFilterListener = this
        mMinPrice = (applicationContext as RentersApplication).getMinPriceValue()
        mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
        mSessionManager = SessionManager(this)
        initView()
        customizeToolbar()
        initViewModel()
        getIntentValues()
    }

    private fun getIntentValues() {

        if(intent!=null){
            if(intent.hasExtra(Iconstants.BUNDLE)){
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
                if(bundle!!.containsKey(Iconstants.ID))
                    mSpaceId = bundle.getString(Iconstants.ID)!!
                if(bundle.containsKey(Iconstants.SELECTED_ID))
                    mSelectedAttributeId = bundle.getString(Iconstants.SELECTED_ID)!!
                if(bundle.containsKey(Iconstants.MIN_PRICE))
                    mMinPrice = bundle.getInt(Iconstants.MIN_PRICE)
                if(bundle.containsKey(Iconstants.MAX_PRICE))
                    mMaxPrice = bundle.getInt(Iconstants.MAX_PRICE)
                if(bundle.containsKey(Iconstants.INSTANCE_BOOK))
                    mInstantBook = bundle.getString(Iconstants.INSTANCE_BOOK)!!
            }

        }

        if((applicationContext as RentersApplication).getMoreFilterList().size == 0 && (applicationContext as RentersApplication).getSpaceFilterList().size == 0){
            filterViewModel.callGetMoreFilterItems(mSpaceId).observe(this, Observer<MoreFilterResponse> {
                baseViewModel.rentersLoader.postValue(false)
                if (it != null) {
                    mSpaceFilterList.clear()
                    mFilterList.clear()
                    if (it.status == "1") {
                        if(it.data?.amenities != null) {
                            mFilterList.addAll(it.data?.amenities!!)
                            setRecyclerViewAdapter()
                        }
                        if(it.data?.space_list != null) {
                            mSpaceFilterList.addAll(it.data?.space_list!!)
                            for (i in 0 until mSpaceFilterList.size) {
                                if (mSpaceFilterList[i].id.equals(mSpaceId, true)) {
                                    mSpaceFilterList[i].isSelected = true
                                    break
                                }
                            }
                            setSpaceTypeAdapter()
                        }

                    }

                }
            })

        } else{
            toggleInstantBook.isChecked = mInstantBook.equals("Yes",true)
            rangeSeekBarFilter.selectedMinValue = mMinPrice
            rangeSeekBarFilter.selectedMaxValue = mMaxPrice

            mFilterList = (applicationContext as RentersApplication).getMoreFilterList()
            mSpaceFilterList = (applicationContext as RentersApplication).getSpaceFilterList()
            for(i in 0 until mFilterList.size){
                for(j in 0 until mFilterList[i].values.size){
                    if(mFilterList[i].values[j].isSelected)
                        tvRightOption.visibility = View.VISIBLE
                }
            }

            for(j in 0 until mSpaceFilterList.size){
                if(mSpaceFilterList[j].isSelected)
                    tvRightOption.visibility = View.VISIBLE
            }


            setRecyclerViewAdapter()
            setSpaceTypeAdapter()

        }



    }

    private fun initViewModel() {
        filterViewModel = ViewModelProvider(this).get(FilterViewModel::class.java)
        filterViewModel.initMethod(this)

    }


    private fun resetAll(){
        baseViewModel.rentersLoader.postValue(true)
        tvRightOption.visibility = View.GONE

        toggleInstantBook.isChecked = false
        mInstantBook = ""
        mMinPrice = 1
        mMaxPrice = (applicationContext as RentersApplication).getMaxPriceValue()
        mSelectedAttributeId = ""
        mSpaceId = ""

        rangeSeekBarFilter.setRangeValues(1, (applicationContext as RentersApplication).getMaxPriceValue())
        rangeSeekBarFilter.selectedMinValue = mMinPrice
        rangeSeekBarFilter.selectedMaxValue = mMaxPrice
        for(i in 0 until mFilterList.size){
            for(j in 0 until mFilterList[i].values.size){
                mFilterList[i].values[j].isSelected = false
            }
        }

        for(j in 0 until mSpaceFilterList.size){
            mSpaceFilterList[j].isSelected = false
        }



        (applicationContext as RentersApplication).setMoreFilterList(mFilterList)
        (applicationContext as RentersApplication).setSpaceFilterList(mSpaceFilterList)

        setRecyclerViewAdapter()
        setSpaceTypeAdapter()

        baseViewModel.rentersLoader.postValue(false)

    }

    private fun customizeToolbar() {
        tvRightOption.text = getString(R.string.reset_all)
        imgBtnBack.setImageResource(R.drawable.ic_close)
    }

    private fun initView() {

        tvDailyPrice.text = (getString(R.string.daily_price)).plus(" (").plus(mSessionManager.getCurrencySymbol()).plus(")")

        if(intent != null){
            if(intent.hasExtra(Iconstants.ID))
                mSpaceId = intent.getStringExtra(Iconstants.ID)!!
        }

        rangeSeekBarFilter = findViewById(R.id.rangeSeekBarFilter)


        imgBtnBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        tvRightOption.setOnClickListener(this)


        rangeSeekBarFilter.setRangeValues(1, (applicationContext as RentersApplication).getMaxPriceValue())
        rangeSeekBarFilter.selectedMaxValue = (applicationContext as RentersApplication).getMaxPriceValue()
        rangeSeekBarFilter.selectedMinValue = 1
        rangeSeekBarFilter.setTextAboveThumbsColor(ContextCompat.getColor(this,R.color.text_black))
        setLayoutManager()

        toggleInstantBook.setOnCheckedChangeListener{ _, isChecked ->
            mInstantBook = if(isChecked)
                "Yes"
            else
                "No"
            if(isChecked)
                tvRightOption.visibility = View.VISIBLE
            else if((mMinPrice== 1 && mMaxPrice == (applicationContext as RentersApplication).getMaxPriceValue())&& (getSelectedAttributeID().isEmpty() && getSelectedSpaceID().isEmpty()))
                tvRightOption.visibility = View.GONE
        }
        rangeSeekBarFilter.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
            mMinPrice = minValue
            mMaxPrice = maxValue
            if(minValue > 1 || maxValue < (applicationContext as RentersApplication).getMaxPriceValue())
                tvRightOption.visibility = View.VISIBLE
            else if((mInstantBook.isEmpty() || mInstantBook.equals("no",true))&& (getSelectedAttributeID().isEmpty() && getSelectedSpaceID().isEmpty()))
                tvRightOption.visibility = View.GONE

        }

    }

    private fun setLayoutManager() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val layoutManager1 = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        rvFilter.layoutManager = layoutManager
        rvSpaceType.layoutManager = layoutManager1

    }

    private fun setRecyclerViewAdapter() {

        if(mFilterAdapter == null) {
            mFilterAdapter = MoreFilterListAdapter(this, mFilterList,mFilterListener)
            rvFilter.adapter = mFilterAdapter

        } else{
            mFilterAdapter!!.notifyDataSetChanged()
        }


    }

    private fun setSpaceTypeAdapter() {

        if(mSpaceFilterAdapter == null) {
            mSpaceFilterAdapter = MoreFilterAdapter(this, mSpaceFilterList,mFilterListener,-1)
            rvSpaceType.adapter = mSpaceFilterAdapter

        } else{
            mSpaceFilterAdapter!!.notifyDataSetChanged()
        }


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBtnBack -> {
              onBackPressed()
            }
            R.id.btnSave ->{
                (applicationContext as RentersApplication).setMoreFilterList(mFilterList)
                (applicationContext as RentersApplication).setSpaceFilterList(mSpaceFilterList)
                    val resultIntent = Intent()
                    resultIntent.putExtra(Iconstants.SELECTED_ID, getSelectedAttributeID())
                    resultIntent.putExtra(Iconstants.ID, getSelectedSpaceID())
                    resultIntent.putExtra(Iconstants.MIN_PRICE,rangeSeekBarFilter.selectedMinValue)
                    resultIntent.putExtra(Iconstants.MAX_PRICE,rangeSeekBarFilter.selectedMaxValue)
                    resultIntent.putExtra(Iconstants.INSTANCE_BOOK,mInstantBook)
                    setResult(Activity.RESULT_OK, resultIntent)

                finish()
            }
            R.id.tvRightOption -> resetAll()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mFilterList.clear()
        mSpaceFilterList.clear()
        (applicationContext as RentersApplication).setMoreFilterList(mFilterList)
        (applicationContext as RentersApplication).setSpaceFilterList(mSpaceFilterList)
        finish()
    }

    private fun getSelectedAttributeID():String{
        mSelectedAttributeId = ""
        for(i in 0 until mFilterList.size){
            for(j in 0 until mFilterList[i].values.size){
                if(mFilterList[i].values[j].isSelected)
                    mSelectedAttributeId = mSelectedAttributeId.plus(mFilterList[i].values[j].id).plus(",")
            }
        }
        if(mSelectedAttributeId.isNotEmpty()) {
           mSelectedAttributeId =  mSelectedAttributeId.substring(0, mSelectedAttributeId.length - 1)
        }
        return mSelectedAttributeId
    }

    private fun getSelectedSpaceID():String{
        mSpaceId = ""
        for(i in 0 until mSpaceFilterList.size){
                if(mSpaceFilterList[i].isSelected)
                    mSpaceId = (mSpaceId.plus(mSpaceFilterList[i].id)).plus(",")

        }
        if(mSpaceId.isNotEmpty()) {
            mSpaceId = mSpaceId.substring(0, mSpaceId.length - 1)
        }

        return mSpaceId
    }

    override fun onMoreFilterItemClick(item: MoreFilterData, listPosition: Int,isSelected:Boolean) {

        if(listPosition >= 0) {
            for (i in 0 until mFilterList[listPosition].values.size) {
                if (item.id.equals(mFilterList[listPosition].values[i].id, true)) {
                    mFilterList[listPosition].values[i].isSelected = isSelected
                    break
                }

            }
        } else if(listPosition == -1){

            for (i in 0 until mSpaceFilterList.size) {
                if (item.id.equals(mSpaceFilterList[i].id, true)) {
                    mSpaceFilterList[i].isSelected = isSelected
                    break
                }

            }
        }

        if(getSelectedAttributeID().isNotEmpty() || getSelectedSpaceID().isNotEmpty())
            tvRightOption.visibility = View.VISIBLE
        else if((mInstantBook.equals("No",true)|| mInstantBook.isEmpty()) && (mMinPrice== 1 && mMaxPrice == (applicationContext as RentersApplication).getMaxPriceValue()))
                tvRightOption.visibility = View.GONE

    }
}
