package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class ListingStepResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ListingStepData){
    constructor(status: String?,message: String?):this("",status,message, ListingStepData("","",""))
}
data class ListingStepData(var step_one_status:String?="",var step_two_status:String?="",var step_three_status:String?="")

data class ListStepOneValues(var id:String?="",var property_type:String?="",var property_size:String?="",var product_name:String?="",var price:String?="",
                             var currency:String?="",var banner_photos:String?="",var guest_count:String?="",var bedroom_count:String?="",
                             var week_disc:String?="",var month_disc:String?="",var space_type:String?="")

data class ListStepOneViewData(var values: ListStepOneValues?=ListStepOneValues())
data class ListStepOneViewResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ListStepOneViewData){
    constructor(status: String?,message: String?):this("",status,message, ListStepOneViewData())
}

data class NewListingResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:NewListingData?=
NewListingData("",""))

data class NewListingData(var propid:String ?="",var mobile_verification:String?="")

data class MyListingsResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ListingData){
    constructor(status: String?,message: String?):this("",status,message,ListingData(arrayListOf()))
}
data class ListingData(var active_listings:ArrayList<ListingItem>?= arrayListOf())
data class ListingItem(var id:String?="",var product_name: String?="",var banner_photos: String?="",var status:String?="",
                       var base_price:String?="",var host_status:String?="",var seo_url:String?="",var address:String?="",
                       var latitude:String?="",var longitude:String?="",var request:String?="",var pending_steps:String?="")

@Parcelize
data class PropertiesImagesData(var id:String?="",var url:String?=""):Parcelable
data class ListStepTwoViewData(var property_data: ListStepTwoPropertyData?=ListStepTwoPropertyData(),var properties_images:ArrayList<PropertiesImagesData>?= arrayListOf())
data class ListStepTwoViewResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ListStepTwoViewData){
    constructor(status: String?,message: String?):this("",status,message, ListStepTwoViewData())
}

data class ListStepThreeViewData(var property_data: ListStepThreePropertyData?=ListStepThreePropertyData())
data class ListStepThreeViewResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ListStepThreeViewData){
    constructor(status: String?,message: String?):this("",status,message, ListStepThreeViewData())
}

data class ListStepTwoPropertyData(var full_address:String?="",var address: AddressData?=AddressData("","","","","",""),var latitude: String?="",var longitude: String?="",
                                   var summary:String?="",var about_your_place:String?="",var other_things_to_note:String?="",
                                   var space_rules:String?="",var about_neighborhood:String?="")

data class ListStepThreePropertyData(var amenities_id:ArrayList<String>?= arrayListOf(),var cancellation_policy:String?="",var instant_booking:String?="",
                                     var cancellation_rules:String?="",var meta_title:String?="",var meta_keywords:String?="",
                                     var meta_description:String?="",var min_stay:String?="",var max_stay:String?="",var guest_check_in_form:String?="",
                                     var guest_check_in_to:String?="",var guest_check_in_form_before:String?="",var check_in_time_from:String?="",
                                     var check_in_time_till:String?="",var check_out_time:String?="")

data class UploadPropertyImageResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:PropertiesImagesData){
    constructor(status: String?,message: String?):this("",status,message,PropertiesImagesData())
}

