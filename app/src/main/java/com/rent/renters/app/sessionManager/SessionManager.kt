package com.rent.renters.app.sessionManager

import android.content.Context
import android.content.SharedPreferences
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonData
import com.rent.renters.app.data.model.UserDetailsData

class SessionManager(var mContext: Context) {

    var mSharedPreferenceName = mContext.getString(R.string.app_name)
    var mSharedPreferenceMode = 0
    private var mSharedPreference: SharedPreferences = mContext.getSharedPreferences(mSharedPreferenceName, mSharedPreferenceMode)
    private var mEditor = mSharedPreference.edit()


    private val authToken = "AUTH_TOKEN"
    private val firstName = "FIRST_NAME"
    private val lastName = "LAST_NAME"
    private val profileImage = "PROFILE_IMAGE"
    private val phoneVerified = "PHONE_VERIFIED"
    private val emailVerified = "EMAIL_VERIFIED"
    private val userId = "USER_ID"
    private val currencyCode = "CURRENCY_CODE"
    private val currencySymbol = "CURRENCY_SYMBOL"
    private val phoneNumber = "PHONE_NUMBER"
    private val countryCode = "COUNTRY_CODE"
    private val googleId = "GOOGLE_ID"
    private val googleConnectId = "GOOGLE_CONNECT_ID"
    private val loginType = "LOGIN_TYPE"
    private val mobileCode = "MOBILE_CODE"
    private val email = "EMAIL"
    private val isVerified = "IS_VERIFIED"
    private val setUserType = "SET_USER_TYPE"


    fun getLoginStatus(): Boolean {
        val tempToken = (mSharedPreference.getString(phoneVerified, "No"))
        if (tempToken.equals("No", true)) {
            return false
        }
        return true
    }

    fun createLoginSession(token: String, mCommonData: CommonData) {
        mEditor.putString(authToken, token)
        mEditor.putString(firstName, mCommonData.firstname)
        mEditor.putString(lastName, mCommonData.lastname)
        mEditor.putString(profileImage, mCommonData.profile_image)
        mEditor.putString(phoneVerified, mCommonData.phone_verified)
        mEditor.putString(emailVerified, mCommonData.email_verified)
        mEditor.putString(userId, mCommonData.id)
        mEditor.putString(currencyCode, mCommonData.currency_code)
        mEditor.putString(currencySymbol, mCommonData.cur_symbol)
        mEditor.putString(loginType, mCommonData.loginUserType)
        mEditor.putString(mobileCode, mCommonData.mobile_code)
        mEditor.putString(phoneNumber, mCommonData.phone_number)
        mEditor.putString(email, mCommonData.email)
        mCommonData.is_verified?.let {
            mEditor.putString(isVerified, mCommonData.is_verified)
        }
        mEditor.commit()
        mEditor.apply()
    }

    fun updateUserDetails(mCommonData: UserDetailsData) {
        mEditor.putString(firstName, mCommonData.firstname)
        mEditor.putString(lastName, mCommonData.lastname)
        mEditor.putString(profileImage, mCommonData.profile_image)
        mEditor.putString(phoneVerified, mCommonData.phone_verified)
        mEditor.putString(emailVerified, mCommonData.email_verified)
        mEditor.commit()
        mEditor.apply()
    }

    fun getUserDetails(): CommonData {
        return CommonData(
                mSharedPreference.getString(firstName, "")!!,
                mSharedPreference.getString(lastName, "")!!,
                mSharedPreference.getString(profileImage, "")!!,
                mSharedPreference.getString(phoneVerified, "")!!,
                mSharedPreference.getString(emailVerified, "")!!,
                mSharedPreference.getString(userId, "")!!,
                mSharedPreference.getString(currencyCode, "")!!,
                mSharedPreference.getString(currencySymbol, "")!!,
                mSharedPreference.getString(loginType, "")!!,
                mSharedPreference.getString(mobileCode, "")!!,
                mSharedPreference.getString(phoneNumber, "")!!,
                mSharedPreference.getString(email, "")!!,
                mSharedPreference.getString(isVerified, "")!!)

    }

    fun deleteLoginSession() {
        mEditor.clear()
        mEditor.commit()
        mEditor.apply()
    }

    fun getApiHeader(): HashMap<String, String> {
        val header = HashMap<String, String>()
        mSharedPreference.getString(authToken, "")?.let { header.put("AuthToken", it) }
        mSharedPreference.getString(currencyCode, "")?.let { header.put("Currency", it) }
        return header
    }

    fun updateCurrencyCode(code: String, symbol: String) {
        mEditor.putString(currencyCode, code)
        mEditor.putString(currencySymbol, symbol)
        mEditor.commit()
        mEditor.apply()

    }

    fun updateProfileImage(listItem: String) {
        mEditor.putString(profileImage, listItem)
        mEditor.commit()
        mEditor.apply()

    }

    fun updateVerifyStatus(status: String) {
        mEditor.putString(phoneVerified, status)
        mEditor.commit()
        mEditor.apply()

    }

    fun updatePhoneNumberCountryCode(number: String, code: String, mCode: String) {
        mEditor.putString(phoneNumber, number)
        mEditor.putString(countryCode, code)
        mEditor.putString(mobileCode, mCode)
        mEditor.commit()
        mEditor.apply()

    }

    fun setGoogleID(id: String) {
        mEditor.putString(googleId, id)
        mEditor.commit()
        mEditor.apply()
    }


    fun setUserType(type: String) {
        mEditor.putString(setUserType, type)
        mEditor.commit()
        mEditor.apply()
    }

    fun getUserType(): String {
        return mSharedPreference.getString(setUserType, "").toString()
    }

    fun setGoogleConnectID(id: String) {
        mEditor.putString(googleConnectId, id)
        mEditor.commit()
        mEditor.apply()
    }

    fun getGoogleId(): String {
        return mSharedPreference.getString(googleId, "").toString()
    }

    fun getGoogleConnectId(): String {
        return mSharedPreference.getString(googleConnectId, "").toString()
    }


    fun getPhoneNumber(): String {
        return mSharedPreference.getString(phoneNumber, "").toString()
    }

    fun getMobileCode(): String {
        return mSharedPreference.getString(mobileCode, "").toString()
    }

    fun getCountryCode(): String {
        return mSharedPreference.getString(countryCode, "").toString()
    }

    fun getCurrencyCode(): String {
        return mSharedPreference.getString(currencyCode, "").toString()

    }

    fun getCurrencySymbol(): String {
        return mSharedPreference.getString(currencySymbol, "").toString()

    }

}