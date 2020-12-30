package com.rent.renters.app.data.model


data class LoginResponse(var status: String, var message:String, var data:RegistrationData, var commonArr:CommonData){
    constructor(status:String, message: String) :this(status,message, RegistrationData(""),
            CommonData("","","","","","","","","","","","",""))
}
data class RegistrationData(var jwt_token: String)
data class CommonData(var firstname:String, var lastname:String,var profile_image:String, var email_verified:String, var phone_verified:String,
                           var id:String, var currency_code:String,var cur_symbol:String,var loginUserType:String?,
                      var mobile_code:String?,var phone_number:String?, var email:String?,var is_verified: String?="")


data class RegistrationResponse(var message:String?,var status: String?,var data:RegistrationData,var commonArr:CommonData){
    constructor(status:String, message: String) :this(message,status, RegistrationData(""),
            CommonData("","","","","","","","","","","","",""))
}
data class ForgotPasswordResponse(var status: String,var message:String,var data:List<RegistrationData>){
    constructor(status:String, message: String) :this(status,message, emptyList())
}
data class ProfileResponse(var status: String? ="",var message:String?="", var data : ProfileData?){
    constructor(status:String, message: String) :this(status,message, ProfileData(UserDetailsData("","","",
            "","","","","","","","","","","","","",""),"", arrayListOf(SearchSpaceData("","","","", AddressData("","","","","",""),"","","","","", arrayListOf(),false,"",false)),"","", arrayListOf()))
}
data class ProfileData(var UserDetails :UserDetailsData,var user_language :String,var listing_details:ArrayList<SearchSpaceData>,var  listing_count: String,
var review_count: String,var reviews:ArrayList<ReviewsListData> ?= arrayListOf(),var responsetime:String?="",var responserate:String?="")
data class UserDetailsData(var firstname:String,var lastname :String,var profile_image : String,var about_you:String,var gender:String, var email_verified:String,
                           var phone_verified:String,var is_verified:String, var created:String, var dob: String, var email:String,var phone_number:String,var address:String, var timezone: String,
                           var language:String,var country_code:String,var mobile_code:String)

