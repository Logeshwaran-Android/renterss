package com.rent.renters.app.ui.listing

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.retrofit.CommonApiRepository
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.BaseViewModel
import com.rent.renters.mylibrary.util.Util
import java.io.File


class ListingViewModel : BaseViewModel() {

    private var mSessionManager: SessionManager? = null
    private lateinit var mActivity: Activity

    private val commonApi: CommonApiRepository = CommonApiRepository.getInstance()
    var blockedDateResponse: MutableLiveData<BlockedDatesResponse> = MutableLiveData()


    fun initMethod(activity: Activity) {
        mActivity = activity
        mSessionManager = SessionManager(activity)
    }

    fun callGetListingSteps(propId:String): MutableLiveData<ListingStepResponse> {
        var response: MutableLiveData<ListingStepResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetListingSteps(mSessionManager!!.getApiHeader(), propId)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetSpaceTypes(): MutableLiveData<TotalSpaceResponse> {
        var spaceTypeResponse: MutableLiveData<TotalSpaceResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            spaceTypeResponse = commonApi.callGetSpaceTypes(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceTypeResponse

    }

    fun callGetPropertyTypes(): MutableLiveData<SpaceTypeResponse> {
        var spaceTypeResponse: MutableLiveData<SpaceTypeResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            spaceTypeResponse = commonApi.callGetPropertyTypes(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceTypeResponse

    }

    fun callGetSubPropertyTypes(id:String): MutableLiveData<PropertyTypeResponse> {
        var spaceTypeResponse: MutableLiveData<PropertyTypeResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            spaceTypeResponse = commonApi.callGetSubPropertyType(mSessionManager!!.getApiHeader(),id)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return spaceTypeResponse

    }


    fun callGetCurrencyList(): MutableLiveData<CurrencyResponse> {

        var currencyResponse: MutableLiveData<CurrencyResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            currencyResponse = commonApi.callGetCurrencyList(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return currencyResponse

    }

    fun callPostListingStepOne(spaceType:String,propType:String,propSize:String,propName:String, currency:String,basePrice:String,propId:String,guestCount:String, bedroomCount:String,weekDiscount:String, monthDiscount:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPostListingStepOne(mSessionManager!!.getApiHeader(),spaceType, propType,propSize,propName,currency,basePrice,propId,guestCount,bedroomCount,weekDiscount,monthDiscount)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callPostListingStepTwo(propId:String,summary:String,about_your_place:String,other_things_to_note:String, space_rules:String,about_neighborhood:String,address: String,zipcode:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPostListingStepTwo(mSessionManager!!.getApiHeader(),propId,summary,about_your_place,other_things_to_note,space_rules,about_neighborhood,address, zipcode)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callPostListingStepThree(propId:String,amenitiesId:String,metaTitle:String,metaKeywords:String, metaDescription:String,instantBooking:String,cancellationPolicy: String,cancellationRules:String,checkInFromTime:String,checkInToTime:String,checkOutTime:String,minStay:String, maxStay:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callPostListingStepThree(mSessionManager!!.getApiHeader(),propId,amenitiesId,metaTitle,metaKeywords,metaDescription,instantBooking,cancellationPolicy,cancellationRules,checkInFromTime,checkInToTime,checkOutTime,minStay,maxStay)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }


    fun callListYourSpace( ): MutableLiveData<NewListingResponse> {
        var response: MutableLiveData<NewListingResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callListYourSpace(mSessionManager!!.getApiHeader())

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callGetMyListings(type:String ): MutableLiveData<MyListingsResponse> {
        var response: MutableLiveData<MyListingsResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetListings(mSessionManager!!.getApiHeader(),type)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }


    fun callGetListingStepOne(propId: String): MutableLiveData<ListStepOneViewResponse> {

        var currencyResponse: MutableLiveData<ListStepOneViewResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            currencyResponse = commonApi.callGetListingStepOne(mSessionManager!!.getApiHeader(),propId)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return currencyResponse

    }
    fun callGetListingStepTwo(propId: String): MutableLiveData<ListStepTwoViewResponse> {

        var currencyResponse: MutableLiveData<ListStepTwoViewResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            currencyResponse = commonApi.callGetListingStepTwo(mSessionManager!!.getApiHeader(),propId)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return currencyResponse

    }

    fun callGetListingStepThree(propId: String): MutableLiveData<ListStepThreeViewResponse> {

        var currencyResponse: MutableLiveData<ListStepThreeViewResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            currencyResponse = commonApi.callGetListingStepThree(mSessionManager!!.getApiHeader(),propId)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return currencyResponse

    }

    fun callUploadPropertyBannerImage(propId: String,imageFile : File) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callUploadPropertyBannerImage(mSessionManager!!.getApiHeader(), propId,imageFile)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return response

    }

    fun callUploadPropertyGalleryImage(propId: String,imageFile : File) : MutableLiveData<UploadPropertyImageResponse> {
        var response: MutableLiveData<UploadPropertyImageResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callUploadPropertyGalleryImage(mSessionManager!!.getApiHeader(), propId,imageFile)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return response

    }

    fun callGetAmenitiesList(id:String): MutableLiveData<MoreFilterResponse> {
        var response: MutableLiveData<MoreFilterResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetAmenitiesList(mSessionManager!!.getApiHeader(),id)
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return response

    }

    fun callGetCancellationPolicy(): MutableLiveData<CancellationPolicyResponse> {
        var response: MutableLiveData<CancellationPolicyResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callGetCancellationPolicy(mSessionManager!!.getApiHeader())
        } else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))

        return response

    }
    fun callDeletePropertyGalleryImage(propId: String,imageId:String) : MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if(Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callDeleteSpaceGalleryImage(mSessionManager!!.getApiHeader(), propId,imageId)
        }
        else
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        return response

    }

    fun callGetBlockedDates(propId: String) : MutableLiveData<BlockedDatesResponse> {

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            blockedDateResponse = commonApi.callGetBlockedDates(mSessionManager!!.getApiHeader(), propId)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return blockedDateResponse
    }

    fun callManageCalendar(fromDate:String,toDate:String,propId:String,status:String,amount:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callManageCalendar(mSessionManager!!.getApiHeader(),fromDate,toDate,propId,status,amount)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callMakePublishRequest(propId:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callMakePublishRequest(mSessionManager!!.getApiHeader(),propId)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }

    fun callChangePropertyStatus(status:String,propId:String): MutableLiveData<CommonResponse> {
        var response: MutableLiveData<CommonResponse> = MutableLiveData()

        if (Util.isNetworkAvailable(mActivity)) {
            (mActivity as BaseActivity).baseViewModel.rentersLoader.postValue(true)
            response = commonApi.callChangePropertyStatus(mSessionManager!!.getApiHeader(),status,propId)

        } else {
            (mActivity as BaseActivity).baseViewModel.rentersError.postValue(mActivity.getString(R.string.network_err))
        }

        return response
    }
}
