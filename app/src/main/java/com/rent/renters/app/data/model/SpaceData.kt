package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class SpaceTypeResponse(var response_code: String,var status: String,var message:String,var data:SpaceTypeList){
    constructor(status: String,message: String):this("",status,message, SpaceTypeList(arrayListOf<SpaceTypeData>()))
}
data class SpaceTypeList(var list:ArrayList<SpaceTypeData> = ArrayList())
data class SpaceTypeData(var name:String, var id:String)
data class SearchSpaceResponse(var response_code: String?="",var status: String?="",var message:String,var data:SearchSpaceList?= SearchSpaceList("","", "",arrayListOf())){
    constructor(status: String,message: String):this("",status,message, SearchSpaceList("","","",arrayListOf<SearchSpaceData>()))
}
data class SearchSpaceList(var paginationNo:String?="",var result_count:String?="",var pagination_count:String?="",var search_results:ArrayList<SearchSpaceData>?= arrayListOf())
@Parcelize
data class SearchSpaceData(var product_name:String? ="", var id:String?="", var seo_url:String?="", var banner_photos:String?="", var address:AddressData?,
                           var base_price:String?="", var property_size:String?="", var latitude:String?="", var longitude:String?="", var full_address:String?="", var galleryimg:ArrayList<String>?= arrayListOf(),  var is_favorite:Boolean, var instant_booking:String?="",var isSelected:Boolean = false,
                           var property_id:String?=""):Parcelable

data class MoreFilterResponse(var response_code: String?="",var status: String?="",var message:String?="", var data:MoreFilterListObject?= MoreFilterListObject(arrayListOf(), arrayListOf())){
    constructor(status: String,message: String):this("",status,message, MoreFilterListObject(arrayListOf(), arrayListOf()))

}
data class MoreFilterListObject(var space_list : ArrayList<MoreFilterData>?= arrayListOf(), var amenities : ArrayList<MoreFilterList>?= arrayListOf())
@Parcelize
data class MoreFilterList(var name:String, var values:ArrayList<MoreFilterData>):Parcelable
@Parcelize
data class MoreFilterData(var id: String,var listvalues:String, var image:String, var toId: String,var isSelected: Boolean):Parcelable

data class AddRemoveWishListResponse(var response_code: String?="",var status: String?="",var message:String?="",var data:AddRemoveWishListData?=AddRemoveWishListData()){
    constructor(status: String,message: String):this("",status,message,AddRemoveWishListData(false))
}
data class AddRemoveWishListData(var is_favorite: Boolean ?= false)

data class FilterDefaultData(var min_price:Int?,var max_price:Int?,var min_sqft:Int?,var max_sqft:Int?)
data class FilterDefaultDataResponse(var response_code: String?="",var status: String?="",var message:String?="",var data:FilterDefaultData?= FilterDefaultData(1,1,1,1)){
    constructor(status: String?,message: String?) : this("",status,message, FilterDefaultData(1,1,1,1))
}

data class CancellationPolicyResponse(var response_code: String?="",var status: String?="",var message:String?="",var data:CancellationPolicyData){
    constructor(status: String?,message: String?):this("",status,message, CancellationPolicyData(arrayListOf()))
}
data class CancellationPolicyData(var canRules : ArrayList<CancellationPolicyItem> ?= arrayListOf())
data class CancellationPolicyItem(var name:String?="",var to_id: String?="",var description: String?="",var isSelected: Boolean?=false)

data class PropertyTypeData(var name:String?="",var id:String?="",var image:String?="")
data class FeatureSpaceData(var city:String?="",var image:String?="",var average_price:String?="")
data class TrendingSpaceData(var name:String?="", var reviews_count:String?="",var rating:String?="",var banner_photos:String?="",
                             var full_address:String?="",var address:AddressData?=AddressData("","","","" +
                "","",""),var price:String?="",var propId:String?="")
data class TrendingDestinationData(var city_name:String?="",var image:String?="",var description:String?="")

data class TotalSpaceData(var prop_type:ArrayList<PropertyTypeData>?= arrayListOf(), var f_city:ArrayList<FeatureSpaceData>?= arrayListOf(),
                          var t_prop:ArrayList<TrendingSpaceData>?= arrayListOf(),var t_destination:ArrayList<TrendingDestinationData>?= arrayListOf())

data class TotalSpaceResponse(var response_code: String?="",var status: String?="",var message: String?="",var data:TotalSpaceData){
    constructor(status: String?,message: String?):this("",status,message,TotalSpaceData())
}

data class PropertyTypeResponse(var response_code: String?="",var status: String?="",var message: String?="",var data:PropertyTypeDetails){
    constructor(status: String?,message: String?) : this("",status,message, PropertyTypeDetails())
}
data class PropertyTypeDetails(var prop_type: ArrayList<SubPropertyTypeData>?= arrayListOf())
data class SubPropertyTypeData(var name:String?="",var list_space_id:String?="",var id:String?="")

