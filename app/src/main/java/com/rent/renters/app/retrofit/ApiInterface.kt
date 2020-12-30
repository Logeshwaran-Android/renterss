package com.rent.renters.app.retrofit

import com.google.android.gms.maps.model.LatLng
import com.rent.renters.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import okhttp3.RequestBody

interface ApiInterface

{

    @GET("api/place/autocomplete/json?key=AIzaSyBbSv6J3Y4Lw7lc_gnl1pHt53rv0Qupw0I")
    fun getAddressListApi( @Query("input") input: String): Call<ResponseBody>

    @GET("api/place/details/json?key=AIzaSyBbSv6J3Y4Lw7lc_gnl1pHt53rv0Qupw0I")
    fun getLatLngApi(@Query("placeid") placeid: String): Call<ResponseBody>

    @GET("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBbSv6J3Y4Lw7lc_gnl1pHt53rv0Qupw0I")
    fun getPlaceDetails(@Query("latlng") latlng: LatLng): Call<ResponseBody>

    @FormUrlEncoded
    @POST("user/login_user")
    fun postLogin( @Field("email") email:String,@Field("password") password :String,@Field("device_type") deviceType :String,@Field("token") token :String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/google_registration")
    fun callGoogleRegistration( @Field("email") email:String,@Field("firstname") firstname :String,@Field("last_name") lastname :String,@Field("google_image") googleImage :String,@Field("device_type") deviceType :String,@Field("token") token :String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/confirm_idverify")
    fun callVerifyEmailId(@HeaderMap header: HashMap<String, String>, @Field("user_id") userId:String,@Field("code") code :String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("user/user_forgot_password")
    fun callForgotPassword( @Field("email") email:String): Call<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("user/update_password")
    fun callUpdatePassword(@HeaderMap header: HashMap<String, String>, @Field("password") password:String,@Field("user_id") userId:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("user/registerUser")
    fun callRegistration( @Field("first_name") firstName:String,@Field("last_name") lastName :String,@Field("email_address") email :String,@Field("check_policy") checkPolicy : String,@Field("password") password :String,@Field("device_type") deviceType :String,@Field("token") token :String): Call<RegistrationResponse>

    @GET("user/get_timezone")
    fun getTimeZone(@HeaderMap header: HashMap<String, String>): Call<TimeZoneResponse>

    @GET("user/user_lang")
    fun getLanguage(@HeaderMap header: HashMap<String, String>): Call<LanguageResponse>

    @FormUrlEncoded
    @POST("user/your_reviews")
    fun getReviews(@HeaderMap header: HashMap<String, String>,@Field("user_id") userId:String,@Field("page") page:Int): Call<ReviewsResponse>

    @GET("user/review_category")
    fun callGetReviewCategory(@HeaderMap header: HashMap<String, String>): Call<ReviewCategoryResponse>

    @FormUrlEncoded
    @POST("user/product_review")
    fun getPropertyReviews(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String,@Field("page") page:Int): Call<ReviewsResponse>

    @FormUrlEncoded
    @POST("user/user_review")
    fun getUserReviews(@HeaderMap header: HashMap<String, String>,@Field("user_id") propId:String,@Field("page") page:Int): Call<ReviewsResponse>

    @GET("user/trust_verification")
    fun getVerificationStatus(@HeaderMap header: HashMap<String, String>): Call<VerificationStatusResponse>

    @FormUrlEncoded
    @POST("user/phone_verfication")
    fun verifyPhoneNumber(@HeaderMap header: HashMap<String, String>, @Field("country_code") verifyCode:String,@Field("mobile_no") mobileNumber:String): Call<VerifiedPhoneResponse>

    @FormUrlEncoded
    @POST("user/verify_phone_num")
    fun verifyPhoneNumberOtp(@HeaderMap header: HashMap<String, String>, @Field("verify_code") verifyCode:String,@Field("mobile_no") mobileNumber:String,@Field("mobile_code") mobileCode:String): Call<VerifiedPhoneResponse>


    @POST("user/email_verification")
    fun verifyEmail(@HeaderMap header: HashMap<String, String>): Call<VerifiedEmailResponse>

    @Multipart
    @POST("user/id_verify")
    fun verifyID(@HeaderMap header: HashMap<String, String>, @Part front_image: MultipartBody.Part, @Part back_image: MultipartBody.Part, @Part("country_code") countryCode: RequestBody, @Part("id_type") idType: RequestBody): Call<IDVerificationResponse>

    @POST("user/cancelaccount")
    fun callDeActivateAccount(@HeaderMap header: HashMap<String, String>): Call<CommonResponse>


    @POST("user/currency_list")
    fun callGetCurrencyList(@HeaderMap header: HashMap<String, String>): Call<CurrencyResponse>

    @FormUrlEncoded
    @POST("user/change_user_password")
    fun callChangePassword(@HeaderMap header: HashMap<String, String>, @Field("current_password") verifyCode:String,@Field("password") mobileNumber:String): Call<CommonResponse>


    @GET("user/notification")
    fun callUserNotification(@HeaderMap header: HashMap<String, String>): Call<NotificationResponse>

    @FormUrlEncoded
    @POST("user/update_notification")
    fun callUpdateUserNotification(@HeaderMap header: HashMap<String, String>, @Field("email_notify") emailNotify:String,@Field("push_notify") pushNotify:String,@Field("text_notify") textNotify:String): Call<CommonResponse>

    @Multipart
    @POST("user/update_photo")
    fun updateProfileImage(@HeaderMap header: HashMap<String, String>, @Part profile_image: MultipartBody.Part): Call<ProfileImageResponse>

    @FormUrlEncoded
    @POST("user/update_profile")
    fun callUpdateProfile(@HeaderMap header: HashMap<String, String>, @Field("first_name") firstName:String,@Field("last_name") lastName:String,
                          @Field("gender") gender:String,
                          @Field("birthdate") dayValue:String,
                          @Field("about") about:String,@Field("city") city:String,@Field("timezone") timeZone:String,@Field("lang_known") langKnown:String): Call<ProfileResponse>

    @GET("user/get_space_type")
    fun callGetSpaceTypes(@HeaderMap header: HashMap<String, String>): Call<TotalSpaceResponse>

    @GET("property/prop_type_list")
    fun callGetPropertyTypes(@HeaderMap header: HashMap<String, String>): Call<SpaceTypeResponse>


    @FormUrlEncoded
    @POST("property/get_property_type")
    fun callGetSubPropertyTypes(@HeaderMap header: HashMap<String, String>,@Field("typeid") typeId:String): Call<PropertyTypeResponse>

    @FormUrlEncoded
    @POST("user/search")
    fun callGetSearchSpaceList(@HeaderMap header: HashMap<String, String>, @Field("city") city:String,
                               @Field("listspace") listSpace:String, @Field("min_sqft") min_sqft:Int,
                               @Field("max_sqft") max_sqft:Int,@Field("min_price") min_price:Int,
                               @Field("max_price") max_price:Int,@Field("instant") instant:String,
                               @Field("fromdate") fromdate:String,@Field("todate") todate:String,
                               @Field("page") page:Int,@Field("attribute") attribute:String,
                               @Field("guest") guest:Int): Call<SearchSpaceResponse>


    @FormUrlEncoded
    @POST("user/amenities_list")
    fun callGetMoreFilterItems(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String): Call<MoreFilterResponse>


    @FormUrlEncoded
    @POST("user/detail_view")
    fun callGetDetailPage(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String): Call<DetailPageResponse>

    @GET("user/logout")
    fun callLogout(@HeaderMap header: HashMap<String, String>):Call<CommonResponse>

    @FormUrlEncoded
    @POST("user/view_profile")
    fun viewProfile(@HeaderMap header: HashMap<String, String>,@Field("user_id") id:String): Call<ProfileResponse>


    @FormUrlEncoded
    @POST("user/google_connect")
    fun callGoogleConnect(@HeaderMap header: HashMap<String, String>, @Field("google_id") googleId:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("user/google_disconnect")
    fun callGoogleDisConnect(@HeaderMap header: HashMap<String, String>, @Field("google_id") googleId:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("booking/payment_calculation")
    fun callGetPaymentCalculation(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String,@Field("start_date") startDate:String,@Field("end_date") endDate:String): Call<PaymentCalculationResponse>

    @FormUrlEncoded
    @POST("property/calendar_block_dates")
    fun callGetCalendarBlockedDates(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String): Call<BlockedDatesResponse>

    @FormUrlEncoded
    @POST("property/calendar_availabilitys")
    fun callGetCalendarAvailability(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String): Call<BlockedDatesResponse>


    @FormUrlEncoded
    @POST("booking/proceed_to_booking")
    fun callProceedToBooking(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String,@Field("start_date") startDate:String,@Field("end_date") endDate:String,@Field("instant") instant:String,
                             @Field("adultcount")adultCount:String,@Field("childcount")childCount:String,
                             @Field("infantcount")infantCount:String): Call<PaymentBookingResponse>

    @FormUrlEncoded
    @POST("booking/make_booking_request")
    fun postBookingRequest(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String,@Field("message") message:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("booking/payment_detail")
    fun callGetReviewAndPayDetails(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String): Call<ReviewAndPayResponse>

    @GET("payment/available_payment_details")
    fun callGetPaymentGateWays(@HeaderMap header: HashMap<String, String>): Call<PaymentGateWayResponse>

    @FormUrlEncoded
    @POST("payment/apply_coupon")
    fun callApplyCoupon(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String,@Field("coupon_code_text") couponCodeText:String): Call<CouponResponse>

    @FormUrlEncoded
    @POST("payment/remove_coupon")
    fun callRemoveCoupon(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("user/add_remove_wishlist")
    fun callAddRemoveWishlist(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<AddRemoveWishListResponse>


    @GET("user/wishlist")
    fun callGetSavedWishList(@HeaderMap header: HashMap<String, String>): Call<SearchSpaceResponse>


    @FormUrlEncoded
    @POST("booking/your_trips")
    fun callGetBookingList(@HeaderMap header: HashMap<String, String>,@Field("type") type:String,@Field("page") page:Int): Call<MyTripsResponse>


    @FormUrlEncoded
    @POST("booking/view_conversation_details")
    fun viewConversationDetails(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String): Call<ViewChatResponse>



    @FormUrlEncoded
    @POST("payment/pay_creditcard")
    fun callPaymentByCard(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String,@Field("payment_type") payment_type:String,
                          @Field("cardtype") cardtype:String,@Field("cardNumber") cardNumber:String,
                          @Field("cardMonth") cardMonth:String,@Field("cardYear") cardYear:String,
                          @Field("securityCode") securityCode:String
    ): Call<CommonResponse>



    @FormUrlEncoded
    @POST("booking/send_message")
    fun sendMessage(@HeaderMap header: HashMap<String, String>,@Field("message") message:String,@Field("senderId") senderId:String,
                          @Field("receiverId") receiverId:String,@Field("booking_no") booking_no:String,
                          @Field("booking_status") booking_status:String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("payment/invoice")
    fun callGetInvoice(@HeaderMap header: HashMap<String, String>,@Field("booking_no") booking_no:String
    ): Call<InvoiceResponse>

    @FormUrlEncoded
    @POST("booking/guest_inbox")
    fun callGetGuestInbox(@HeaderMap header: HashMap<String, String>,@Field("page") page:Int): Call<MessageResponse>

    @FormUrlEncoded
    @POST("booking/owner_inbox")
    fun callGetHostInbox(@HeaderMap header: HashMap<String, String>,@Field("page") page:Int): Call<MessageResponse>

    @FormUrlEncoded
    @POST("user/verify_email_user")
    fun callVerifyEmailAddress(@Field("email_address") email:String): Call<CommonResponse>


    @GET("user/search_filters")
    fun callGetDefaultFilterValues(@HeaderMap header: HashMap<String, String>): Call<FilterDefaultDataResponse>

    @FormUrlEncoded
    @POST("booking/contact_host")
    fun callContactHost(@HeaderMap header: HashMap<String, String>,@Field("start_date") startDate:String,@Field("end_date") endDate:String,
                        @Field("message") message:String,@Field("propId") propId:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("booking/your_reservation")
    fun callGetReservationList(@HeaderMap header: HashMap<String, String>,@Field("type") type:String,@Field("page") page:Int): Call<MyReservationResponse>

    @FormUrlEncoded
    @POST("booking/your_reservation")
    fun callGetReservationFilterList(@HeaderMap header: HashMap<String, String>,@Field("type") type:String,@Field("keyword") keyword:String,
                                     @Field("fromdate") fromdate:String,@Field("todate") todate:String,
                                     @Field("searchtype") searchtype:String): Call<MyReservationResponse>

    @FormUrlEncoded
    @POST("property/list_steps")
    fun callGetListSteps(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<ListingStepResponse>

    @FormUrlEncoded
    @POST("property/list_steps_one")
    fun callPostListingStepOne(@HeaderMap header: HashMap<String, String>,@Field("space_type") spaceType:String,@Field("property_type") propType:String,@Field("property_size") propSize:String,
                              @Field("product_name") productName:String,@Field("currency") currency:String,@Field("c_base_price") basePrice:String,@Field("propId") propId:String,@Field("guest_count") guestCount:String,
                               @Field("bedroom_count") bedroomCount:String, @Field("c_week_disc") weekDiscount:String,
                               @Field("c_month_disc") monthDiscount:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/list_steps_one_view")
    fun callGetListingStepOne(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<ListStepOneViewResponse>

    @GET("property/list_your_space")
    fun callListYourSpace(@HeaderMap header: HashMap<String, String>): Call<NewListingResponse>

    @FormUrlEncoded
    @POST("property/manage_listing")
    fun callGetListings(@HeaderMap header: HashMap<String, String>,@Field("type")type:String): Call<MyListingsResponse>

    @Multipart
    @POST("property/property_images_upload")
    fun callUploadPropertyGalleryImage(@HeaderMap header: HashMap<String, String>, @Part("propId") propId: RequestBody,@Part property_photos: MultipartBody.Part): Call<UploadPropertyImageResponse>

    @Multipart
    @POST("property/property_banner_upload")
    fun callUploadPropertyBannerImage(@HeaderMap header: HashMap<String, String>,@Part("propId") propId: RequestBody, @Part banner_photos: MultipartBody.Part): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/delete_prop_image")
    fun callDeletePropertyGalleryImage(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:Int,@Field("imageId") imageId:Int): Call<CommonResponse>


    @FormUrlEncoded
    @POST("property/list_steps_two")
    fun callPostListingStepTwo(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:Int,@Field("summary") summary:String,
                               @Field("about_your_place") about_your_place:String,@Field("other_things_to_note") other_things_to_note:String,@Field("space_rules") space_rules:String,
                               @Field("about_neighborhood") about_neighborhood:String,@Field("address") address:String,@Field("zipcode") zipcode:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/list_steps_two_view")
    fun callGetListingStepTwo(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<ListStepTwoViewResponse>


    @FormUrlEncoded
    @POST("property/list_steps_three")
    fun callPostListingStepThree(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:Int,@Field("amenities_id") amenities_id:String,
                               @Field("meta_title") meta_title:String,@Field("meta_keywords") meta_keywords:String,@Field("meta_description") meta_description:String,
                               @Field("instant_booking") instant_booking:String,@Field("cancellation_policy") cancellation_policy:String,@Field("cancellation_rules") cancellation_rules:String,
                                 @Field("checkin_time_from") checkInTimeFrom:String, @Field("checkin_time_till")checkInTimeTill:String,
                                 @Field("checkout_time")checkOutTiem:String,@Field("min_stay") minStay:String,
                                 @Field("max_stay")maxStay:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/list_steps_three_view")
    fun callGetListingStepThree(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<ListStepThreeViewResponse>

    @GET("property/cancellation_rules")
    fun callGetCancellationPolicy(@HeaderMap header: HashMap<String, String>): Call<CancellationPolicyResponse>


    @FormUrlEncoded
    @POST("user/add_review")
    fun callPostReview(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String,@Field("review_rating") review_rating:String
                       ,@Field("review_description") review_description:String,@Field("booking_no") booking_no:String,@Field("review_cat") reviewCat:String,@Field("private_review") privateReview:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("user/view_review")
    fun callGetBookingReview(@HeaderMap header: HashMap<String, String>,@Field("booking_no") booking_no:String): Call<ViewBookingReviewResponse>


    @FormUrlEncoded
    @POST("booking/owner_reservation_request")
    fun callOwnerReservationRequest(@HeaderMap header: HashMap<String, String>,@Field("message") message:String
                                    ,@Field("senderId") senderId:String,@Field("receiverId") receiverId:String
                                    ,@Field("booking_no") booking_no:String,@Field("booking_status") booking_status:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/calendar_edit")
    fun callManageCalendar(@HeaderMap header: HashMap<String, String>,@Field("from_date") from_date:String
                                    ,@Field("to_date") to_date:String,@Field("propId") propId:String
                                    ,@Field("status") status:String,@Field("amount") amount:String): Call<CommonResponse>



    @FormUrlEncoded
    @POST("payment/pay_creditcard")
    fun callPaymentByPaypal(@HeaderMap header: HashMap<String, String>,@Field("booking_no") bookingNo:String,@Field("transacionid") transacionid:String,@Field("status") status:String,
                          @Field("errMsg") errMsg:String,@Field("payment_type") payment_type:String): Call<CommonResponse>


    @FormUrlEncoded
    @POST("property/makepublishrequest")
    fun callMakePublishRequest(@HeaderMap header: HashMap<String, String>,@Field("propId") propId:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/property_change_status")
    fun callChangePropertyStatus(@HeaderMap header: HashMap<String, String>,@Field("status") status:String,@Field("propId") propId:String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("property/amenities_details")
    fun callGetAmenitiesList(@HeaderMap header: HashMap<String, String>,@Field("propId") id:String): Call<MoreFilterResponse>

    @FormUrlEncoded
    @POST("booking/transactions")
    fun callGetTransactionList(@HeaderMap header: HashMap<String, String>,@Field("type") type:String): Call<TransactionListResponse>


    @FormUrlEncoded
    @POST("booking/transactions")
    fun callGetTransactionFilterList(@HeaderMap header: HashMap<String, String>,@Field("type") type:String,@Field("keyword") keyword:String,
                                     @Field("datefrom") fromdate:String,@Field("dateto") todate:String,
                                     @Field("transtype") searchtype:String): Call<TransactionListResponse>

    @FormUrlEncoded
    @POST("user/add_payout_preference")
    fun callAddPayoutPreference(@HeaderMap header: HashMap<String, String>,@Field("pay_account_name") pay_account_name:String,@Field("pay_account_number") pay_account_number:String,
                                     @Field("pay_bank_name") pay_bank_name:String): Call<PayoutPreferenceResponse>


    @GET("user/view_payout_preference")
    fun callViewPayoutPreference(@HeaderMap header: HashMap<String, String>): Call<PayoutPreferenceResponse>

    @GET("user/host_stats")
    fun callGetHostStats(@HeaderMap header: HashMap<String, String>): Call<StatsResponse>

    @GET("booking/owner_money_req")
    fun callRequestEarnings(@HeaderMap header: HashMap<String, String>): Call<CommonResponse>


    @FormUrlEncoded
    @POST("booking/booking_details")
    fun callGetBookingDetails(@HeaderMap header: HashMap<String, String>,@Field("booking_no") booking_no:String): Call<BookingDetailsResponse>

    @GET("booking/canc_reason")
    fun callGetCancellationReason(@HeaderMap header: HashMap<String, String>): Call<CancellationReasonResponse>

    @FormUrlEncoded
    @POST("booking/cancel_booking")
    fun callCancelBooking(@HeaderMap header: HashMap<String, String>,@Field("bookingno") bookingNo:String,
                           @Field ("reasonid")reasonid:String,@Field ("canDesc")canDesc:String,
                           @Field ("accname")accname:String,@Field ("bankname")bankname:String,
                           @Field ("accno")accno:String): Call<CancellationReasonResponse>

    @FormUrlEncoded
    @POST("booking/cancellation_booking")
    fun callGetCancellationBooking(@HeaderMap header: HashMap<String, String>,@Field("bookingno") booking_no:String): Call<CancellationBookingResponse>
}


