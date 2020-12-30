package com.rent.renters.app.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

data class ConversationDetails(var message_id:String ="",var booking_no:String="",var message:String="",var dateAdded:String="",
                               var sender_id:String="",var receiver_id:String="",var sender_firstname:String="",
                               var sender_lastname:String="", var sender_profile_image:String="",
                               var receiver_firstname:String="",var receiver_lastname:String="",var receiver_profile_image:String="",
                               var property_id:String="")
data class ViewChatData(var total_item:String?="",var conv_details:ArrayList<ConversationDetails>)
data class ViewChatResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ViewChatData ?= ViewChatData("",arrayListOf())){
    constructor(status: String?,message:String?):this("",status,message, ViewChatData("",arrayListOf()))
}

@Parcelize
data class MessagesItem(var subject:String?="",var message: String?="",var sender_id: String?="",var receiver_id: String?="",
                        var is_read:String?="",var is_star:String?="",var is_read_arr:String?="",var dateAdded: String?="",
                        var firstname:String?="",var profile_image:String?="",var check_in:String?="",var check_out:String?="",
                        var booking_no: String?="",var total_fee:String?="",var booking_status: String?="",var booking_detail_json:String?="",
                        var currency_json:String?="",var type:String?="",var product_name:String?="",var banner_photos:String?="",
                        var owner_name:String?="",var product_id:String?="",var instant_booking:String?=""):Parcelable




data class MessageData(var pagination_count:String?="",var messages:ArrayList<MessagesItem>?= arrayListOf())
data class MessageResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:MessageData?= MessageData("",arrayListOf())){
    constructor(status: String?,message: String?):this("",status,message, MessageData())
}

