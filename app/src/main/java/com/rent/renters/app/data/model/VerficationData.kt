package com.rent.renters.app.data.model

data class VerifiedPhoneResponse(var response_code: String,var status: String,var message:String,var data:VerifiedPhoneData){
    constructor(status: String,message: String):this("",status,message, VerifiedPhoneData(VerifiedPhoneUserDetails(""),""))
}
data class VerifiedPhoneData(var UserDetails: VerifiedPhoneUserDetails,var verificationcode:String)
data class VerifiedPhoneUserDetails(var phone_verified: String)

data class VerifiedEmailResponse(var response_code:String,var status: String,var message:String){
    constructor(status: String,message: String):this("",status,message)
}


data class VerificationStatusResponse(var response_code:String,var status: String,var message:String,var data:VerificationData){
    constructor(status: String,message: String):this("",status,message, VerificationData(VerificationDetailsData("","","",
            "","","","","","","","")))
}
data class VerificationData(var verification: VerificationDetailsData)
data class VerificationDetailsData(var email: String, var email_verified : String, var phone_number: String, var phone_verified : String,
                                   var google: String, var country_code: String, var is_verified: String, var mobile_code : String,
                                   var front_id_verify_image : String, var back_id_verify_image: String, var is_verified_id_type: String,var description:String?="")
data class IDVerificationResponse(var response_code:String,var status: String,var message:String){
    constructor(status: String,message: String):this("",status,message)
}

