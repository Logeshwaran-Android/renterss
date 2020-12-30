package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class DetailPageResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:DetailPageData?=DetailPageData(PropertyDetailData("","","","",
        "","", AddressData("","","","","",""),"","","","","","",
        "","","","","","","","","" ,
        "","","","","","",false), OwnerData("","","","" +
        "","","","",""), arrayListOf(), arrayListOf(), DatesList(arrayListOf(), arrayListOf()), arrayListOf(), ReviewData("","", arrayListOf()))){
    constructor(status: String,message: String):this("",status,message, DetailPageData(PropertyDetailData("","","","",
            "","", AddressData("","","","","",""),"","","","","","",
            "","","","","","","","","" ,
            "","","","","","",false), OwnerData("","","","" +
            "","","","",""), arrayListOf(), arrayListOf(), DatesList(arrayListOf(), arrayListOf()), arrayListOf(), ReviewData("","", arrayListOf())))
}
@Parcelize
data class DetailPageData(var propDet:PropertyDetailData?=PropertyDetailData("","","","",
        "","", AddressData("","","","","",""),"","","","","","",
        "","","","","","","","","" ,
        "","","","","","",false), var ownerDet:OwnerData?= OwnerData("","","","","","","",""), var gallery_image:ArrayList<String>?= arrayListOf(),
                          var attributes: ArrayList<MoreFilterList>?= arrayListOf(), var dates:DatesList?= DatesList(arrayListOf(), arrayListOf()), var similar_listing:ArrayList<SearchSpaceData>, var review:ReviewData?= ReviewData("","", arrayListOf())):Parcelable

@Parcelize
data class DatesList(var unavailDates:ArrayList<String>,var bookeddated:ArrayList<String>):Parcelable
@Parcelize
data class ReviewData(var tot_avg_review:String,var tot_review:String, var review_det:ArrayList<ReviewsListData>):Parcelable

@Parcelize
data class AddressData(var country:String, var state:String, var latitude:String, var longitude:String, var city:String, var zipcode:String):Parcelable

@Parcelize
data class PropertyDetailData(var id: String?="", var seo_url:String?="", var banner_photos: String?="", var product_name: String?="", var base_price: String?="",
                              var full_address: String?="", var address: AddressData?= AddressData("","","","","",""), var latitude: String?="", var longitude: String?="", var property_type:String?="",
                              var about_your_place:String?="", var amenities_id:String?="", var property_size:String?="", var summary:String?="",
                              var other_things_to_note:String?="", var space_rules:String?="", var about_neighborhood:String?="", var owner_id:String?="",
                              var instant_booking:String?="", var c_base_price:String?="", var request:String?="", var status:String?="", var currency:String?="",
                              var cancellation_rules:String, var cancellation_policy:String, var cancellation_name:String,
                              var listspace_name:String?="", var listspace_value:String?="", var is_favorite: Boolean?=false, var guest_count:String?="",
                              var bedroom_count:String?="",var bed_count:String?="",var guest_check_in_form:String?="",
                              var guest_check_in_to:String?="",var guest_check_in_form_before:String?=""):Parcelable

@Parcelize
data class OwnerData(var firstname:String, var profile_image:String, var id:String, var is_verified:String, var about_you:String,
                     var address:String, var created:String, var language:String):Parcelable


