package com.rent.renters.app.ui.base

open class Iconstants {
    companion object {

        //Staging url
        const val baseUrl= "http://renters3.quickiz.com/"

        //Local Url
        //const val baseUrl = "http://192.168.1.244/renters/customization/renters3.0/"

        const val mainUrl = baseUrl + "api/v1/"
        const val shareURL = baseUrl + "property/"
        const val google_base_url = "https://maps.googleapis.com/maps/"

        const val terms_and_condition_url = baseUrl + "mobile_pages/terms-and-conditions"
        const val privacy_policy_url = baseUrl +"mobile_pages/privacy-policy"
        const val about_us_url = baseUrl + "mobile_pages/about-us"

        const val API_DATE_TIME_FORMAT = "yyyy-MM-dd"

        //Activity Request code
        const val REQUEST_CALENDAR_PAGE = 34
        const val SET_SCENE_REQUEST_CODE = 100
        const val SET_LOOK_UPS_REQUEST_CODE = 101
        const val SET_GUEST_REQUEST_CODE = 102
        const val CONFIRM_PHONE_REQUEST_CODE = 103
        const val EDIT_PROFILE_REQUEST_CODE = 104
        const val SEARCH_LOCATION_REQUEST_CODE = 105
        const val TIME_ZONE_REQUEST_CODE = 106
        const val ABOUT_ME_PROFILE_REQUEST_CODE = 107
        const val OTP_REQUEST_CODE = 108
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 109
        const val ID_VERIFICATION_REQUEST_CODE = 110
        const val SPACE_SIZE_REQUEST_CODE = 111
        const val MORE_FILTER_REQUEST_CODE = 112
        const val ADD_COUPON_REQUEST_CODE = 113

        const val LISTING_REQUEST_CODE = 114
        const val DETAIL_PAGE_REQUEST_CODE = 115
        const val REVIEW_REQUEST_CODE = 116
        const val MESSAGE_REQUEST_CODE = 117
        const val SPACE_GUEST_REQUEST_CODE = 117
        const val SCREEN_OVERLAY_REQUEST_CODE = 118


        //Bundle keys
        const val REVIEWS = "Reviews"
        const val ABOUT_ME = "ABOUT_ME"
        const val URL = "URL"
        const val PHONE_NUMBER = "PHONE_NUMBER"
        const val IS_FROM = "IS_FROM"
        const val OTP_CODE = "OTP_CODE"
        const val COUNTRY_CODE = "COUNTRY_CODE"
        const val MOBILE_CODE = "MOBILE_CODE"
        const val BUNDLE = "BUNDLE"
        const val LAST_NAME = "LAST_NAME"
        const val FIRST_NAME = "FIRST_NAME"
        const val EMAIL = "EMAIL"
        const val CHECK_POLICY = "CHECK_POLICY"
        const val ID = "ID"
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"
        const val TYPE = "TYPE"
        const val STATUS = "STATUS"
        const val TIME_ZONE = "TIME_ZONE"
        const val LOCATION = "LOCATION"
        const val MIN_SQ_FT = "MIN_SQ_FT"
        const val MAX_SQ_FT = "MAX_SQ_FT"
        const val SELECTED_ID = "SELECTED_ID"
        const val MIN_PRICE = "MIN_PRICE"
        const val MAX_PRICE = "MAX_PRICE"
        const val INSTANCE_BOOK = "INSTANCE_BOOK"
        const val AMENITIES_LIST = "AMENITIES_LIST"
        const val SPACE_RULES = "SPACE_RULES"
        const val CANCELLATION_POLICY = "CANCELLATION_POLICY"
        const val CANCELLATION_RULES = "CANCELLATION_RULES"
        const val USER = "USER"
        const val PROPERTY = "PROPERTY"
        const val PROPERTY_NAME = "PROPERTY_NAME"
        const val PROPERTY_IMAGE = "PROPERTY_IMAGE"
        const val PROPERTY_OWNER = "PROPERTY_OWNER"
        const val SHOW_BLOCKED_DATES = "SHOW_BLOCKED_DATES"
        const val BOOKING_NO = "BOOKING_NO"
        const val PAYMENT_DETAILS = "PAYMENT_DETAILS"
        const val DISABLE_INTERACTION = "DISABLE_INTERACTION"
        const val IMAGE = "IMAGE"
        const val BOOKING_PAGE = "BOOKING_PAGE"
        const val TOTAL = "TOTAL"
        const val CARD_PAYMENT = "CARD_PAYMENT"
        const val GENDER = "GENDER"
        const val LANGUAGE = "LANGUAGE"
        const val PROFILE_IMAGE = "PROFILE_IMAGE"
        const val CURRENCY = "CURRENCY"
        const val VERIFICATION_IMAGE = "VERIFICATION_IMAGE"
        const val OTP = "OTP"
        const val RESERVATION = "RESERVATION"
        const val SPACE_TYPE = "SPACE_TYPE"
        const val SPACE_TITLE = "SPACE_TYPE"
        const val SPACE_SIZE = "SPACE_SIZE"
        const val SPACE_PRICE = "SPACE_PRICE"
        const val HOST = "HOST"
        const val IS_NEW_LISTING = "IS_NEW_LISTING"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val IS_HOST = "IS_HOST"

        const val SUMMARY = "SUMMARY"
        const val ABOUT_YOUR_PLACE = "ABOUT_YOUR_PLACE"
        const val OTHER_THINGS_TO_NOTE = "OTHER_THINGS_TO_NOTE"
        const val ABOUT_NEIGHBORHOOD = "ABOUT_NEIGHBORHOOD"
        const val ADDRESS = "ADDRESS"
        const val ZIPCODE = "ZIPCODE"
        const val SPACE_BANNER_IMAGE = "SPACE_BANNER_IMAGE"
        const val SPACE_GALLERY_IMAGE = "SPACE_GALLERY_IMAGE"
        const val META_TITLE = "META_TITLE"
        const val META_KEYWORD = "META_KEYWORD"
        const val META_DESCRIPTION = "META_DESCRIPTION"
        const val FULL_ADDRESS = "FULL_ADDRESS"
        const val PRE_APPROVE = "PRE_APPROVE"
        const val DECLINE = "DECLINE"
        const val VIEW_REVIEW = "VIEW_REVIEW"
        const val ADD_REVIEW = "ADD_REVIEW"

        const val PAY = "PAY"
        const val MESSAGE = "MESSAGE"
        const val INVOICE = "INVOICE"
        const val AVAILABLE = "AVAILABLE"
        const val UNAVAILABLE = "UNAVAILABLE"
        const val MANAGE_CALENDAR = "MANAGE_CALENDAR"
        const val LISTING = "LISTING"
        const val EDIT_LISTING = "EDIT_LISTING"
        const val PUBLISH_LISTING = "PUBLISH_LISTING"
        const val COUPON = "COUPON"
        const val IS_FAVORITE = "IS_FAVORITE"
        const val DISCOUNT_FEE = "DISCOUNT_FEE"
        const val HINT = "HINT"
        const val BOOKING_SUCCESS = "BOOKING_SUCCESS"
        const val BOOKING_UNSUCCESS = "BOOKING_UNSUCCESS"
        const val ADULT_COUNT = "ADULT_COUNT"
        const val CHILD_COUNT = "CHILD_COUNT"
        const val INFANT_COUNT = "INFANT_COUNT"
        const val WEEKLY_DISCOUNT = "WEEKLY_DISCOUNT"
        const val MONTHLY_DISCOUNT = "MONTHLY_DISCOUNT"
        const val CHECK_IN_FROM_TIME = "CHECK_IN_FROM_TIME"
        const val CHECK_IN_TO_TIME = "CHECK_IN_TO_TIME"
        const val CHECK_OUT_TIME = "CHECK_OUT_TIME"
        const val MINIMUM_STAY = "MINIMUM_STAY"
        const val MAXIMUM_STAY = "MAXIMUM_STAY"
        const val PROPERTY_TYPE = "PROPERTY_TYPE"
        const val GUEST_COUNT = "GUEST_COUNT"
        const val BEDROOM_COUNT = "BEDROOM_COUNT"
        const val CANCEL_BOOKING = "CANCEL_BOOKING"



        //api values
        const val COMPLETED_TRANSACTIONS = "completed"
        const val REQUESTED_TRANSACTIONS = "pending"
        const val TRANSACTIONS = "transaction"
        const val ENQUIRY = "Enquiry"
        const val CREDIT_CARD = "Credit Card"
        const val STRIPE = "Stripe"
        const val ACTIVE = "Active"
        const val INACTIVE = "Inactive"
        const val  ACCEPTED= "Accepted"
        const val  DECLINED= "Declined"
        const val YES = "Yes"

        //push notification keys
        const val PUSH_RESERVATION = "reservation"
        const val PUSH_BOOKINGS = "trips"
        const val PUSH_CHAT = "message"
        const val PUSH_LISTING = "listing"
        const val PUSH_VERIFICATION = "verification"
        const val PUSH_TRANSACTION = "transactions"


    }
}
