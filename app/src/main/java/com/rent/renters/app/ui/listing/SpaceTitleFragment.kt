package com.rent.renters.app.ui.listing


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_space_title.*
import java.io.File
import java.io.IOException



/**
 * A simple [Fragment] subclass.
 */
class SpaceTitleFragment : Fragment(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener {

    private val mImagePickerList = ArrayList<Any>()
    private lateinit var listingViewModel: ListingViewModel

    private var mPropId =""
    private var uri : Uri ?=null
    private var mSpaceBannerImg = ""
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_space_title, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this

        initViewModel()
        initView()

    }

    private fun initView() {
        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.ID))
                mPropId = arguments?.getString(Iconstants.ID)!!
            if(arguments!!.containsKey(Iconstants.SPACE_TITLE))
                etSpaceNameVal.setText(arguments?.getString(Iconstants.SPACE_TITLE))
            if(arguments!!.containsKey(Iconstants.SPACE_BANNER_IMAGE))
                (context as BaseActivity).loadImageWithGlide(ivSpaceBannerPhoto,arguments?.getString(Iconstants.SPACE_BANNER_IMAGE),R.drawable.ic_default_img_upload)
        }
        mImagePickerList.clear()
        mImagePickerList.add(getString(R.string.camera))
        mImagePickerList.add(getString(R.string.gallery))

        ivSpaceBannerPhoto.setOnClickListener(this)
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
    }

    fun validateFields() : Boolean{
        return when {
            etSpaceNameVal.text.toString().isEmpty() -> {
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_name_err))
                false
            }
            else -> true
        }
    }

    fun getSpaceName() : String{
        return etSpaceNameVal.text.toString()
    }

    fun getBannerImage() : String{
        return mSpaceBannerImg
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivSpaceBannerPhoto ->
                Util.showBottomDialog(context!!,mImagePickerList, mBottomDialogListener, Iconstants.SPACE_BANNER_IMAGE,false)
        }

    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        when (position) {

            0 -> {
                (context as BaseActivity).takePhotoFromCamera()
        }
            1 -> {
                (context as BaseActivity).choosePhotoFromGallery()
        }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      //  super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == BaseActivity.mGallery) {
                if (data != null) {
                    val contentURI = data.data
                    try {

                        val file = (context as BaseActivity).getImageFile()
                        val destinationUri = Uri.fromFile(file)

                        (context as BaseActivity).openCropActivity(contentURI!!,destinationUri)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

            } else if (requestCode == BaseActivity.mCamera) {

                (context as BaseActivity).openCropActivity(Uri.parse((context as BaseActivity).currentPhotoPath), Uri.parse((context as BaseActivity).currentPhotoPath))

            } else if (requestCode == UCrop.REQUEST_CROP){
                 uri = UCrop.getOutput(data!!)
                val finalFile = File(Util.getRealPathFromURI(context!!,uri!!))
                mSpaceBannerImg = uri.toString()
                (context as BaseActivity).loadImageWithGlide(ivSpaceBannerPhoto,uri.toString(),R.drawable.ic_card_front_text)
                callUploadPropertyBannerImage(finalFile)

            }
        }
        }

    private fun callUploadPropertyBannerImage(finalFile: File) {
        listingViewModel.callUploadPropertyBannerImage(mPropId,finalFile).observe(this, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)

        })

    }

}// Required empty public constructor
