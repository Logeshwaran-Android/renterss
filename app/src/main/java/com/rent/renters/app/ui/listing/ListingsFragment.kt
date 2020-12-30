package com.rent.renters.app.ui.listing


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.rent.renters.app.data.model.ListingItem
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.fragment_listings.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*
import java.util.ArrayList

class ListingsFragment : Fragment(), MyListingsAdapter.MyListingClickListener, View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener {


    private lateinit var listingViewModel: ListingViewModel
    private var mMyListingAdapter: MyListingsAdapter? = null
    private val mListOptions = ArrayList<Any>()
    private lateinit var mListingListener: MyListingsAdapter.MyListingClickListener

    private var mMyListings: ArrayList<ListingItem> = ArrayList()
    private lateinit var mBottomListener : CustomRecyclerViewAdapter.CustomItemClickListener
    private var mType ="All"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListingListener = this
        mBottomListener = this
        initView()
        initViewModel()

    }


    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        callGetMyListings()
    }

    private fun callGetMyListings() {
        listingViewModel.callGetMyListings(mType).observe(viewLifecycleOwner, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                mMyListings.clear()
                it.data.active_listings?.let {
                    mMyListings.addAll(it)
                    if(mMyListings.size == 0){
                        swipeRefreshLayout.visibility = View.GONE
                        listingLayout.visibility = View.GONE
                        fabAddList.visibility = View.GONE
                        noDataLayout.visibility = View.VISIBLE
                        btnStartExploring.setOnClickListener{
                            val listYourSpaceIntent = Intent(context, ListYourSpaceActivity::class.java)
                            listYourSpaceIntent.putExtra(Iconstants.IS_FROM, Iconstants.IS_NEW_LISTING)
                            startActivityForResult(listYourSpaceIntent,Iconstants.LISTING_REQUEST_CODE)
                        }
                    } else {
                        setAdapter()
                    }
                }
            }
        })
    }


    private fun initView() {

        imgBtnBack.visibility = View.GONE
        tvTitle.text = getString(R.string.my_listing)


        mListOptions.add("All")
        mListOptions.add("Listed")
        mListOptions.add(("Un listed"))

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMyListing.layoutManager = layoutManager


        fabAddList.setOnClickListener {
            val listYourSpaceIntent = Intent(context, ListYourSpaceActivity::class.java)
            listYourSpaceIntent.putExtra(Iconstants.IS_FROM, Iconstants.IS_NEW_LISTING)
            startActivityForResult(listYourSpaceIntent,Iconstants.LISTING_REQUEST_CODE)
        }
        swipeRefreshLayout.setOnRefreshListener {

            swipeRefreshLayout.isRefreshing = false
            callGetMyListings()
        }

      tvListOption.setOnClickListener(this)
    }

    private fun setAdapter() {

            listingLayout.visibility = View.VISIBLE
            fabAddList.visibility = View.VISIBLE
            noDataLayout.visibility = View.GONE
            if (mMyListingAdapter == null) {
                mMyListingAdapter = MyListingsAdapter(context!!, mMyListings, mListingListener)
                rvMyListing.adapter = mMyListingAdapter
            } else {
                mMyListingAdapter!!.notifyDataSetChanged()
            }

    }

    override fun myListingClick(item: ListingItem, isFrom: String,position:Int) {
        when(isFrom){
            Iconstants.MANAGE_CALENDAR ->{
                val calendarIntent = Intent(context, SetTheSceneActivity::class.java)
                calendarIntent.putExtra(Iconstants.IS_FROM, Iconstants.MANAGE_CALENDAR)
                calendarIntent.putExtra(Iconstants.ID, item.id)
                startActivity(calendarIntent)

            }
            Iconstants.EDIT_LISTING ->{
                val listYourSpaceIntent = Intent(context, ListYourSpaceActivity::class.java)
                listYourSpaceIntent.putExtra(Iconstants.IS_FROM, Iconstants.EDIT_LISTING)
                listYourSpaceIntent.putExtra(Iconstants.ID, item.id)
                startActivityForResult(listYourSpaceIntent,Iconstants.LISTING_REQUEST_CODE)

            }
            Iconstants.PUBLISH_LISTING->{
                listingViewModel.callMakePublishRequest(item.id!!).observe(this, Observer {
                    (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
                    (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
                    mMyListings[position].status = getString(R.string.pending)
                    mMyListingAdapter!!.notifyItemChanged(position)

                })

            }
        }

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.tvListOption ->{
                Util.showBottomDialog(context!!,mListOptions,mBottomListener,Iconstants.LISTING,false)
            }
        }

    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        tvListOption.text = listItem.toString()
            mType = "All"
        if(listItem.toString().equals("Listed",true))
            mType= "Active"
        else if(listItem.toString().equals("Un listed",true))
            mType = "Pending"

        listingViewModel.callGetMyListings(mType).observe(viewLifecycleOwner, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                mMyListings.clear()
                mMyListings.addAll(it.data.active_listings!!)
                setAdapter()
            }
        })
    }

    override fun activeInActiveClick(item: ListingItem, status: String,position:Int) {

        listingViewModel.callChangePropertyStatus(status,item.id!!).observe(viewLifecycleOwner, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            if(it.status == "1"){
                mMyListings[position].host_status = status
                mMyListingAdapter!!.notifyItemChanged(position)


            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     //   super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == Iconstants.LISTING_REQUEST_CODE){
                callGetMyListings()

            }
        }
    }
}// Required empty public constructor
