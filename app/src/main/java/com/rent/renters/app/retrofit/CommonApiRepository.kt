package com.rent.renters.app.retrofit

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.rent.renters.app.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import okhttp3.RequestBody
import okhttp3.MultipartBody


class CommonApiRepository {
    val mErrormsg = "Error in network connection"

    companion object {
        private lateinit var apiService: CommonApiRepository
        fun getInstance(): CommonApiRepository {
            apiService = CommonApiRepository()
            return apiService
        }
    }

    fun postLogin(email: String, pw: String): MutableLiveData<LoginResponse> {
        val loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.postLogin(email, pw, "android",FirebaseInstanceId.getInstance().getToken()!! )
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    loginResponse.value = response.body()
                }else{
                    val failureResponse = LoginResponse("0",response.message())
                    loginResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val failureResponse = LoginResponse("0",mErrormsg)
                loginResponse.value = failureResponse
            }

        })
        return loginResponse
    }

    fun callGoogleRegistration(email: String, firstName: String,lastName: String, google_image: String): MutableLiveData<LoginResponse> {
        val loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGoogleRegistration(email, firstName, lastName,google_image, "android", FirebaseInstanceId.getInstance().getToken()!!)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    loginResponse.value = response.body()
                }else{
                    val failureResponse = LoginResponse("0",response.message())
                    loginResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val failureResponse = LoginResponse("0",mErrormsg)
                loginResponse.value = failureResponse
            }

        })
        return loginResponse
    }
    fun callVerifyEmailId(apiHeader: HashMap<String, String>,userId: String, code: String): MutableLiveData<CommonResponse> {
        val loginResponse: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callVerifyEmailId(apiHeader,userId,code)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    loginResponse.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    loginResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                loginResponse.value = failureResponse
            }

        })
        return loginResponse
    }

    fun callRegistration(firstName: String, lastName: String, email: String, checkPolicy: String, password: String): MutableLiveData<RegistrationResponse> {
        val registrationResponse: MutableLiveData<RegistrationResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callRegistration(firstName, lastName, email, checkPolicy, password, "android", FirebaseInstanceId.getInstance().getToken()!!)
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.code() == 200) {
                    registrationResponse.value = response.body()
                }else{
                    val failureResponse = RegistrationResponse("0",response.message())
                    registrationResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                val failureResponse = RegistrationResponse("0",mErrormsg)
                registrationResponse.value = failureResponse
            }

        })
        return registrationResponse
    }

    fun callForgotPassword(email: String): MutableLiveData<ForgotPasswordResponse> {
        val forgotPasswordResponse: MutableLiveData<ForgotPasswordResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callForgotPassword(email)
        call.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.code() == 200) {
                    forgotPasswordResponse.value = response.body()
                }else{
                    val failureResponse = ForgotPasswordResponse("0",response.message())
                    forgotPasswordResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                val failureResponse = ForgotPasswordResponse("0",mErrormsg)
                forgotPasswordResponse.value = failureResponse
            }

        })
        return forgotPasswordResponse
    }


    fun callGetTimeZone(apiHeader: HashMap<String, String>): MutableLiveData<TimeZoneResponse> {
        val timeZoneResponse: MutableLiveData<TimeZoneResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getTimeZone(apiHeader)
        call.enqueue(object : Callback<TimeZoneResponse> {
            override fun onResponse(call: Call<TimeZoneResponse>, response: Response<TimeZoneResponse>) {
                if (response.code() == 200) {
                    timeZoneResponse.value = response.body()
                }else{
                    val failureResponse = TimeZoneResponse("0",response.message())
                    timeZoneResponse.value = failureResponse
                }
            }

            override fun onFailure(call: Call<TimeZoneResponse>, t: Throwable) {
                val failureResponse = TimeZoneResponse("0",mErrormsg)
                timeZoneResponse.value = failureResponse
            }

        })
        return timeZoneResponse
    }

    fun callGetUserLanguage(apiHeader: HashMap<String, String>): MutableLiveData<LanguageResponse> {
        val responseLiveData: MutableLiveData<LanguageResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getLanguage(apiHeader)
        call.enqueue(object : Callback<LanguageResponse> {
            override fun onResponse(call: Call<LanguageResponse>, response: Response<LanguageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = LanguageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<LanguageResponse>, t: Throwable) {
                val failureResponse = LanguageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetReviews(apiHeader: HashMap<String, String>,userId:String, page:Int): MutableLiveData<ReviewsResponse> {
        val responseLiveData: MutableLiveData<ReviewsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getReviews(apiHeader,userId,page)
        call.enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ReviewsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                val failureResponse = ReviewsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetReviewCategory(apiHeader: HashMap<String, String>): MutableLiveData<ReviewCategoryResponse> {
        val responseLiveData: MutableLiveData<ReviewCategoryResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetReviewCategory(apiHeader)
        call.enqueue(object : Callback<ReviewCategoryResponse> {
            override fun onResponse(call: Call<ReviewCategoryResponse>, response: Response<ReviewCategoryResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ReviewCategoryResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ReviewCategoryResponse>, t: Throwable) {
                val failureResponse = ReviewCategoryResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetPropertyReviews(apiHeader: HashMap<String, String>,propId: String,page: Int): MutableLiveData<ReviewsResponse> {
        val responseLiveData: MutableLiveData<ReviewsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getPropertyReviews(apiHeader,propId,page)
        call.enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ReviewsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                val failureResponse = ReviewsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetUserReviews(apiHeader: HashMap<String, String>,userId: String,page: Int): MutableLiveData<ReviewsResponse> {
        val responseLiveData: MutableLiveData<ReviewsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getUserReviews(apiHeader,userId,page)
        call.enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ReviewsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                val failureResponse = ReviewsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetBookingReview(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<ViewBookingReviewResponse> {
        val responseLiveData: MutableLiveData<ViewBookingReviewResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetBookingReview(apiHeader,bookingNo)
        call.enqueue(object : Callback<ViewBookingReviewResponse> {
            override fun onResponse(call: Call<ViewBookingReviewResponse>, response: Response<ViewBookingReviewResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ViewBookingReviewResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ViewBookingReviewResponse>, t: Throwable) {
                val failureResponse = ViewBookingReviewResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetVerificationStatus(apiHeader: HashMap<String, String>): MutableLiveData<VerificationStatusResponse> {
        val responseLiveData: MutableLiveData<VerificationStatusResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.getVerificationStatus(apiHeader)
        call.enqueue(object : Callback<VerificationStatusResponse> {
            override fun onResponse(call: Call<VerificationStatusResponse>, response: Response<VerificationStatusResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = VerificationStatusResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<VerificationStatusResponse>, t: Throwable) {
                val failureResponse = VerificationStatusResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }



    fun callVerifyPhoneNumberOtp(apiHeader: HashMap<String, String>, code: String, number: String,mobileCode: String): MutableLiveData<VerifiedPhoneResponse> {
        val responseLiveData: MutableLiveData<VerifiedPhoneResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.verifyPhoneNumberOtp(apiHeader, code, number,mobileCode)
        call.enqueue(object : Callback<VerifiedPhoneResponse> {
            override fun onResponse(call: Call<VerifiedPhoneResponse>, response: Response<VerifiedPhoneResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = VerifiedPhoneResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<VerifiedPhoneResponse>, t: Throwable) {
                val failureResponse = VerifiedPhoneResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callVerifyPhoneNumber(apiHeader: HashMap<String, String>, code: String, number: String): MutableLiveData<VerifiedPhoneResponse> {
        val responseLiveData: MutableLiveData<VerifiedPhoneResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.verifyPhoneNumber(apiHeader, code, number)
        call.enqueue(object : Callback<VerifiedPhoneResponse> {
            override fun onResponse(call: Call<VerifiedPhoneResponse>, response: Response<VerifiedPhoneResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = VerifiedPhoneResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<VerifiedPhoneResponse>, t: Throwable) {
                val failureResponse = VerifiedPhoneResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callVerifyEmailAddress(apiHeader: HashMap<String, String>): MutableLiveData<VerifiedEmailResponse> {
        val responseLiveData: MutableLiveData<VerifiedEmailResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.verifyEmail(apiHeader)
        call.enqueue(object : Callback<VerifiedEmailResponse> {
            override fun onResponse(call: Call<VerifiedEmailResponse>, response: Response<VerifiedEmailResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = VerifiedEmailResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<VerifiedEmailResponse>, t: Throwable) {
                val failureResponse = VerifiedEmailResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callVerifyID(apiHeader: HashMap<String, String>, countryCode: String, idType: String, frontImageFile: File, backImageFile: File): MutableLiveData<IDVerificationResponse> {
        val responseLiveData: MutableLiveData<IDVerificationResponse> = MutableLiveData()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), frontImageFile)
        val requestFile1 = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), backImageFile)
        val mFrontImage = MultipartBody.Part.createFormData("front_image", frontImageFile.name, requestFile)
        val mBackImage = MultipartBody.Part.createFormData("back_image", backImageFile.name, requestFile1)
        val mCountryCode = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), countryCode)
        val mIdType = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), idType)

        val service = RetrofitInstance.getInstance()
        val call = service.verifyID(apiHeader, mFrontImage, mBackImage, mCountryCode, mIdType)
        call.enqueue(object : Callback<IDVerificationResponse> {
            override fun onResponse(call: Call<IDVerificationResponse>, response: Response<IDVerificationResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = IDVerificationResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<IDVerificationResponse>, t: Throwable) {
                val failureResponse = IDVerificationResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callUpdateProfileImage(apiHeader: HashMap<String, String>, imageFile: File): MutableLiveData<ProfileImageResponse> {
        val responseLiveData: MutableLiveData<ProfileImageResponse> = MutableLiveData()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)

        val body = MultipartBody.Part.createFormData("profile_image", imageFile.name, requestFile)

        val service = RetrofitInstance.getInstance()
        val call = service.updateProfileImage(apiHeader, body)
        call.enqueue(object : Callback<ProfileImageResponse> {
            override fun onResponse(call: Call<ProfileImageResponse>, response: Response<ProfileImageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ProfileImageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ProfileImageResponse>, t: Throwable) {
                val failureResponse = ProfileImageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callUpdateProfile(apiHeader: HashMap<String, String>, firstName: String, lastName: String, gender: String, dob: String, about: String, city: String, timeZone: String, langKnown: String): MutableLiveData<ProfileResponse> {
        val responseLiveData: MutableLiveData<ProfileResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callUpdateProfile(apiHeader, firstName, lastName, gender, dob, about, city, timeZone, langKnown)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = ProfileResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                val failureResponse = ProfileResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetSpaceTypes(apiHeader: HashMap<String, String>): MutableLiveData<TotalSpaceResponse> {
        val responseLiveData: MutableLiveData<TotalSpaceResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetSpaceTypes(apiHeader)
        call.enqueue(object : Callback<TotalSpaceResponse> {
            override fun onResponse(call: Call<TotalSpaceResponse>, response: Response<TotalSpaceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = TotalSpaceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<TotalSpaceResponse>, t: Throwable) {
                
                val failureResponse = TotalSpaceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetPropertyTypes(apiHeader: HashMap<String, String>): MutableLiveData<SpaceTypeResponse> {
        val responseLiveData: MutableLiveData<SpaceTypeResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetPropertyTypes(apiHeader)
        call.enqueue(object : Callback<SpaceTypeResponse> {
            override fun onResponse(call: Call<SpaceTypeResponse>, response: Response<SpaceTypeResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = SpaceTypeResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<SpaceTypeResponse>, t: Throwable) {

                val failureResponse = SpaceTypeResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetMoreFilterItems(apiHeader: HashMap<String, String>,id:String): MutableLiveData<MoreFilterResponse> {
        val responseLiveData: MutableLiveData<MoreFilterResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetMoreFilterItems(apiHeader,id)
        call.enqueue(object : Callback<MoreFilterResponse> {
            override fun onResponse(call: Call<MoreFilterResponse>, response: Response<MoreFilterResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = MoreFilterResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MoreFilterResponse>, t: Throwable) {
                val failureResponse = MoreFilterResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetSubPropertyType(apiHeader: HashMap<String, String>,id:String): MutableLiveData<PropertyTypeResponse> {
        val responseLiveData: MutableLiveData<PropertyTypeResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetSubPropertyTypes(apiHeader,id)
        call.enqueue(object : Callback<PropertyTypeResponse> {
            override fun onResponse(call: Call<PropertyTypeResponse>, response: Response<PropertyTypeResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = PropertyTypeResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PropertyTypeResponse>, t: Throwable) {
                val failureResponse = PropertyTypeResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }





    fun callGetSearchSpaceList(apiHeader: HashMap<String, String>,city:String,listId:String,minSquareFeet:Int,maxSquareFeet:Int,minPrice:Int,maxPrice:Int,instant:String,fromDate:String,toDate:String,page:Int,attribute:String,guest:Int): MutableLiveData<SearchSpaceResponse> {
        val responseLiveData: MutableLiveData<SearchSpaceResponse> = MutableLiveData()
        val service = RetrofitInstance.getInstance()
        val call = service.callGetSearchSpaceList(apiHeader,city,listId,minSquareFeet,maxSquareFeet,minPrice,maxPrice,instant,fromDate,toDate,page,attribute,guest)
        call.enqueue(object : Callback<SearchSpaceResponse> {
            override fun onResponse(call: Call<SearchSpaceResponse>, response: Response<SearchSpaceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = SearchSpaceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<SearchSpaceResponse>, t: Throwable) {
                val failureResponse = SearchSpaceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetDetailPage(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<DetailPageResponse> {
        val responseLiveData: MutableLiveData<DetailPageResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetDetailPage(apiHeader,propId)
        call.enqueue(object : Callback<DetailPageResponse> {
            override fun onResponse(call: Call<DetailPageResponse>, response: Response<DetailPageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = DetailPageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<DetailPageResponse>, t: Throwable) {
                val failureResponse = DetailPageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }
    fun callGetProfile(apiHeader: HashMap<String, String>,userId:String): MutableLiveData<ProfileResponse> {
        val responseLiveData: MutableLiveData<ProfileResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.viewProfile(apiHeader,userId)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ProfileResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                val failureResponse = ProfileResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callLogout(apiHeader: HashMap<String, String>): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callLogout(apiHeader)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }




    fun callDeActivateAccount(apiHeader: HashMap<String, String>): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callDeActivateAccount(apiHeader)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callChangePassword(apiHeader: HashMap<String, String>, currentPassword: String, password: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callChangePassword(apiHeader, currentPassword, password)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callUpdatePassword(apiHeader: HashMap<String, String>, password: String, userId: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callUpdatePassword(apiHeader, password, userId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callUserNotification(apiHeader: HashMap<String, String>): MutableLiveData<NotificationResponse> {
        val responseLiveData: MutableLiveData<NotificationResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callUserNotification(apiHeader)
        call.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = NotificationResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                val failureResponse = NotificationResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callUpdateUserNotification(apiHeader: HashMap<String, String>, emailNotify: String, textNotify: String, pushNotify: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callUpdateUserNotification(apiHeader, emailNotify, pushNotify, textNotify)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetCurrencyList(apiHeader: HashMap<String, String>): MutableLiveData<CurrencyResponse> {
        val responseLiveData: MutableLiveData<CurrencyResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCurrencyList(apiHeader)
        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CurrencyResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                val failureResponse = CurrencyResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetPaymentGateWays(apiHeader: HashMap<String, String>): MutableLiveData<PaymentGateWayResponse> {
        val responseLiveData: MutableLiveData<PaymentGateWayResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetPaymentGateWays(apiHeader)
        call.enqueue(object : Callback<PaymentGateWayResponse> {
            override fun onResponse(call: Call<PaymentGateWayResponse>, response: Response<PaymentGateWayResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = PaymentGateWayResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PaymentGateWayResponse>, t: Throwable) {
                val failureResponse = PaymentGateWayResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callApplyCoupon(apiHeader: HashMap<String, String>,bookingNo: String,couponCode:String): MutableLiveData<CouponResponse> {
        val responseLiveData: MutableLiveData<CouponResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callApplyCoupon(apiHeader,bookingNo,couponCode)
        call.enqueue(object : Callback<CouponResponse> {
            override fun onResponse(call: Call<CouponResponse>, response: Response<CouponResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CouponResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CouponResponse>, t: Throwable) {
                val failureResponse = CouponResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callRemoveCoupon(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callRemoveCoupon(apiHeader,bookingNo)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callAddRemoveWishlist(apiHeader: HashMap<String, String>,propId: String): MutableLiveData<AddRemoveWishListResponse> {
        val responseLiveData: MutableLiveData<AddRemoveWishListResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callAddRemoveWishlist(apiHeader,propId)
        call.enqueue(object : Callback<AddRemoveWishListResponse> {
            override fun onResponse(call: Call<AddRemoveWishListResponse>, response: Response<AddRemoveWishListResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = AddRemoveWishListResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<AddRemoveWishListResponse>, t: Throwable) {
                val failureResponse = AddRemoveWishListResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetSavedWishList(apiHeader: HashMap<String, String>): MutableLiveData<SearchSpaceResponse> {
        val responseLiveData: MutableLiveData<SearchSpaceResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetSavedWishList(apiHeader)
        call.enqueue(object : Callback<SearchSpaceResponse> {
            override fun onResponse(call: Call<SearchSpaceResponse>, response: Response<SearchSpaceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = SearchSpaceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<SearchSpaceResponse>, t: Throwable) {
                val failureResponse = SearchSpaceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetBookingList(apiHeader: HashMap<String, String>,type:String,page:Int): MutableLiveData<MyTripsResponse> {
        val responseLiveData: MutableLiveData<MyTripsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetBookingList(apiHeader,type,page)
        call.enqueue(object : Callback<MyTripsResponse> {
            override fun onResponse(call: Call<MyTripsResponse>, response: Response<MyTripsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MyTripsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MyTripsResponse>, t: Throwable) {
                val failureResponse = MyTripsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetReservationList(apiHeader: HashMap<String, String>,type:String,page:Int): MutableLiveData<MyReservationResponse> {
        val responseLiveData: MutableLiveData<MyReservationResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetReservationList(apiHeader,type,page)
        call.enqueue(object : Callback<MyReservationResponse> {
            override fun onResponse(call: Call<MyReservationResponse>, response: Response<MyReservationResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MyReservationResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MyReservationResponse>, t: Throwable) {
                val failureResponse = MyReservationResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetReservationFilterList(apiHeader: HashMap<String, String>,type:String,keyword:String,fromDate:String,toDate:String,searchType:String): MutableLiveData<MyReservationResponse> {
        val responseLiveData: MutableLiveData<MyReservationResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetReservationFilterList(apiHeader,type,keyword,fromDate,toDate,searchType)
        call.enqueue(object : Callback<MyReservationResponse> {
            override fun onResponse(call: Call<MyReservationResponse>, response: Response<MyReservationResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MyReservationResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MyReservationResponse>, t: Throwable) {
                val failureResponse = MyReservationResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callListYourSpace(apiHeader: HashMap<String, String>): MutableLiveData<NewListingResponse> {
        val responseLiveData: MutableLiveData<NewListingResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callListYourSpace(apiHeader)
        call.enqueue(object : Callback<NewListingResponse> {
            override fun onResponse(call: Call<NewListingResponse>, response: Response<NewListingResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = NewListingResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<NewListingResponse>, t: Throwable) {
                val failureResponse = NewListingResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetListings(apiHeader: HashMap<String, String>,type:String): MutableLiveData<MyListingsResponse> {
        val responseLiveData: MutableLiveData<MyListingsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetListings(apiHeader,type)
        call.enqueue(object : Callback<MyListingsResponse> {
            override fun onResponse(call: Call<MyListingsResponse>, response: Response<MyListingsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MyListingsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MyListingsResponse>, t: Throwable) {
                val failureResponse = MyListingsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetListingSteps(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<ListingStepResponse> {
        val responseLiveData: MutableLiveData<ListingStepResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetListSteps(apiHeader,propId)
        call.enqueue(object : Callback<ListingStepResponse> {
            override fun onResponse(call: Call<ListingStepResponse>, response: Response<ListingStepResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ListingStepResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ListingStepResponse>, t: Throwable) {
                val failureResponse = ListingStepResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }
    fun callPostListingStepOne(apiHeader: HashMap<String, String>,spaceType:String,propType:String,propSize:String,propName:String, currency:String,basePrice:String,propId: String,guestCount:String, bedroomCount:String,weekDiscount:String, monthDiscount:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPostListingStepOne(apiHeader,spaceType,propType,propSize,propName,currency,basePrice,propId,guestCount,bedroomCount,weekDiscount,monthDiscount)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callPostListingStepTwo(apiHeader: HashMap<String, String>,propId:String,summary:String,about_your_place:String,other_things_to_note:String, space_rules:String,about_neighborhood:String,address: String,zipcode:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPostListingStepTwo(apiHeader,propId.toInt(),summary,about_your_place,other_things_to_note,space_rules,about_neighborhood,address, zipcode)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callPostListingStepThree(apiHeader: HashMap<String, String>,propId:String,amenitiesId:String,metaTitle:String,metaKeywords:String, metaDescription:String,instantBooking:String,cancellationPolicy: String,cancellationRules:String,checkInFromTime:String,checkInToTime:String,checkOutTime:String, minStay:String, maxStay:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPostListingStepThree(apiHeader,propId.toInt(),amenitiesId,metaTitle,metaKeywords,metaDescription,instantBooking,cancellationPolicy,cancellationRules,checkInFromTime,checkInToTime,checkOutTime,minStay,maxStay)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetListingStepOne(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<ListStepOneViewResponse> {
        val responseLiveData: MutableLiveData<ListStepOneViewResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetListingStepOne(apiHeader,propId)
        call.enqueue(object : Callback<ListStepOneViewResponse> {
            override fun onResponse(call: Call<ListStepOneViewResponse>, response: Response<ListStepOneViewResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ListStepOneViewResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ListStepOneViewResponse>, t: Throwable) {
                val failureResponse = ListStepOneViewResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetListingStepTwo(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<ListStepTwoViewResponse> {
        val responseLiveData: MutableLiveData<ListStepTwoViewResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetListingStepTwo(apiHeader,propId)
        call.enqueue(object : Callback<ListStepTwoViewResponse> {
            override fun onResponse(call: Call<ListStepTwoViewResponse>, response: Response<ListStepTwoViewResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ListStepTwoViewResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ListStepTwoViewResponse>, t: Throwable) {
                val failureResponse = ListStepTwoViewResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetListingStepThree(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<ListStepThreeViewResponse> {
        val responseLiveData: MutableLiveData<ListStepThreeViewResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetListingStepThree(apiHeader,propId)
        call.enqueue(object : Callback<ListStepThreeViewResponse> {
            override fun onResponse(call: Call<ListStepThreeViewResponse>, response: Response<ListStepThreeViewResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ListStepThreeViewResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ListStepThreeViewResponse>, t: Throwable) {
                val failureResponse = ListStepThreeViewResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetCancellationPolicy(apiHeader: HashMap<String, String>): MutableLiveData<CancellationPolicyResponse> {
        val responseLiveData: MutableLiveData<CancellationPolicyResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCancellationPolicy(apiHeader)
        call.enqueue(object : Callback<CancellationPolicyResponse> {
            override fun onResponse(call: Call<CancellationPolicyResponse>, response: Response<CancellationPolicyResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CancellationPolicyResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CancellationPolicyResponse>, t: Throwable) {
                val failureResponse = CancellationPolicyResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callPostReview(apiHeader: HashMap<String, String>,propId: String,reviewRating:String,reviewDescription:String,bookingNo:String,reviewCat:String,privateReview:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPostReview(apiHeader,propId,reviewRating,reviewDescription,bookingNo,reviewCat,privateReview)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callOwnerReservationRequest(apiHeader: HashMap<String, String>,message:String,senderId:String,receiverId:String,booking_no:String,booking_status:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callOwnerReservationRequest(apiHeader,message,senderId,receiverId,booking_no,booking_status)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callManageCalendar(apiHeader: HashMap<String, String>,fromDate:String,toDate:String,propId:String,status:String,amount:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callManageCalendar(apiHeader,fromDate,toDate,propId,status,amount)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }




    fun viewConversationDetails(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<ViewChatResponse> {
        val responseLiveData: MutableLiveData<ViewChatResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.viewConversationDetails(apiHeader,bookingNo)
        call.enqueue(object : Callback<ViewChatResponse> {
            override fun onResponse(call: Call<ViewChatResponse>, response: Response<ViewChatResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ViewChatResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ViewChatResponse>, t: Throwable) {
                val failureResponse = ViewChatResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callPaymentByCard(apiHeader: HashMap<String, String>,bookingNo: String,paymentType: String,cardType: String,cardNumber: String,cardMonth: String,cardYear: String,securityCode: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPaymentByCard(apiHeader,bookingNo,paymentType,cardType,cardNumber,cardMonth,cardYear,securityCode)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callPaymentByPaypal(apiHeader: HashMap<String, String>,bookingNo:String,transactionId:String,status:String,errMsg:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callPaymentByPaypal(apiHeader,bookingNo,transactionId,status,errMsg,"paypal")
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callMakePublishRequest(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callMakePublishRequest(apiHeader,propId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callChangePropertyStatus(apiHeader: HashMap<String, String>,status:String,propId:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callChangePropertyStatus(apiHeader,status,propId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetAmenitiesList(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<MoreFilterResponse> {
        val responseLiveData: MutableLiveData<MoreFilterResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetAmenitiesList(apiHeader,propId)
        call.enqueue(object : Callback<MoreFilterResponse> {
            override fun onResponse(call: Call<MoreFilterResponse>, response: Response<MoreFilterResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MoreFilterResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MoreFilterResponse>, t: Throwable) {
                val failureResponse = MoreFilterResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetTransactionList(apiHeader: HashMap<String, String>,type:String): MutableLiveData<TransactionListResponse> {
        val responseLiveData: MutableLiveData<TransactionListResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetTransactionList(apiHeader,type)
        call.enqueue(object : Callback<TransactionListResponse> {
            override fun onResponse(call: Call<TransactionListResponse>, response: Response<TransactionListResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = TransactionListResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<TransactionListResponse>, t: Throwable) {
                val failureResponse = TransactionListResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetTransactionFilterList(apiHeader: HashMap<String, String>,type:String,keyword:String,fromDate:String,toDate:String,searchType:String): MutableLiveData<TransactionListResponse> {
        val responseLiveData: MutableLiveData<TransactionListResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetTransactionFilterList(apiHeader,type,keyword,fromDate,toDate,searchType)
        call.enqueue(object : Callback<TransactionListResponse> {
            override fun onResponse(call: Call<TransactionListResponse>, response: Response<TransactionListResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = TransactionListResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<TransactionListResponse>, t: Throwable) {
                val failureResponse = TransactionListResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callAddPayoutPreference(apiHeader: HashMap<String, String>,accountName:String,accountNumber:String,bankName:String): MutableLiveData<PayoutPreferenceResponse> {
        val responseLiveData: MutableLiveData<PayoutPreferenceResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callAddPayoutPreference(apiHeader,accountName,accountNumber,bankName)
        call.enqueue(object : Callback<PayoutPreferenceResponse> {
            override fun onResponse(call: Call<PayoutPreferenceResponse>, response: Response<PayoutPreferenceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = PayoutPreferenceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PayoutPreferenceResponse>, t: Throwable) {
                val failureResponse = PayoutPreferenceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callViewPayoutPreference(apiHeader: HashMap<String, String>): MutableLiveData<PayoutPreferenceResponse> {
        val responseLiveData: MutableLiveData<PayoutPreferenceResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callViewPayoutPreference(apiHeader)
        call.enqueue(object : Callback<PayoutPreferenceResponse> {
            override fun onResponse(call: Call<PayoutPreferenceResponse>, response: Response<PayoutPreferenceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = PayoutPreferenceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PayoutPreferenceResponse>, t: Throwable) {
                val failureResponse = PayoutPreferenceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetHostStats(apiHeader: HashMap<String, String>): MutableLiveData<StatsResponse> {
        val responseLiveData: MutableLiveData<StatsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetHostStats(apiHeader)
        call.enqueue(object : Callback<StatsResponse> {
            override fun onResponse(call: Call<StatsResponse>, response: Response<StatsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = StatsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<StatsResponse>, t: Throwable) {
                val failureResponse = StatsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callRequestEarnings(apiHeader: HashMap<String, String>): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callRequestEarnings(apiHeader)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetBookingDetails(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<BookingDetailsResponse> {
        val responseLiveData: MutableLiveData<BookingDetailsResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetBookingDetails(apiHeader,bookingNo)
        call.enqueue(object : Callback<BookingDetailsResponse> {
            override fun onResponse(call: Call<BookingDetailsResponse>, response: Response<BookingDetailsResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = BookingDetailsResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<BookingDetailsResponse>, t: Throwable) {
                val failureResponse = BookingDetailsResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetCancellationReason(apiHeader: HashMap<String, String>): MutableLiveData<CancellationReasonResponse> {
        val responseLiveData: MutableLiveData<CancellationReasonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCancellationReason(apiHeader)
        call.enqueue(object : Callback<CancellationReasonResponse> {
            override fun onResponse(call: Call<CancellationReasonResponse>, response: Response<CancellationReasonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CancellationReasonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CancellationReasonResponse>, t: Throwable) {
                val failureResponse = CancellationReasonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callCancelBooking(apiHeader: HashMap<String, String>,bookingNo: String,reasonId:String, cancellationDesc:String,accName:String,bankName:String,accNo:String ): MutableLiveData<CancellationReasonResponse> {
        val responseLiveData: MutableLiveData<CancellationReasonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callCancelBooking(apiHeader,bookingNo,reasonId,cancellationDesc,accName,bankName,accNo)
        call.enqueue(object : Callback<CancellationReasonResponse> {
            override fun onResponse(call: Call<CancellationReasonResponse>, response: Response<CancellationReasonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CancellationReasonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CancellationReasonResponse>, t: Throwable) {
                val failureResponse = CancellationReasonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callGetCancellationBooking(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<CancellationBookingResponse> {
        val responseLiveData: MutableLiveData<CancellationBookingResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCancellationBooking(apiHeader,bookingNo)
        call.enqueue(object : Callback<CancellationBookingResponse> {
            override fun onResponse(call: Call<CancellationBookingResponse>, response: Response<CancellationBookingResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CancellationBookingResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CancellationBookingResponse>, t: Throwable) {
                val failureResponse = CancellationBookingResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }



    fun callSendMessage(apiHeader: HashMap<String, String>,message: String,senderId: String,receiverId: String,bookingNo: String,bookingStatus: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.sendMessage(apiHeader,message,senderId,receiverId,bookingNo,bookingStatus)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetInvoice(apiHeader: HashMap<String, String>,bookingNo: String): MutableLiveData<InvoiceResponse> {
        val responseLiveData: MutableLiveData<InvoiceResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetInvoice(apiHeader,bookingNo)
        call.enqueue(object : Callback<InvoiceResponse> {
            override fun onResponse(call: Call<InvoiceResponse>, response: Response<InvoiceResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = InvoiceResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<InvoiceResponse>, t: Throwable) {
                val failureResponse = InvoiceResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetGuestInbox(apiHeader: HashMap<String, String>,page:Int): MutableLiveData<MessageResponse> {
        val responseLiveData: MutableLiveData<MessageResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetGuestInbox(apiHeader,page)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MessageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                val failureResponse = MessageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetHostInbox(apiHeader: HashMap<String, String>,page:Int): MutableLiveData<MessageResponse> {
        val responseLiveData: MutableLiveData<MessageResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetHostInbox(apiHeader,page)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = MessageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                val failureResponse = MessageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callVerifyEmailAddress(email:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callVerifyEmailAddress(email)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetDefaultFilterValues(apiHeader: HashMap<String, String>): MutableLiveData<FilterDefaultDataResponse> {
        val responseLiveData: MutableLiveData<FilterDefaultDataResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetDefaultFilterValues(apiHeader)
        call.enqueue(object : Callback<FilterDefaultDataResponse> {
            override fun onResponse(call: Call<FilterDefaultDataResponse>, response: Response<FilterDefaultDataResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = FilterDefaultDataResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<FilterDefaultDataResponse>, t: Throwable) {
                val failureResponse = FilterDefaultDataResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callContactHost(apiHeader: HashMap<String, String>,startDate: String,endDate: String,message: String,propId: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callContactHost(apiHeader,startDate,endDate,message,propId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGoogleConnect(apiHeader: HashMap<String, String>, googleId: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGoogleConnect(apiHeader, googleId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }
    fun callGoogleDisConnect(apiHeader: HashMap<String, String>, googleId: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGoogleDisConnect(apiHeader, googleId)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callPostBookingRequest(apiHeader: HashMap<String, String>,bookingNo:String,message:String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.postBookingRequest(apiHeader,bookingNo,message)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetReviewAndPayDetails(apiHeader: HashMap<String, String>,bookingNo:String): MutableLiveData<ReviewAndPayResponse> {
        val responseLiveData: MutableLiveData<ReviewAndPayResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetReviewAndPayDetails(apiHeader,bookingNo)
        call.enqueue(object : Callback<ReviewAndPayResponse> {
            override fun onResponse(call: Call<ReviewAndPayResponse>, response: Response<ReviewAndPayResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = ReviewAndPayResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<ReviewAndPayResponse>, t: Throwable) {
                val failureResponse = ReviewAndPayResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetPaymentCalculation(apiHeader: HashMap<String, String>,propId:String,startDate:String,endDate:String): MutableLiveData<PaymentCalculationResponse> {
        val responseLiveData: MutableLiveData<PaymentCalculationResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetPaymentCalculation(apiHeader,propId,startDate,endDate)
        call.enqueue(object : Callback<PaymentCalculationResponse> {
            override fun onResponse(call: Call<PaymentCalculationResponse>, response: Response<PaymentCalculationResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = PaymentCalculationResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PaymentCalculationResponse>, t: Throwable) {
                val failureResponse = PaymentCalculationResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callProceedToBooking(apiHeader: HashMap<String, String>,propId:String,startDate:String,endDate:String,adultCount:String,childCount:String, infantCount:String): MutableLiveData<PaymentBookingResponse> {
        val responseLiveData: MutableLiveData<PaymentBookingResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callProceedToBooking(apiHeader,propId,startDate,endDate,"no",adultCount,childCount,infantCount)
        call.enqueue(object : Callback<PaymentBookingResponse> {
            override fun onResponse(call: Call<PaymentBookingResponse>, response: Response<PaymentBookingResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = PaymentBookingResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<PaymentBookingResponse>, t: Throwable) {
                val failureResponse = PaymentBookingResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetBlockedDates(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<BlockedDatesResponse> {
        val responseLiveData: MutableLiveData<BlockedDatesResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCalendarBlockedDates(apiHeader,propId)
        call.enqueue(object : Callback<BlockedDatesResponse> {
            override fun onResponse(call: Call<BlockedDatesResponse>, response: Response<BlockedDatesResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = BlockedDatesResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<BlockedDatesResponse>, t: Throwable) {
                val failureResponse = BlockedDatesResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callGetCalendarAvailability(apiHeader: HashMap<String, String>,propId:String): MutableLiveData<BlockedDatesResponse> {
        val responseLiveData: MutableLiveData<BlockedDatesResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()
        val call = service.callGetCalendarAvailability(apiHeader,propId)
        call.enqueue(object : Callback<BlockedDatesResponse> {
            override fun onResponse(call: Call<BlockedDatesResponse>, response: Response<BlockedDatesResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                } else{
                    val failureResponse = BlockedDatesResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<BlockedDatesResponse>, t: Throwable) {
                val failureResponse = BlockedDatesResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }

    fun callUploadPropertyBannerImage(apiHeader: HashMap<String, String>, propId: String,  propImageFile: File): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propImageFile)
        val mPropImage = MultipartBody.Part.createFormData("banner_photos", propImageFile.name, requestFile)
        val mPropId = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propId)

        val service = RetrofitInstance.getInstance()
        val call = service.callUploadPropertyBannerImage(apiHeader, mPropId,mPropImage)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callDeleteSpaceGalleryImage(apiHeader: HashMap<String, String>, propId: String,  imageId: String): MutableLiveData<CommonResponse> {
        val responseLiveData: MutableLiveData<CommonResponse> = MutableLiveData()

        val service = RetrofitInstance.getInstance()

        val call = service.callDeletePropertyGalleryImage(apiHeader, propId.toInt(),imageId.toInt())
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = CommonResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                val failureResponse = CommonResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun callUploadPropertyGalleryImage(apiHeader: HashMap<String, String>, propId: String,  propImageFile: File): MutableLiveData<UploadPropertyImageResponse> {
        val responseLiveData: MutableLiveData<UploadPropertyImageResponse> = MutableLiveData()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propImageFile)
        val mPropImage = MultipartBody.Part.createFormData("property_photos", propImageFile.name, requestFile)
        val mPropId = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propId)

        val service = RetrofitInstance.getInstance()
        val call = service.callUploadPropertyGalleryImage(apiHeader, mPropId,mPropImage)
        call.enqueue(object : Callback<UploadPropertyImageResponse> {
            override fun onResponse(call: Call<UploadPropertyImageResponse>, response: Response<UploadPropertyImageResponse>) {
                if (response.code() == 200) {
                    responseLiveData.value = response.body()
                }else{
                    val failureResponse = UploadPropertyImageResponse("0",response.message())
                    responseLiveData.value = failureResponse
                }
            }

            override fun onFailure(call: Call<UploadPropertyImageResponse>, t: Throwable) {
                val failureResponse = UploadPropertyImageResponse("0",mErrormsg)
                responseLiveData.value = failureResponse
            }

        })
        return responseLiveData
    }


    fun getGooglePlaceSearchResult(input: String): MutableLiveData<ArrayList<CustomPlaceSearchModal>> {
        val searchResponse: MutableLiveData<ArrayList<CustomPlaceSearchModal>> = MutableLiveData()

        val service = RetrofitInstance.getGoogleInstance()
        val call = service.getAddressListApi(input)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    val mAddressListArr: ArrayList<CustomPlaceSearchModal> = ArrayList()

                    try {
                        val responseStr = response.body()!!.string()
                        val responseObj = JSONObject(responseStr)
                        val predictionArr = responseObj.getJSONArray("predictions")


                        for (i in 0 until predictionArr.length()) {

                            val termsArr = predictionArr.getJSONObject(i).getJSONArray("terms")

                            for (j in 0 until termsArr.length()) {
                                val customPlaceSearchModal = CustomPlaceSearchModal(predictionArr.getJSONObject(i).getString("description"), predictionArr.getJSONObject(i).getJSONObject("structured_formatting").getString("main_text"),
                                        predictionArr.getJSONObject(i).getString("place_id"), "", "")
                                mAddressListArr.add(customPlaceSearchModal)

                                break
                            }
                        }
                        searchResponse.value = mAddressListArr

                    } catch (e: JSONException) {
                        e.printStackTrace()

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                searchResponse.value = null
            }

        })
        return searchResponse
    }

    fun getLatLng(placeId: String): MutableLiveData<CustomPlaceSearchModal> {

        val searchResponse: MutableLiveData<CustomPlaceSearchModal> = MutableLiveData()


        val call = RetrofitInstance.getGoogleInstance().getLatLngApi(placeId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val responseObj = JSONObject(res)
                        val status = responseObj.getString("status")
                        val placeObject = responseObj.getJSONObject("result")
                        if (status.equals("OK", ignoreCase = true)) {
                            if (placeObject.length() > 0) {
                                val geometryObject = placeObject.getJSONObject("geometry")
                                if (geometryObject.length() > 0) {
                                    val locationObject = geometryObject.getJSONObject("location")
                                    if (locationObject.length() > 0) {
                                        val mResultLatitude = locationObject.getString("lat")
                                        val mResultLongitude = locationObject.getString("lng")
                                        val customPlaceSearchModal = CustomPlaceSearchModal("", "", "", mResultLatitude, mResultLongitude)
                                        searchResponse.value = customPlaceSearchModal
                                    }
                                }
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                searchResponse.value = null

            }

        })
        return searchResponse
    }

    fun getGooglePlaceDetails(latlng: LatLng): MutableLiveData<CustomPlaceAddress> {
        val searchResponse: MutableLiveData<CustomPlaceAddress> = MutableLiveData()

        val service = RetrofitInstance.getGoogleInstance()
        val call = service.getPlaceDetails(latlng)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    val mAddressListArr: ArrayList<CustomPlaceSearchModal> = ArrayList()

                    try {
                        val responseStr = response.body()!!.string()
                        val responseObj = JSONObject(responseStr)


                    } catch (e: JSONException) {
                        e.printStackTrace()

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                searchResponse.value = null
            }

        })
        return searchResponse
    }
}