package com.rent.renters.app.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PaymentCalculationResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:PaymentCalculationData?=PaymentCalculationData("","","",
        "","","","","")){
    constructor(status: String?,message: String?):this("",message,status, PaymentCalculationData("","","","",
            "","","",""))
}
@Parcelize
data class PaymentCalculationData(var base_price:String? = "", var days:String?="", var total_base_price:String? = "",var booking_fee:String?="",var service_fee:String?="",
                                  var total_booking_fee:String?="",var currencySymbol:String?="",var currencyRate:String?="",
                                  var week_disc_percentage:String?="",var week_discount:String?="",var month_disc_percentage:String?="",
                                  var month_discount:String?=""):Parcelable
data class BlockedDatesResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:BlockedDateList?= BlockedDateList()){
    constructor(status: String?,message: String?):this("",message,status, BlockedDateList())
}

data class BlockedDateList(var details:DefaultPriceDetails?= DefaultPriceDetails(),
                           var specialprice:ArrayList<SpecialPriceData>?= arrayListOf(), var dates:ArrayList<BlockedData>?= arrayListOf())
data class DefaultPriceDetails(var defaultprice:String?="",var currency_symbol:String="",var currency_code:String="")
data class SpecialPriceData(var value:String?="",var price:String?="")

data class BlockedData(var value:String ?="", var status: String?="")

data class BookingDetails(var user_id:String ?="",var owner_id:String?="", var property_id:String?="",var check_in:String?="",
                          var check_out:String?="",var total_fee:String?="",var booking_fee:String?="",var service_fee: String?="",
                          var booking_detail_json:String?="",var currency_json:String?="",var request_type:String?="",var instant_status:String?="")

data class BookingData(var booking_no:String?="",var sender_id:String?="",var receiver_id:String?="",var bookingDet:BookingDetails?=BookingDetails("","","","","","","","","",
        "","",""))
data class PaymentBookingResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:BookingData?=BookingData("",
        "","", BookingDetails("","","","","","","","","",
        "","","")))

data class CouponData(var discount_fee:String?="",var total_fee:String?="")
data class CouponResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:CouponData ?= CouponData("",""))
data class MyTripsResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:MyTripsData?= MyTripsData("",arrayListOf()))
data class MyTripsData(var pagination_count:String?="",var your_trips:ArrayList<MyTripsListData>?= arrayListOf())
data class MyTripsListData(var booking_no:String?="",var check_in: String?="",var check_out: String?="",var base_price: String?="",
                       var booking_fee: String?="",var service_fee: String?="",var total_booking_fee: String?="",var discount_amt:String?="",
                       var status: String?="",var dateAdded:String?="",var product_name:String?="",var seo_url:String?="",
                       var full_address:String?="",var property_image:String?="",var user_image:String?="",var property_id: String?="",
                       var firstname:String?="",var userid: String?="",var lastname:String?="",var is_verified:String?="",var review_status:String?="",var review_added:String?="",var address:AddressData,
                           var canc_status:String?="")


data class InvoiceDetails(var booking_no: String?="",var check_in: String?="",var check_out: String?="",var guest_count:String?="",
                          var discount_fee: String?="",var full_address: String?="",var firstname: String?="",var lastname: String?="",
                          var phone_number: String?="",var mobile_code:String?="",var banner_photos:String?="",var latitude:String?="",
                          var longitude:String?="",var days: String?="",var base_price:String?="",var booking_fee: String?="",
                          var service_fee: String?="",var total_booking_fee: String?="",var curcode:String?="",var pay_date:String?="",
                          var payment_type:String?="",var ownerid:String?="",var product_name:String?="")
data class InvoiceData(var invoice_details:InvoiceDetails)
data class InvoiceResponse(var response_code: String?="",var message:String ?="",var status:String?="",var data:InvoiceData){
    constructor(status: String?,message: String?):this("",message,status, InvoiceData(InvoiceDetails("","","","","","",
            "","","","","","","","","","",
            "","","","","","","")))
}


data class BookingDetailsItem(var booking_no:String?="",var check_in:String?="",var check_out:String?="",var booking_fee:String?="",
                              var service_fee:String?="",var total_fee:String?="",var property_name:String?="",
                              var PropImage:String?="")
data class BookingOwnerGuestDetails(var firstname:String?="",var lastname: String?="",var profileimage:String?="",var is_verified:String?="")

data class BookingDetailsResponse(var response_code: String?="",var message:String ?="",var status:String?="",var data:BookingDetailsData,
                                  var owner_details:BookingOwnerGuestDetails,var guest_details:BookingOwnerGuestDetails){
    constructor(status: String?,message: String?):this("",message,status, BookingDetailsData(BookingDetailsItem()),
            BookingOwnerGuestDetails(), BookingOwnerGuestDetails())
}
data class BookingDetailsData(var details:BookingDetailsItem)


data class CancellationReasonData(var id:String ?="",var reason:String?="",var dateAdded:String?="",var status:String?="",
                                  var lang:String?="",var to_id:String?="",var isSelected:Boolean ?= false)
data class CancellationList(var cancList: ArrayList<CancellationReasonData> ?= arrayListOf())
data class CancellationReasonResponse(var response_code: String?="",var message:String ?="",var status:String?="",var data:CancellationList){
    constructor(status:String?,message: String?) : this("",message,status,CancellationList())
}

data class CancellationBookingResponse(var response_code: String?="",var message:String ?="",var status:String?="",var data:CancellationBookingData){
    constructor(status: String?,message: String?):this("",message,status,CancellationBookingData())
}
data class CancellationBookingData(var product_name:String?="",var banner_photos:String?="",var check_in:String?="",var check_out:String?="",
                                   var total_cost:String?="",var cancellation_message:String?="",var cancellation_types:ArrayList<CancellationReasonData>?= arrayListOf())



