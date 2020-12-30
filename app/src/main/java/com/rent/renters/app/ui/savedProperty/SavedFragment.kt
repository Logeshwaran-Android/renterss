package com.rent.renters.app.ui.savedProperty


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.data.model.SearchSpaceResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.app.ui.userHomePage.HomeViewModel
import com.rent.renters.app.ui.userHomePage.SearchSpaceActivity
import com.rent.renters.app.ui.userHomePage.SpaceTypeItemAdapter
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*


class SavedFragment : Fragment(), SpaceTypeItemAdapter.SpaceTypeItemClickListener {


    private lateinit var homeViewModel: HomeViewModel
    private var spaceTypeItemAdapter: SpaceTypeItemAdapter? = null

    internal lateinit var mContext: Context
    private lateinit var mListener: SpaceTypeItemAdapter.SpaceTypeItemClickListener

    private var mSearchSpaceList : ArrayList<SearchSpaceData> = ArrayList()
    private  var mDetailPagePosition : Int ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mListener = this
        initView()
        customizeToolbar()
        initViewModel()
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.initMethod(context as Activity)
        homeViewModel.callGetSavedWishList().observe(viewLifecycleOwner, Observer<SearchSpaceResponse> {
            (activity as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSearchSpaceList.addAll(it.data?.search_results!!)
                if(mSearchSpaceList.size == 0){
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.setOnClickListener{
                        val spaceSizeIntent = Intent(mContext, SearchSpaceActivity::class.java)
                        startActivity(spaceSizeIntent)
                    }

                }else {
                setSpaceTypeAdapter()
                    }

            }
        })
    }

        private fun customizeToolbar() {
    imgBtnBack.visibility = GONE
    tvTitle.text = getString(R.string.saved)
}


    private fun initView( ) {
        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        rvSavedList.layoutManager = layoutManager

    }

    private fun setSpaceTypeAdapter() {

            noDataLayout.visibility = GONE
            if (spaceTypeItemAdapter == null) {
                spaceTypeItemAdapter = SpaceTypeItemAdapter(mContext, mSearchSpaceList, mListener)
                rvSavedList.adapter = spaceTypeItemAdapter
            } else {
                if(mSearchSpaceList.size == 0) {
                    spaceTypeItemAdapter!!.notifyDataSetChanged()
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.setOnClickListener{
                        val spaceSizeIntent = Intent(mContext, SearchSpaceActivity::class.java)
                        startActivity(spaceSizeIntent)
                    }
                }else{
                    spaceTypeItemAdapter!!.notifyDataSetChanged()
                }
            }


    }

    override fun spaceTypeItemClick(item : SearchSpaceData,position: Int) {
        mDetailPagePosition = position
        val detailPageIntent = Intent(mContext, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID,item.id)
        (mContext as Activity).startActivityForResult(detailPageIntent,Iconstants.DETAIL_PAGE_REQUEST_CODE)
       // (mContext as Activity).overridePendingTransition(R.anim.enter, R.anim.exit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == Iconstants.DETAIL_PAGE_REQUEST_CODE){
                val isFavorite = data?.getBooleanExtra(Iconstants.IS_FAVORITE,false)!!
                mDetailPagePosition?.let{
                    mSearchSpaceList[mDetailPagePosition!!].is_favorite = isFavorite
                    spaceTypeItemAdapter!!.notifyItemChanged(mDetailPagePosition!!)
                }
            }
        }
    }


    override fun addRemoveWishList(item: SearchSpaceData, position: Int) {
        homeViewModel.callAddRemoveWishList(item.property_id!!).observe(this, Observer {
            (activity as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                mSearchSpaceList[position].is_favorite = it.data?.is_favorite!!
                mSearchSpaceList.removeAt(position)
                setSpaceTypeAdapter()
                (activity as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            }
        })

    }
}
