package com.rent.renters.app.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.rent.renters.BuildConfig
import com.rent.renters.R
import com.rent.renters.app.RentersApplication
import com.rent.renters.app.data.model.BlockedData
import com.rent.renters.app.data.model.CustomPlaceAddress
import com.rent.renters.mylibrary.util.SnackbarWrapper
import com.yalantis.ucrop.UCrop
import mumayank.com.airlocationlibrary.AirLocation
import org.threeten.bp.LocalDate
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseActivity : AppCompatActivity() {

    lateinit var baseViewModelFactory: BaseViewModelFactory
    lateinit var baseViewModel: BaseViewModel
    private var mDialog: Dialog? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var mLocationListener: locationInterface
    private var airLocation: AirLocation? = null
    private var rootView: View? = null
    private lateinit var locationCallBack: LocationCallback

    /**
     * Represents a geographical location.
     */
    private var mLastLocation: Location? = null
    private var mResultAddress: String = ""
    var currentPhotoPath: String = ""
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient


    interface locationInterface {
        fun locationUpdate(customAddress: CustomPlaceAddress)
    }

    companion object {
        private const val TAG = "LocationProvider"
        private const val mCameraPermissionRequestCode = 111
        private const val mGalleryPermissionRequestCode = 112
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        const val mGallery = 1
        const val mCamera = 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation?.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleSignIn()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        baseViewModelFactory = BaseViewModelFactory(BaseViewModel())
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(
                BaseViewModel::class.java)
        baseViewModel.init(this@BaseActivity)

        baseViewModel.rentersLoader().observe(this, Observer<Boolean> {
            if (it)
                showProgress()
            else
                hideProgress()
        })

        baseViewModel.rentersError().observe(this, Observer<String> {
            /* val mySnackBar = Snackbar.make(getRootView(),
                     it, Snackbar.LENGTH_SHORT)
             var view = mySnackBar.view
             val params = view.layoutParams as (FrameLayout.LayoutParams)
             params.gravity = Gravity.TOP
             view.layoutParams = params
             mySnackBar.show()*/

           /* if(!Settings.canDrawOverlays(this){
                        val intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, URI.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1);
             }*/
            val snackbarWrapper = SnackbarWrapper.make(getApplicationContext(),getString(R.string.app_name),
                    it, Snackbar.LENGTH_LONG)
            snackbarWrapper.show()

             /* val snackbar = TSnackbar.make(getRootView(), getString(R.string.app_name), TSnackbar.LENGTH_LONG)
              val snackbarView = snackbar.view
              snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.app_color))
              val textView = snackbarView.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text)
              textView.text = it
              textView.setTextColor(Color.WHITE)
              snackbar.show()*/
        })
    }

    fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    fun callGoogleLogin() {
        mGoogleSignInClient.signOut()
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Iconstants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    public fun getRootView(): View {
        val contentViewGroup = findViewById<ViewGroup>(android.R.id.content)

        if (contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0)

        if (rootView == null)
            rootView = window.decorView.rootView

        return rootView!!
    }


    fun getImageFile(): File {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        )
        val file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        )
        currentPhotoPath = "file:" + file.absolutePath
        return file
    }

    fun spannableBoldString(mFullString: String, mSubString: String): SpannableString {
        val spannable = SpannableString(mFullString)
        spannable.setSpan(StyleSpan(Typeface.BOLD),
                mFullString.indexOf(mSubString),
                mFullString.indexOf(mSubString) + mSubString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannable
    }

    fun spannableColorString(mFullString: String, mSubString: String): SpannableString {
        val spannable = SpannableString(mFullString)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.app_color)),
                mFullString.indexOf(mSubString),
                mFullString.indexOf(mSubString) + mSubString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannable
    }

    fun loadImageWithGlide(imageView: ImageView, url: String?, errorImg: Int) {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 20f

        circularProgressDrawable.start()
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this).load(url).placeholder(circularProgressDrawable).error(errorImg).apply(requestOptions).into(imageView)

    }

    fun loadCircleImageWithGlide(imageView: ImageView, url: String?, errorImg: Int) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this).load(url).placeholder(errorImg).error(errorImg).apply(requestOptions).into(imageView)

    }

    fun loadBitmapImageWithGlide(imageView: ImageView, bitmap: Bitmap) {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this).load(bitmap).placeholder(circularProgressDrawable).apply(requestOptions).into(imageView)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }


    private fun showProgress() {
        if (mDialog == null) {
            mDialog = Dialog(this@BaseActivity)
            // no tile for the dialog
            mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog!!.setContentView(R.layout.dialog_custom_progress)
            mDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog!!.setCancelable(false)
            mDialog!!.setCanceledOnTouchOutside(false)
        }

        if (mDialog != null)
            mDialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    private fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@BaseActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun checkWriteExternalStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkReadExternalStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), mCameraPermissionRequestCode)
    }

    private fun requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), mGalleryPermissionRequestCode)
    }

    fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(5f, 5f)
                .start(this)
    }

    fun takePhotoFromCamera() {
        if (!checkCameraPermission() || !checkWriteExternalStoragePermission()) {
            requestCameraPermission()

        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = getImageFile()// 1
            val uri: Uri
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.plus(".fileprovider"), file)
            else
                Uri.fromFile(file) // 3
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // 4
            startActivityForResult(intent, mCamera)
        }
    }

    fun choosePhotoFromGallery() {
        if (!checkReadExternalStoragePermission()) {
            requestGalleryPermission()

        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, mGallery)
        }
    }


    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            val mySnackBar = Snackbar.make(getRootView(),
                    getString(R.string.enable_permission), Snackbar.LENGTH_INDEFINITE)
            mySnackBar.setAction(getString(R.string.ok)) {
                startLocationPermissionRequest()
            }
            mySnackBar.show()
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }


    fun getCurrentLocation(locationListener: locationInterface) {
        mLocationListener = locationListener
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation(mLocationListener)
        }


    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(mLocationListener: locationInterface) {

        airLocation = AirLocation(this, true, true, object : AirLocation.Callbacks {
            override fun onSuccess(location: Location) {
                // location fetched successfully, proceed with it
                mLastLocation = location
                (applicationContext as RentersApplication).mLastLocation = mLastLocation
                mLocationListener.locationUpdate(getAddressFromLatLong(mLastLocation!!.latitude, mLastLocation!!.longitude))
            }

            override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                if ((applicationContext as RentersApplication).mLastLocation != null) {
                    mLastLocation = (applicationContext as RentersApplication).mLastLocation
                    if (mLastLocation != null)
                        mLocationListener.locationUpdate(getAddressFromLatLong(mLastLocation!!.latitude, mLastLocation!!.longitude))
                    else
                        baseViewModel.rentersError.postValue("No location detected")
                }
            }

        })

    }

    private fun removeLocationUpdate() {
        fusedLocationClient!!.removeLocationUpdates(locationCallBack)
    }


    private fun getLocationName(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
            val obj = addresses[0]
            //val add = obj.getAddressLine(0)
            var add = ""
            if (obj.locality != null)
                add = add.plus(obj.locality)
            if (!obj.locality.equals(obj.subAdminArea, true)) {
                if (obj.subAdminArea != null)
                    add = add.plus(",").plus(obj.subAdminArea)
            }

            if (add.isNotEmpty())
                add = add.plus(",").plus(obj.adminArea).plus(",").plus(obj.countryName)
            else
                add = (obj.adminArea).plus(",").plus(obj.countryName)
            // autoCompleteSearchTxt.setText(mResultAddress)
            return add
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        airLocation?.onRequestPermissionsResult(requestCode, permissions, grantResults) // ADD THIS LINE INSIDE onRequestPermissionResult

        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == mCameraPermissionRequestCode) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> takePhotoFromCamera()
                PackageManager.PERMISSION_DENIED -> baseViewModel.rentersError.postValue(getString(R.string.camera_permission_err))
            }
        } else if (requestCode == mGalleryPermissionRequestCode) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> choosePhotoFromGallery()
                PackageManager.PERMISSION_DENIED -> baseViewModel.rentersError.postValue(getString(R.string.gallery_permission_err))
            }
        } else if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                    getLastLocation(mLocationListener)
                else -> {

                    val mySnackBar = Snackbar.make(getRootView(),
                            getString(R.string.enable_permission), Snackbar.LENGTH_INDEFINITE)
                    mySnackBar.setAction(getString(R.string.settings)) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    mySnackBar.show()
                }
            }
        }
    }

    fun getUnAvailableDates(mBlockedDateList: ArrayList<BlockedData>): ArrayList<LocalDate> {
        val mUnavailableDates: ArrayList<LocalDate> = ArrayList()
        for (i in 0 until mBlockedDateList.size) {
            if (mBlockedDateList[i].status!!.equals("unavailable", true))
                mUnavailableDates.add(LocalDate.parse(mBlockedDateList[i].value!!))
        }
        return mUnavailableDates
    }

    fun getBlockedDate(mBlockedDateList: ArrayList<BlockedData>): ArrayList<LocalDate> {
        val mBlockDates: ArrayList<LocalDate> = ArrayList()
        for (i in 0 until mBlockedDateList.size) {
            if (!mBlockedDateList[i].status!!.equals("unavailable", true))
                mBlockDates.add(LocalDate.parse(mBlockedDateList[i].value!!))
        }
        return mBlockDates
    }

    fun getAddressFromLatLong(latitude: Double, longitude: Double): CustomPlaceAddress {
        val mLatlng = LatLng(latitude, longitude)

        /* var response: MutableLiveData<CustomPlaceAddress> = MutableLiveData()
         if(Util.isNetworkAvailable(this@BaseActivity)) {
             response = baseViewModel.callGetGooglePlaceDetails(mLatlng)


             response?.let {

                 val addresses: List<Address>
                 //val geocoder = Geocoder(this, Locale.getDefault())
                 try {

                     //  addresses = geocoder.getFromLocation(latitude, longitude, 1)

                     *//*   val address = addresses[0].getAddressLine(0)
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName*//*

                    val address = response.value?.address
                    val city = response.value?.city
                    val state = response.value?.state
                    val country = response.value?.country
                    val postalCode = response.value?.postalCode
                    val knownName = response.value?.knowName

                    return CustomPlaceAddress(address, city, state, country, postalCode, knownName, latitude, longitude)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

*/
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1)

            val address = addresses[0].getAddressLine(0)
            val city = if (addresses[0].locality.isNullOrEmpty()) "" else addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName

            return CustomPlaceAddress(address, city, state, country, postalCode, knownName, latitude, longitude)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return CustomPlaceAddress("", "", "", "", "", "", 0.0, 0.0)

    }

    private fun NetwordDetect() {

        var WIFI = false

        var MOBILE = false

        val CM = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = CM.getAllNetworkInfo()

        for (netInfo in networkInfo) {
            if (netInfo.getTypeName().equals("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true

            if (netInfo.getTypeName().equals("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true
        }

        if (WIFI == true) {
            //IPaddress = GetDeviceipWiFiData();


        }

        if (MOBILE == true) {

            //IPaddress = GetDeviceipMobileData();

        }

    }


    fun isValidPhoneNumber(target: CharSequence?): Boolean {
        return if (target == null || TextUtils.isEmpty(target) || target.length <= 6) {
            false
        } else {
            android.util.Patterns.PHONE.matcher(target).matches()
        }
    }

    fun validateUsing_libphonenumber(countryCode: String, phNumber: String): Boolean {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode))
        var phoneNumber: Phonenumber.PhoneNumber? = null
        try {
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode)
        } catch (e: NumberParseException) {
            System.err.println(e)
        }

        val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
        if (isValid) {
            return true
        } else {
            baseViewModel.rentersError.postValue( getString(R.string.phone_invalid) + phoneNumber!!)
            return false
        }
    }
}