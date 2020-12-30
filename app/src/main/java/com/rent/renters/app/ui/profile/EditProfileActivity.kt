package com.rent.renters.app.ui.profile


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod

import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver


import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.text.HtmlCompat

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.reviews.EditProfileReviewsActivity
import com.rent.renters.app.ui.searchLocation.SearchActivity
import com.rent.renters.app.ui.verification.ConfirmPhoneNumberActivity
import com.rent.renters.app.ui.verification.TrustAndVerificationActivity
import com.rent.renters.mylibrary.util.MySpannable
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_edit_profile.*


import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_edit_profile.btnSave
import kotlinx.android.synthetic.main.activity_edit_profile.ivProfile
import kotlinx.android.synthetic.main.activity_edit_profile.tvEmailVal
import kotlinx.android.synthetic.main.header_layout.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EditProfileActivity : BaseActivity(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener {


    private var mAboutMe : String ?=""
    private var mProfileName : String ?=""
    private var mSelecteLanguages : String ?=""
    private var mSelectedLanguagesText : String ?=""
    private lateinit var mSessionManager: SessionManager
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    private val mGenderOptions :ArrayList<Any> = ArrayList()
    private val mLanguageList:ArrayList<Any> = ArrayList()

    private lateinit var mProfileResponse : ProfileResponse

    private var mYear : String = ""
    private var mMonth : String = ""
    private var mDay : String = ""
    private var mDOB : String = ""
    private var mTimeZone : String = ""


    private var mPhoneNumber = ""
    private var mCountryCode =""


    private lateinit var destination : File
    private val mImagePickerList = ArrayList<Any>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mSessionManager = SessionManager(this)
        mBottomDialogListener = this

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.initMethod(this)
        profileViewModel.getProfileDetails(mSessionManager.getUserDetails().id).observe(this, Observer<ProfileResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1") {
                   setProfileData(it)
                }

            }
        })

        profileViewModel.getLanguage().observe(this, Observer<LanguageResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null) {
                if (it.status == "1"){
                    mLanguageList.clear()
                    val languageList = it.data.user_language
                    for (item in languageList) {
                        mLanguageList.add(item)
                    }

                }else {
                    baseViewModel.rentersError.postValue( (it.message))
                }
            }
        })

    }


    private fun setProfileData(response: ProfileResponse) {
        mProfileResponse = response
        mProfileName = mProfileResponse.data?.UserDetails?.firstname
        etFirstName.setText(mProfileResponse.data?.UserDetails?.firstname)
        etLastName.setText(mProfileResponse.data?.UserDetails?.lastname)
        mAboutMe = mProfileResponse.data?.UserDetails?.about_you
        tvAboutMeDesc.tag = null
        tvAboutMeDesc.text = mAboutMe
        if(mAboutMe!!.length > 200 || tvAboutMeDesc.lineCount > 2)
        makeTextViewResizable(tvAboutMeDesc,2,getString(R.string.read_more),true)
        if(!mSessionManager.getUserDetails().profile_image.equals(mProfileResponse.data?.UserDetails?.profile_image!!,true)) {
            setResult(Activity.RESULT_OK)
            mSessionManager.updateProfileImage(mProfileResponse.data?.UserDetails?.profile_image!!)
        }
        loadCircleImageWithGlide(ivProfile,mProfileResponse.data?.UserDetails?.profile_image!!,R.drawable.ic_default_circle_profile_img)
        tvGenderVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        tvGenderVal.text = mProfileResponse.data?.UserDetails?.gender
        mDOB = mProfileResponse.data?.UserDetails?.dob!!
        tvBirthDateVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        tvBirthDateVal.text = mDOB
        tvEmailVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        tvEmailVal.text = mProfileResponse.data?.UserDetails?.email
        mPhoneNumber =  mProfileResponse.data?.UserDetails?.phone_number!!
        mProfileResponse.data?.UserDetails?.country_code?.let {
            mCountryCode = it
        }
        tvPhoneVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        val mMobileCode = mProfileResponse.data?.UserDetails?.mobile_code.toString().replace("\n"," ")
        tvPhoneVal.text = mMobileCode.plus(mPhoneNumber)

        if(mProfileResponse.data?.UserDetails?.language!=null){
            if(mProfileResponse.data?.UserDetails?.language!!.isNotEmpty()) {
                tvLanguagesVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                val result: List<String> = mProfileResponse.data?.UserDetails?.language!!.split(",")
                if (result != null) {
                    for (i in 0 until mLanguageList.size) {
                        for (element in result) {
                            if(element.isNotEmpty()) {
                                if ((mLanguageList[i] as UserLanguage).id.equals(element, true)) {
                                    (mLanguageList[i] as UserLanguage).isSelected = !(mLanguageList[i] as UserLanguage).isSelected
                                    break
                                }
                            }
                        }
                    }
                  //  tvLanguagesVal.text = getSelectedLanguagesText()
                }
            }
        }

        if(mProfileResponse.data?.UserDetails?.timezone!!.isNotEmpty()) {
            tvTimeZoneVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            mTimeZone = mProfileResponse.data?.UserDetails?.timezone!!
            tvTimeZoneVal.text = mTimeZone
        }
        if(mProfileResponse.data?.UserDetails?.address!!.isNotEmpty()) {
            tvLocationVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvLocationVal.text = mProfileResponse.data?.UserDetails?.address
        }

    }


    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.TYPE)){
                val mType = intent.getStringExtra(Iconstants.TYPE)
                if(mType == Iconstants.PUSH_VERIFICATION){
                    val trustAndVerificationIntent = Intent(this, TrustAndVerificationActivity::class.java)
                    startActivity(trustAndVerificationIntent)
                }
            }
        }

        tvTitle.text = getString(R.string.edit_profile)

        mImagePickerList.add(getString(R.string.camera))
        mImagePickerList.add(getString(R.string.gallery))

        mGenderOptions.add(getString(R.string.male))
        mGenderOptions.add(getString(R.string.female))
        mGenderOptions.add(getString(R.string.un_specified))

        imgBtnBack.setOnClickListener(this)
        tvVerifiedInfo.setOnClickListener(this)
        llLocation.setOnClickListener(this)
        llTimeZone.setOnClickListener(this)
        llGender.setOnClickListener(this)
        llLanguages.setOnClickListener(this)
        ivProfile.setOnClickListener(this)

        llBirthDate.setOnClickListener(this)
        tvPhoneVerifiedStatus.setOnClickListener(this)
        btnEditAboutMe.setOnClickListener(this)
        btnEditName.setOnClickListener(this)
        btnEditLastName.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        tvReviewsByYou.setOnClickListener(this)
        tvReviewsAboutYou.setOnClickListener(this)

        val name = dateToString(Date(), "yyyy-MM-dd-hh-mm-ss")


        destination = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$name.jpg")
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToString(date: Date, format: String): String {
        val df = SimpleDateFormat(format)
        return df.format(date)
    }


    override fun onClick(v: View?) {
        etFirstName.clearFocus()
        etLastName.clearFocus()
        when(v!!.id){
            R.id.imgBtnBack -> finish()
            R.id.tvVerifiedInfo ->{
                val trustAndVerificationIntent = Intent(this, TrustAndVerificationActivity::class.java)
                startActivity(trustAndVerificationIntent)
            }
            R.id.llLocation ->{
                val searchIntent = Intent(this, SearchActivity::class.java)
                searchIntent.putExtra(Iconstants.LOCATION,tvLocationVal.text.toString())
                searchIntent.putExtra(Iconstants.HINT,getString(R.string.search_location))
                startActivityForResult(searchIntent,Iconstants.SEARCH_LOCATION_REQUEST_CODE)
            }

            R.id.llTimeZone ->{
                val timeZoneIntent = Intent(this, TimeZoneActivity::class.java)
                timeZoneIntent.putExtra(Iconstants.TIME_ZONE,mTimeZone)
                startActivityForResult(timeZoneIntent,Iconstants.TIME_ZONE_REQUEST_CODE)

            }
            R.id.llGender -> {
                Util.showBottomDialog(this, mGenderOptions, mBottomDialogListener,Iconstants.GENDER,false)
            }
            R.id.llLanguages ->{
                Util.showBottomDialog(this, mLanguageList, mBottomDialogListener,Iconstants.LANGUAGE,true)

            }
            R.id.tvPhoneVerifiedStatus ->{
                val phoneIntent = Intent(this, ConfirmPhoneNumberActivity::class.java)
                phoneIntent.putExtra(Iconstants.PHONE_NUMBER,mPhoneNumber)
                phoneIntent.putExtra(Iconstants.COUNTRY_CODE,mCountryCode)
                startActivityForResult(phoneIntent,Iconstants.CONFIRM_PHONE_REQUEST_CODE)
            }
            R.id.ivProfile ->{
                Util.showBottomDialog(this,mImagePickerList, mBottomDialogListener,Iconstants.PROFILE_IMAGE,false)
            }
            R.id.btnEditAboutMe ->{

                val aboutMeIntent = Intent(this, AboutMeActivity::class.java)
                aboutMeIntent.putExtra(Iconstants.ABOUT_ME,mAboutMe)
                startActivityForResult(aboutMeIntent,Iconstants.ABOUT_ME_PROFILE_REQUEST_CODE)
            }
            R.id.llBirthDate ->{
                val c = Calendar.getInstance()
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val year = c.get(Calendar.YEAR)
                showCalendarDialog(month, day,year-18)
            }
            R.id.btnEditName ->{

                showKeyboard()
                etFirstName.requestFocus()
                etFirstName.setSelection(etFirstName.text.toString().length)

            }
            R.id.btnEditLastName->{
                showKeyboard()
                etLastName.requestFocus()
                etLastName.setSelection(etLastName.text.toString().length)

            }
            R.id.btnSave ->{

                profileViewModel.callUpdateProfile(etFirstName.text.toString(),etLastName.text.toString(),tvGenderVal.text.toString(),mDOB,mAboutMe!!,tvLocationVal.text.toString(),mTimeZone,getSelectedLanguages()).observe(this, Observer<ProfileResponse> {
                    baseViewModel.rentersLoader.postValue(false)
                    if(it.status == "1"){
                            mSessionManager.updateUserDetails(it.data?.UserDetails!!)
                            setResult(Activity.RESULT_OK)
                            setProfileData(it)
                            finish()
                        }
                            baseViewModel.rentersError.postValue(it.message)
                })

            }
            R.id.tvReviewsByYou ->{
                val reviewsIntent = Intent(this, EditProfileReviewsActivity::class.java)
                reviewsIntent.putExtra(Iconstants.IS_FROM,getString(R.string.reviews_by_you))
                startActivity(reviewsIntent)

            }
            R.id.tvReviewsAboutYou ->{
                val reviewsIntent = Intent(this, EditProfileReviewsActivity::class.java)
                reviewsIntent.putExtra(Iconstants.IS_FROM,getString(R.string.reviews_about_you))
                startActivity(reviewsIntent)

            }
        }

    }

    private fun showCalendarDialog(month: Int, day: Int,year:Int) {

        val dialog = DatePickerDialog(this@EditProfileActivity, datePickerListener, (year), month, day)

        val now = Calendar.getInstance()
        now.add(Calendar.YEAR, -17)
        dialog.datePicker.maxDate = now.timeInMillis

        dialog.show()
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        mYear = (selectedYear).toString()
        if ((selectedMonth + 1).toString().length == 1)
            mMonth = "0" + (selectedMonth + 1).toString()
        else
            mMonth =(selectedMonth + 1).toString()
        mDay = selectedDay.toString()

        mDOB = ("$mYear-").plus("$mMonth-").plus(mDay)
      //  if (selectedYear <= 2002) {
            tvBirthDateVal.text = mDOB
      /*  } else {
            baseViewModel.rentersError.postValue(getString(R.string.birth_year_err))
        }*/
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Iconstants.SEARCH_LOCATION_REQUEST_CODE){
                val mLocation = data?.getParcelableExtra<CustomPlaceAddress>(Iconstants.LOCATION)
                tvLocationVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvLocationVal.text = mLocation?.address
            } else if(requestCode == Iconstants.TIME_ZONE_REQUEST_CODE){
                mTimeZone = data?.getStringExtra(Iconstants.TIME_ZONE)!!
                tvTimeZoneVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvTimeZoneVal.text = mTimeZone
            }
            if (requestCode == mGallery) {
                if (data != null) {
                    val contentURI = data.data
                    try {

                         val file = getImageFile()
                         val destinationUri = Uri.fromFile(file)

                        openCropActivity(contentURI!!,destinationUri)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

            } else if (requestCode == mCamera) {

                openCropActivity(Uri.parse(currentPhotoPath),Uri.parse(currentPhotoPath))

            } else if(requestCode == Iconstants.ABOUT_ME_PROFILE_REQUEST_CODE){
                mAboutMe = data?.getStringExtra(Iconstants.ABOUT_ME)
                val expandText = getString(R.string.read_more)
                tvAboutMeDesc.tag = mAboutMe
                tvAboutMeDesc.text = mAboutMe!!
                if(mAboutMe!!.length > 200) {
                   // makeTextViewResizable(tvAboutMeDesc, 2, getString(R.string.read_more), true)
                    val lineEndIndex = tvAboutMeDesc.layout.getLineEnd(2 - 1)
                    val text = tvAboutMeDesc.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                    tvAboutMeDesc.text = text
                    tvAboutMeDesc.movementMethod = LinkMovementMethod.getInstance()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvAboutMeDesc.setText(
                                addClickablePartTextViewResizable(HtmlCompat.fromHtml(tvAboutMeDesc.text.toString(), Html.FROM_HTML_MODE_LEGACY), tvAboutMeDesc, 2, expandText,
                                        true), TextView.BufferType.SPANNABLE)
                    }else {
                        tvAboutMeDesc.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tvAboutMeDesc.text.toString()), tvAboutMeDesc, 2, expandText,
                                        true), TextView.BufferType.SPANNABLE)
                    }

                }
            } else if (requestCode == UCrop.REQUEST_CROP){
                val uri = UCrop.getOutput(data!!)
                val finalFile = File(Util.getRealPathFromURI(this,uri!!))
                loadCircleImageWithGlide(ivProfile,uri.toString(),R.drawable.ic_default_circle_profile_img)
                callUpdateProfileImage(finalFile)

            } else if(requestCode == Iconstants.CONFIRM_PHONE_REQUEST_CODE){
                mPhoneNumber = mSessionManager.getPhoneNumber()
                mCountryCode = mSessionManager.getCountryCode()

                tvPhoneVal.text =("+").plus(mSessionManager.getMobileCode()).plus(mSessionManager.getPhoneNumber())

            }
        }
    }

    private fun callUpdateProfileImage(file: File) {
        profileViewModel.callUpdateProfileImage(file).observe(this, Observer<ProfileImageResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it != null){
                if (it.status == "1") {
                    setResult(Activity.RESULT_OK)
                    mSessionManager.updateProfileImage(it.data.profile_image)
                   // loadCircleImageWithGlide(ivProfile,it.data.profile_image,R.drawable.ic_default_circle_profile_img)
                }
            baseViewModel.rentersError.postValue( it.message)
        }

        })

    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        when(isFrom) {
            Iconstants.GENDER -> {
                tvGenderVal.text = listItem as String
            }
            Iconstants.LANGUAGE -> {
                if((listItem as String).isNotEmpty()) {
                    val result: List<String> = (listItem).split(",")
                    for (i in 0 until mLanguageList.size) {
                        (mLanguageList[i] as UserLanguage).isSelected = false
                        for (element in result) {
                            if(element.isNotEmpty()) {
                                if ((mLanguageList[i] as UserLanguage).id.equals(element, true)) {
                                    (mLanguageList[i] as UserLanguage).isSelected = true
                                    break
                                }
                            }
                        }
                    }
                   // tvLanguagesVal.text = getSelectedLanguagesText()
                }
            }
            Iconstants.PROFILE_IMAGE -> when (position) {

                0 -> {
                    takePhotoFromCamera()
                }
                1 -> {
                    choosePhotoFromGallery()
                }

            }

        }
    }

    private fun getSelectedLanguages():String{
        mSelecteLanguages = ""
        for(i in 0 until mLanguageList.size){

            if((mLanguageList[i] as UserLanguage).isSelected) {
                if(i == mLanguageList.size-1)
                    mSelecteLanguages = mSelecteLanguages.plus((mLanguageList[i] as UserLanguage).id)
                else
                mSelecteLanguages = mSelecteLanguages.plus((mLanguageList[i] as UserLanguage).id).plus(",")
            }
        }
        return  mSelecteLanguages!!
    }
    private fun getSelectedLanguagesText():String{
        mSelectedLanguagesText = ""
        for(i in 0 until mLanguageList.size){
            if((mLanguageList[i] as UserLanguage).isSelected)
                mSelectedLanguagesText = mSelectedLanguagesText.plus((mLanguageList[i] as UserLanguage).name).plus(",")
        }
        if(mSelectedLanguagesText!!.isNotEmpty())
            mSelectedLanguagesText!!.substring(0, mSelectedLanguagesText!!.length-1)
        return  mSelecteLanguages!!
    }

    fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {

        if (tv.tag == null) {
            tv.tag = tv.text
        }

        tv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {

                val obs = tv.viewTreeObserver

                obs.removeOnGlobalLayoutListener(this)

                if (maxLine == 0) {
                    val lineEndIndex = tv.layout.getLineEnd(0)
                    val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tv.text = ""
                        tv.setText(
                                addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    } else {
                        tv.text = ""
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    }


                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tv.setText(
                                addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    }else {
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    }
                } else {
                    val lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    val text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tv.setText(
                                addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    } else {
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE)
                    }

                }
            }
        })

    }

    private fun addClickablePartTextViewResizable(strSpanned: Spanned, tv: TextView,
                                                  maxLine: Int, spanableText: String, viewMore: Boolean): SpannableStringBuilder {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)

        if (str.contains(spanableText)) {


            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.text = ""
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "Read Less", false)
                    } else {
                        tv.layoutParams = tv.layoutParams
                        tv.text = ""
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 2, "Read More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)

        }
        return ssb

    }
}
