package com.rent.renters.app

import android.app.Application
import android.location.Location
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen

import com.rent.renters.app.data.model.FilterDefaultData
import com.rent.renters.app.data.model.MoreFilterData
import com.rent.renters.app.data.model.MoreFilterList


class RentersApplication : Application() {

    var mMoreFilterList: ArrayList<MoreFilterList> = ArrayList()
    var mSpaceFilterList: ArrayList<MoreFilterData> = ArrayList()



    var mMaxPrice: Int = 3000
    var mMaxSpace: Int = 10000
    var mMinPrice: Int = 0
    var mMinSpace: Int = 0


    var mLatitude: Double = 0.0
    var mLongitude: Double = 0.0

    var mLastLocation: Location? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Initialize ThreeTenABP library
        AndroidThreeTen.init(this)
    }

    fun getMoreFilterList(): ArrayList<MoreFilterList> {
        return mMoreFilterList
    }

    fun setSpaceFilterList(list: ArrayList<MoreFilterData>) {
        mSpaceFilterList = list
    }

    fun getSpaceFilterList(): ArrayList<MoreFilterData> {
        return mSpaceFilterList
    }

    fun setMoreFilterList(list: ArrayList<MoreFilterList>) {
        mMoreFilterList = list
    }


    fun getMaxPriceValue(): Int {
        return mMaxPrice
    }

    fun getMaxSpaceValue(): Int {
        return mMaxSpace
    }

    fun getMinPriceValue(): Int {
        return mMinPrice
    }

    fun getMinSpaceValue(): Int {
        return mMinSpace
    }


    fun setDefaultFilterData(data: FilterDefaultData) {
        mMinPrice = data.min_price!!
        mMaxPrice = data.max_price!!
        mMinSpace = data.min_sqft!!
        mMaxSpace = data.max_sqft!!
    }


}