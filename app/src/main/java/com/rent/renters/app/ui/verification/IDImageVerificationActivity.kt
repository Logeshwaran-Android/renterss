package com.rent.renters.app.ui.verification

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.IDVerificationResponse
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_id_image_verification.*
import kotlinx.android.synthetic.main.header_layout.*
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList

class IDImageVerificationActivity : BaseActivity(), View.OnClickListener, CustomRecyclerViewAdapter.CustomItemClickListener {

    private lateinit var verificationViewModel: VerificationViewModel

    private var mSelectedIDType: String = ""
    private var mSelectedCountryCode: String = ""
    private lateinit var mFrontImageFile: File
    private lateinit var mBackImageFile: File


    private var mFrontImageClicked = false
    private var mBackImageClicked = false

    private val mImagePickerList = ArrayList<Any>()
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_image_verification)
        mBottomDialogListener = this
        initView()
        initViewModel()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.trust_and_verification)
        if (intent != null) {
            if(intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
                if (bundle!!.containsKey(Iconstants.TYPE))
                    mSelectedIDType = bundle.getString(Iconstants.TYPE,"")
                if (bundle.containsKey(Iconstants.COUNTRY_CODE))
                    mSelectedCountryCode = bundle.getString(Iconstants.COUNTRY_CODE,"")
            }
        }

        mImagePickerList.add(getString(R.string.camera))
        mImagePickerList.add(getString(R.string.gallery))
        imgBtnBack.setOnClickListener(this)
        ivFrontImage.setOnClickListener(this)
        ivBackImage.setOnClickListener(this)
        btnProceedToVerify.setOnClickListener(this)
    }
    private fun initViewModel() {
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        verificationViewModel.initMethod(this)

    }






   
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
if(resultCode == Activity.RESULT_OK) {
    if (requestCode == mGallery) {
        if (data != null) {
            val contentURI = data.data
            try {

                val file = getImageFile()
                val destinationUri = Uri.fromFile(file)

                openCropActivity(contentURI!!, destinationUri)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    } else if (requestCode == mCamera) {

        openCropActivity(Uri.parse(currentPhotoPath), Uri.parse(currentPhotoPath))

    }else if (requestCode == UCrop.REQUEST_CROP){
        val uri = UCrop.getOutput(data!!)
        val finalFile = File(Util.getRealPathFromURI(this,uri!!))
        if(mFrontImageClicked) {
            loadImageWithGlide(ivFrontImage,uri.toString(),R.drawable.ic_card_front_text)
            mFrontImageFile = finalFile
        }
        else if(mBackImageClicked) {
            loadImageWithGlide(ivBackImage,uri.toString(),R.drawable.ic_card_back_text)
            mBackImageFile = finalFile
        }


    }
}
    }




    override fun onClick(view: View?) {
        when (view!!.id) {

            R.id.ivFrontImage ->{
                mBackImageClicked = false
                mFrontImageClicked = true
                Util.showBottomDialog(this, mImagePickerList,  mBottomDialogListener,Iconstants.VERIFICATION_IMAGE,false)
            }
            R.id.ivBackImage -> {
                mFrontImageClicked = false
                mBackImageClicked = true
                Util.showBottomDialog(this, mImagePickerList, mBottomDialogListener,Iconstants.VERIFICATION_IMAGE,false)
            }
            R.id.btnProceedToVerify ->{
                validateFields()

            }
            R.id.imgBtnBack->{
                finish()
            }
        }


    }

    private fun validateFields() {
        if(!::mFrontImageFile.isInitialized)
            baseViewModel.rentersError.postValue(getString(R.string.front_img_error))
            else if(!::mBackImageFile.isInitialized)
                baseViewModel.rentersError.postValue(getString(R.string.back_img_error))
        else{
            verificationViewModel.callVerifyID(mSelectedCountryCode,mSelectedIDType,mFrontImageFile,mBackImageFile).observe(this, Observer<IDVerificationResponse> {
                baseViewModel.rentersLoader.postValue(false)
                if(it.status == "1"){
                    val resultIntent = Intent()
                    resultIntent.putExtra(Iconstants.STATUS,it.status)
                    setResult(Activity.RESULT_OK,resultIntent)
                    finish()
                } else{
                    baseViewModel.rentersError.postValue(it.message)
                }

            })
        }


    }

    override fun onAdapterItemClick(listItem: Any, position: Int,isFrom:String) {
        Util.dismissBottomDialog()
        when (position) {
            0 ->
                    takePhotoFromCamera()

            1 ->
                    choosePhotoFromGallery()

            }
    }

}


