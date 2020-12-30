package com.rent.renters.app.ui.userHomePage


import android.app.Activity
import android.content.Context
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
import androidx.recyclerview.widget.GridLayoutManager


import com.rent.renters.R
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.data.model.*
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.detailPage.DetailPageActivity
import com.rent.renters.app.ui.searchLocation.SearchActivity
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment(), View.OnClickListener, SpaceTypesAdapter.SpaceTypeClickListener, WorldSpaceAdapter.SpaceTypeItemClickListener {


    internal var mContext: Context? = null

    private var spaceTypesAdapter: SpaceTypesAdapter? = null
    private var featureSpaceItemAdapter: SpaceItemAdapter? = null
    private var rentersSpaceAdapter: WorldSpaceAdapter? = null
    private var trendingDestinationAdapter: TrendingDestinationAdapter? = null
    private lateinit var homeViewModel: HomeViewModel

    private var mSpaceTypeList: ArrayList<PropertyTypeData> = ArrayList()
    private var mRenterSpaceList: ArrayList<TrendingSpaceData> = ArrayList()
    private var mFeatureSpaceList: ArrayList<FeatureSpaceData> = ArrayList()
    private var mTrendingDestinationList: ArrayList<TrendingDestinationData> = ArrayList()

    private lateinit var mRentersSpaceListener: WorldSpaceAdapter.SpaceTypeItemClickListener

    private lateinit var mListener: SpaceTypesAdapter.SpaceTypeClickListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        mContext = context
        mListener = this
        mRentersSpaceListener = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        tvSearch.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        val layoutManager2 = GridLayoutManager(mContext, 2)
        val layoutManager3 = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)

        rvSpacesTypes.layoutManager = layoutManager
        rvSpacesItems.layoutManager = layoutManager1
        rvPlacesToStay.layoutManager = layoutManager2
        rvTrendingDestination.layoutManager = layoutManager3

    }


    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.initMethod(context as Activity)
        homeViewModel.callGetSpaceTypes().observe(viewLifecycleOwner, Observer<TotalSpaceResponse> {

            if (it.status == "1") {
                mSpaceTypeList.clear()
                mSpaceTypeList.addAll(it.data.prop_type!!)
                setSpaceTypeAdapter()
                mFeatureSpaceList.clear()
                mFeatureSpaceList.addAll(it.data.f_city!!)
                setFeatureSpaceItemAdapter()
                mRenterSpaceList.clear()
                mRenterSpaceList.addAll(it.data.t_prop!!)
                setRentersPlacesAdapter()
                mTrendingDestinationList.clear()
                    mTrendingDestinationList.addAll(it.data.t_destination!!)
                    setTrendingDestinationAdapter()

            }
            shimmer_view_container1.stopShimmer()
            shimmer_view_container1.visibility = View.GONE
            svSpaceType.visibility = View.VISIBLE

        })

        homeViewModel.callGetDefaultFilterValues().observe(viewLifecycleOwner, Observer<FilterDefaultDataResponse> {
            if (it.status == "1") {
                (((context as BaseActivity).applicationContext) as RentersApplication).setDefaultFilterData(it.data!!)
            } else {
                (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            }
        })
    }

    private fun setRentersPlacesAdapter() {
        if (rentersSpaceAdapter == null) {
            rentersSpaceAdapter = WorldSpaceAdapter(context!!, mRentersSpaceListener, mRenterSpaceList)
            rvPlacesToStay.adapter = rentersSpaceAdapter
        } else {
            rentersSpaceAdapter!!.notifyDataSetChanged()
        }

    }


    private fun setFeatureSpaceItemAdapter() {
        if (featureSpaceItemAdapter == null) {
            featureSpaceItemAdapter = SpaceItemAdapter(mContext!!, mFeatureSpaceList)
            rvSpacesItems.adapter = featureSpaceItemAdapter
        } else {
            featureSpaceItemAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setTrendingDestinationAdapter() {
        if (trendingDestinationAdapter == null) {
            trendingDestinationAdapter = TrendingDestinationAdapter(mContext!!, mTrendingDestinationList)
            rvTrendingDestination.adapter = trendingDestinationAdapter
        } else {
            trendingDestinationAdapter!!.notifyDataSetChanged()
        }
    }


    private fun setSpaceTypeAdapter() {
        if (spaceTypesAdapter == null) {
            spaceTypesAdapter = SpaceTypesAdapter(mContext!!, mSpaceTypeList, mListener)
            rvSpacesTypes.adapter = spaceTypesAdapter
        } else {
            spaceTypesAdapter!!.notifyDataSetChanged()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.tvSearch -> {
                val searchIntent = Intent(mContext, SearchActivity::class.java)
                startActivityForResult(searchIntent, Iconstants.SEARCH_LOCATION_REQUEST_CODE)

            }
        }
    }

    override fun spaceTypeClick(item: PropertyTypeData) {
        val spaceSizeIntent = Intent(mContext, SearchSpaceActivity::class.java)
        spaceSizeIntent.putExtra(Iconstants.ID, item.id)
        startActivity(spaceSizeIntent)

    }

    override fun onPause() {
        super.onPause()
        shimmer_view_container1.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container1.startShimmer()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val spaceSizeIntent = Intent(mContext, SearchSpaceActivity::class.java)
        if (resultCode == Activity.RESULT_OK) {
            if (data!!.hasExtra(Iconstants.LOCATION)) {
                val customAddress = data.getParcelableExtra<CustomPlaceAddress>(Iconstants.LOCATION)
                spaceSizeIntent.putExtra(Iconstants.LOCATION, customAddress?.address)
                startActivity(spaceSizeIntent)
            }
        }

    }
    override fun spaceTypeItemClick(item: TrendingSpaceData) {
        val detailPageIntent = Intent(context!!, DetailPageActivity::class.java)
        detailPageIntent.putExtra(Iconstants.ID, item.propId)
        startActivity(detailPageIntent)
    }


}
