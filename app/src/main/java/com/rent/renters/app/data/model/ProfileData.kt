package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class TimeZoneResponse(var response_code:String,var status: String,var message:String,var data:TimeZoneData){
    constructor(status: String,message: String):this("",status,message, TimeZoneData(arrayListOf()))
}
data class TimeZoneData(var time_zone: ArrayList<String>)
data class LanguageResponse(var response_code:String,var status: String,var message:String,var data:LanguageData){
    constructor(status: String,message: String):this("",status,message, LanguageData(arrayListOf()))
}
data class LanguageData(var user_language: ArrayList<UserLanguage>)
data class UserLanguage(var id: String, var name: String,var isSelected: Boolean)

data class CurrencyResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:CurrencyData?=CurrencyData("",
        arrayListOf(CurrencyListData("","","")))) {
    constructor(status: String,message: String):this("",status,message, CurrencyData("", arrayListOf()))
}
data class CurrencyData(var default_cur:String,var currency:ArrayList<CurrencyListData>)
data class CurrencyListData(var currency_symbols:String ?="", var currency_type:String?="", var default_currency : String?="")
data class ProfileImageResponse(var response_code:String,var status: String,var message:String,var data:ProfileImageData){
    constructor(status: String,message: String):this("",status,message, ProfileImageData(""))
}
data class ProfileImageData(var profile_image: String)

