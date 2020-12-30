package com.rent.renters.app.data.model

data class CurrentUserData(var email:String, var phone_number:String, var mobile_code:String,var email_notify:String, var text_notify:String)
data class NotificationResponse(var response_code: String,var status: String,var message:String,var data:NotificationData){
    constructor(status: String,message: String):this("",status,message, NotificationData(CurrentUserData("","",
            "","","")))
}
data class NotificationData(var current_user_data:CurrentUserData)
data class CommonResponse(var response_code:String,var status: String,var message:String){
    constructor(status: String,message: String):this("",status,message)
}

data class StatsResponse(var response_code: String?="",var status: String?="",var message: String?="",var data:StatsData?=StatsData(StatsEarningsData(),CommonData("","","",
        "","","","","","","","",""))){
    constructor(status: String,message: String):this("",status,message, StatsData(StatsEarningsData(),CommonData("","","",
            "","","","","","","","","")))
}
data class StatsEarningsData(var current_payout:String?="",var total_payout:String?="",var upcoming_payout:String?="",var overallratings:String?="",
                             var tot_review:String?="")
data class StatsData(var earnings:StatsEarningsData?= StatsEarningsData(), var commonArr:CommonData)
