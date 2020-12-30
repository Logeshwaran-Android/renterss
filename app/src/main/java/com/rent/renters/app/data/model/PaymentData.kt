package com.rent.renters.app.data.model

data class PaymentGateWayResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:PaymentData){
    constructor(status: String?,message: String?):this("",status,message, PaymentData(arrayListOf()))
}
data class PaymentData(var gateways:ArrayList<PaymentGateWaysData>)

data class PaymentGateWaysData(var id:String ?="",var gateway_name:String ?="",var status:String?="")

data class ReviewAndPayResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ReviewAndPayData){
    constructor(status: String?,message: String?):this("",status,message, ReviewAndPayData(PropertyDetailData("","","",
            "","","",AddressData("","","","","",""),"","","","","",
            "","","","","","","","",
            "","","","","","","",""), OwnerData("",
            "","","","","","",""),PaymentCalculationData("","","",
            "",""),PaymentCalculationData("","","","","")))
}
data class ReviewAndPayData(var property_det:PropertyDetailData?=PropertyDetailData("","","","",
        "","", AddressData("","","","","",""),"","","","","","",
        "","","","","","","","","" ,
        "","","","","","",false), var owner_det:OwnerData?= OwnerData("","","","","","","",""), var payment_det:PaymentCalculationData?=PaymentCalculationData(),var price_original_val:PaymentCalculationData?=PaymentCalculationData())


