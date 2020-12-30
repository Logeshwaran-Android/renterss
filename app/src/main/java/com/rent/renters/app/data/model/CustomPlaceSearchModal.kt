package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CustomPlaceSearchModal(val address: String,val title:String,val placeId:String,val latitude:String,val longitude:String)
@Parcelize
data class CustomPlaceAddress(var address:String?="",var city:String?="",var state:String?="",var country:String?="",var postalCode:String?="", var knowName:String?="",var latitude:Double, var longitude:Double):Parcelable
