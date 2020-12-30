package com.rent.renters.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class ViewBookingReviewDetails(var id:String?="",var booking_no:String?="",var review_description:String?="",var review_rating:String?="",
                                 var product_id:String?="",var reviewer_id:String?="",var status:String?="",var dateAdded:String?="",
                                 var owner_id:String?="",var review_category:ArrayList<ReviewCategoryItem>?= arrayListOf())
data class ViewBookingReviewData(var reviewDet:ViewBookingReviewDetails?=ViewBookingReviewDetails())
data class ViewBookingReviewResponse(var response_code:String ?="",var message:String ?="",var status:String?="",var data:ViewBookingReviewData?= ViewBookingReviewData()){
    constructor(status: String?,message: String?):this("",status,message,ViewBookingReviewData())
}

data class ReviewsObjectData(var review:ReviewsData?= ReviewsData("","", arrayListOf(), arrayListOf()))
data class ReviewsData(var tot_avg_review:String?="", var tot_review:String?="", var review_cat:ArrayList<ReviewAverageCategoryItem> ?= arrayListOf(),var review_det:ArrayList<ReviewsListData>?= arrayListOf())
data class ReviewsResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ReviewsObjectData?=ReviewsObjectData(ReviewsData("",
        "", arrayListOf(),arrayListOf(ReviewsListData("","","","","","","",
        "","","","","","","","",""))))){
    constructor(status: String,message: String):this("",status,message, ReviewsObjectData(ReviewsData("","", arrayListOf(),arrayListOf())))
}

@Parcelize
data class ReviewsListData(var id:String?="" ,var booking_no:String?="",var review_description:String?="",var review_rating:String?="",
                           var product_id:String?="",var check_in:String?="",var check_out:String?="",var property_id:String?="",
                           var property_name:String?="",var banner_photos:String?="",var reviewer_id:String?="",var userid:String?="",
                           var dateAdded:String?="",var firstname:String?="",var lastname:String?="",var profile_image:String?="",
                           var product_name:String?="",var review_cat:ArrayList<ReviewAverageCategoryItem> ?= arrayListOf(),
                           var private_description:String?=""): Parcelable



data class ReviewCategoryResponse(var response_code:String?="",var status: String?="",var message:String?="",var data:ReviewCategoryData ){
    constructor(status: String?,message: String?) : this("",status,message,ReviewCategoryData())
}
data class ReviewCategoryData(var review_category:ArrayList<ReviewCategoryItem> ?= arrayListOf())
@Parcelize
data class ReviewCategoryItem(var id:String?="",var title:String?="",var details:String?="",var rating:String?=""):Parcelable
@Parcelize
data class ReviewAverageCategoryItem(var name:String?="",var avg_review:String?=""):Parcelable

