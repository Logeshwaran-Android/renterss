package com.rent.renters.app.ui.amenities

import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterList
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_amenities_list.*
import kotlinx.android.synthetic.main.header_layout.*
import java.util.ArrayList


class AmenitiesListActivity : BaseActivity() {


    private var mAmenitiesAdapter: AmenitiesListAdapter? = null
    private var mAmenitiesList = ArrayList<MoreFilterList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amenities_list)
        initView()
        customizeToolbar()
    }

    private fun customizeToolbar() {
        imgBtnBack.setImageResource(R.drawable.ic_close)
        imgBtnBack.setOnClickListener { finish() }
    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.AMENITIES_LIST)) {
                val mList = (intent.getParcelableArrayListExtra<MoreFilterList>(Iconstants.AMENITIES_LIST)) as ArrayList
                mAmenitiesList.addAll(mList)
            }

        }


        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvAmenitiesList.layoutManager = layoutManager

        setAmenitiesAdapter()


    }

    private fun setAmenitiesAdapter() {
        if (mAmenitiesAdapter == null) {
            mAmenitiesAdapter = AmenitiesListAdapter(this, mAmenitiesList)
            rvAmenitiesList.adapter = mAmenitiesAdapter
        } else {
            mAmenitiesAdapter!!.notifyDataSetChanged()
        }
    }
}
