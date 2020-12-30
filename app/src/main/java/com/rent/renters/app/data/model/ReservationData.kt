package com.rent.renters.app.data.model

data class MyReservationResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:MyReservationData?= MyReservationData("",arrayListOf()))
data class MyReservationData(var pagination_count:String?="",var reservations:ArrayList<MyReservationListData>?= arrayListOf())
data class MyReservationListData(var booking_no:String?="",var check_in: String?="",var check_out: String?="",var base_price: String?="",
                           var booking_fee: String?="",var service_fee: String?="",var total_booking_fee: String?="",var discount_amt:String?="",
                           var booking_status: String?="",var paid_status: String?="",var dateAdded:String?="",var product_name:String?="",var seo_url:String?="",
                           var full_address:String?="",var property_image:String?="",var user_image:String?="",var propid: String?="",
                           var firstname:String?="",var userid: String?="",var lastname:String?="",var is_verified:String?="",var banner_photos:String?="",var profile_image:String?="",
var address:AddressData?=AddressData("","","","","",""))

data class ReservationFilterData(var name:String?="",var key:String?="",var isSelected:Boolean?=false){
    constructor():this("","",false)
}