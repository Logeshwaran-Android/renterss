package com.rent.renters.app.data.model

data class TransactionListResponse(var response_code:String ?="",var status:String?="",var message:String ?="",var data:TransactionData){
    constructor(status:String,message: String?):this("",status,message,TransactionData())
}

data class TransactionData(var current_earning:String?="",var total_earning:String?="",var transactions:ArrayList<TransactionItem>?= ArrayList())
data class TransactionItem(var paid_date:String?="",var transaction_id:String?="",var amount:String?="",var booking_no:String?="",var product_name:String?="",
                           var firstname:String?="",var lastname:String?="",var check_in:String?="",var check_out:String?="",
                           var profile_image:String?="",var userid:String?="",var dateAdded:String?="",var total_fee:String?="",
                           var service_fee:String?="",var acceptance_fee:String?="",var host_fee:String?="",var address:AddressData)

data class PayoutPreferenceResponse(var response_code:String ?="",var status:String?="",var message:String ?="",var data:PayoutPreferenceUserDetails?=PayoutPreferenceUserDetails()){
    constructor(status: String?,message: String?) : this("",status,message,PayoutPreferenceUserDetails())
}
data class PayoutPreferenceUserDetails(var UserDetails:PayoutPreferenceItem ?= PayoutPreferenceItem())
data class PayoutPreferenceItem(var pay_account_name:String?="",var pay_account_number:String?="",var pay_bank_name:String?="")



