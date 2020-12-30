package com.rent.renters.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.TimeZoneResponse
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_time_zone.*
import kotlinx.android.synthetic.main.header_layout.*
import java.util.*


class TimeZoneActivity : BaseActivity(),CustomRecyclerViewAdapter.CustomItemClickListener {


    private lateinit var profileViewModel: ProfileViewModel
    private val mTimeZoneList = ArrayList<Any> ()
    private lateinit var mSelectedValue : String
    private lateinit var mCustomRecyclerViewAdapter: CustomRecyclerViewAdapter

    lateinit var mListener : CustomRecyclerViewAdapter.CustomItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_zone)
        mListener = this
        initViewModel()
        initView()
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.TIME_ZONE)) {
                if(intent.getStringExtra(Iconstants.TIME_ZONE)!!.isNotEmpty()) {
                    etSearch.setText(intent.getStringExtra(Iconstants.TIME_ZONE))
                }
            }

        }

        tvTitle.text = getString(R.string.time_zone)
        imgBtnBack.setOnClickListener{finish()}

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                filter(editable.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun initViewModel() {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.initMethod(this)
        profileViewModel.getTimeZone().observe(this, Observer<TimeZoneResponse> {
            baseViewModel.rentersLoader.postValue(false)
                if (it.status == "1"){
                    mTimeZoneList.clear()
                    mTimeZoneList.addAll(it.data.time_zone)
                    setTimeZoneAdapter()

                }else {
                    baseViewModel.rentersError.postValue( (it.message))
                }
        })
    }

    private fun setTimeZoneAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvTimeZone.layoutManager = layoutManager
        mCustomRecyclerViewAdapter = CustomRecyclerViewAdapter(mTimeZoneList, mListener,Iconstants.TIME_ZONE,false)
        rvTimeZone.adapter = mCustomRecyclerViewAdapter
    }

    override fun onAdapterItemClick(listItem: Any, position: Int,isFrom:String) {
        mSelectedValue = listItem as String
        val resultIntent = Intent()
        resultIntent.putExtra(Iconstants.TIME_ZONE,mSelectedValue)
        setResult(Activity.RESULT_OK,resultIntent)
        finish()

    }

    private fun filter(text: String) {
        val mFilteredList = ArrayList<Any>()

        for (s in mTimeZoneList) {

            if ((s as String).toLowerCase().contains(text.toLowerCase())) {
                mFilteredList.add(s)
            }
        }
        mCustomRecyclerViewAdapter.filterList(mFilteredList)
    }

}
