package com.rent.renters.app.ui.listing


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterData
import com.rent.renters.app.data.model.MoreFilterList
import com.rent.renters.app.data.model.MoreFilterResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.filter.MoreFilterAdapter
import com.rent.renters.app.ui.filter.MoreFilterListAdapter
import kotlinx.android.synthetic.main.fragment_space_amenities.*

import java.util.ArrayList

class SpaceAmenitiesFragment : Fragment(),MoreFilterAdapter.MoreFilterInterface {

    private lateinit var listingViewModel: ListingViewModel
    internal lateinit var mContext: Context

    private var mAmenitiesList = ArrayList<MoreFilterList>()
    private var mAmenitiesAdapter: MoreFilterListAdapter ?= null

    private lateinit var mFilterListener: MoreFilterAdapter.MoreFilterInterface
    private var mSelectedAttributeIdList : ArrayList<String> = ArrayList()
    private var mSelectedAttributeId =""

    var mRoot: View? = null

    private var mPropId =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            mRoot =  inflater.inflate(R.layout.fragment_space_amenities, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mFilterListener = this
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        listingViewModel.callGetAmenitiesList(mPropId).observe(viewLifecycleOwner, Observer<MoreFilterResponse> {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            mAmenitiesList.clear()
            if (it.status == "1") {
                if (it.data?.amenities != null) {
                    mAmenitiesList.addAll(it.data?.amenities!!)
                    for(i in 0 until mAmenitiesList.size){
                        for(j in 0 until mAmenitiesList[i].values.size) {
                            for (k in 0 until mSelectedAttributeIdList.size) {
                                if (mAmenitiesList[i].values[j].id.equals(mSelectedAttributeIdList[k],true)) {
                                    mAmenitiesList[i].values[j].isSelected = true
                                    break
                                }
                            }
                        }
                    }

                    setRecyclerViewAdapter()
                }
            }
        })

    }


    private fun setRecyclerViewAdapter() {

        if(mAmenitiesAdapter == null) {
            mAmenitiesAdapter = MoreFilterListAdapter(context!!, mAmenitiesList,mFilterListener)
            rvAmenities.adapter = mAmenitiesAdapter

        } else{
            mAmenitiesAdapter!!.notifyDataSetChanged()
        }


    }

    private fun initView() {

        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        rvAmenities.layoutManager = layoutManager

        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.AMENITIES_LIST)){
                mSelectedAttributeIdList = arguments?.getStringArrayList(Iconstants.AMENITIES_LIST)!!
            }
            if(arguments!!.containsKey(Iconstants.ID))
                mPropId = arguments?.getString(Iconstants.ID)!!
        }

    }

    override fun onMoreFilterItemClick(item: MoreFilterData, listPosition: Int, isSelected: Boolean) {
        for (i in 0 until mAmenitiesList[listPosition].values.size) {
            if (item.id.equals(mAmenitiesList[listPosition].values[i].id, true)) {
                mAmenitiesList[listPosition].values[i].isSelected = isSelected
                break
            }

        }
    }


    fun getSelectedAmenitiesID():String{
        mSelectedAttributeId = ""
        for(i in 0 until mAmenitiesList.size){
            for(j in 0 until mAmenitiesList[i].values.size){
                if(mAmenitiesList[i].values[j].isSelected)
                    mSelectedAttributeId = mSelectedAttributeId.plus(mAmenitiesList[i].values[j].id).plus(",")
            }
        }
        if(mSelectedAttributeId.isNotEmpty()) {
            mSelectedAttributeId =  mSelectedAttributeId.substring(0, mSelectedAttributeId.length - 1)
        }
        return mSelectedAttributeId
    }

}
