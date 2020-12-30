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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R
import com.rent.renters.app.data.model.PropertiesImagesData
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_space_photo.*

import java.io.File
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class SpacePhotoFragment : Fragment(), View.OnClickListener,CustomRecyclerViewAdapter.CustomItemClickListener , SpaceGalleryPhotosAdapter.SpaceGalleryImageClickListener,BottomDialogButtonInterface {


    private lateinit var listingViewModel: ListingViewModel
    private val mImagePickerList = ArrayList<Any>()
    private var mSpaceImageList : ArrayList<PropertiesImagesData> = ArrayList()
    private var mSpacePhotoAdapter: SpaceGalleryPhotosAdapter? = null
    private var mPropId =""
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    lateinit var mListener : BottomDialogButtonInterface
    private var mDeletePropertyItem: PropertiesImagesData ?=null
    private var mDeleteItemPosition = 0
    var mRoot: View? = null
    private lateinit var mPhotoListener : SpaceGalleryPhotosAdapter.SpaceGalleryImageClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            mRoot =  inflater.inflate(R.layout.fragment_space_photo, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this
        mPhotoListener = this
        mListener = this
        initView()
        initViewModel()
    }

    fun initView(){
        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.ID))
                mPropId = arguments?.getString(Iconstants.ID)!!
            if(arguments!!.containsKey(Iconstants.SPACE_GALLERY_IMAGE))
                mSpaceImageList = arguments?.getParcelableArrayList<PropertiesImagesData>(Iconstants.SPACE_GALLERY_IMAGE)!!
            if(mSpaceImageList.size > 0){
                tvNoData.text = getString(R.string.uploaded_images)
                setSpacePhotoAdapter()
            }
        }
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvSpacePhotos.layoutManager = layoutManager
        mImagePickerList.clear()
        mImagePickerList.add(getString(R.string.camera))
        mImagePickerList.add(getString(R.string.gallery))

        ivUploadImg.setOnClickListener(this)
    }

    private fun setSpacePhotoAdapter() {
        if(mSpaceImageList.size == 0){
            tvNoData.text = getString(R.string.no_img_available)
        }else{
            tvNoData.text = getString(R.string.uploaded_images)
        }
        if (mSpacePhotoAdapter == null) {
            mSpacePhotoAdapter = SpaceGalleryPhotosAdapter(context!!, mSpaceImageList, mPhotoListener)
            rvSpacePhotos.adapter = mSpacePhotoAdapter
        } else {
            mSpacePhotoAdapter!!.notifyDataSetChanged()
        }
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
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
                val uri = UCrop.getOutput(data!!)
                val finalFile = File(Util.getRealPathFromURI(context!!,uri!!))
               // (context as BaseActivity).loadImageWithGlide(iv,uri.toString(),R.drawable.ic_card_front_text)
                callUploadPropertyGalleryImage(finalFile)

            }
        }
    }

    private fun callUploadPropertyGalleryImage(finalFile: File) {
        listingViewModel.callUploadPropertyGalleryImage(mPropId,finalFile).observe(this, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
            it.data?.let{
            mSpaceImageList.add(0,PropertiesImagesData(it?.id!!,it.url))
            setSpacePhotoAdapter()
                }

        })

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivUploadImg ->
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
    override fun spaceGalleryImageClick(item: PropertiesImagesData,position: Int) {
        mDeletePropertyItem = item
        mDeleteItemPosition = position
        Util.showBottomSheetDialogWithButtons((context as Activity),getString(R.string.app_name),getString(R.string.delete_alert),mListener,true)


    }

    override fun onBottomCookieItemClick() {
        mDeletePropertyItem?.let {
            listingViewModel.callDeletePropertyGalleryImage(mPropId, it.id!!).observe(this, Observer {
                (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
                if (it.status == "1") {
                    mSpaceImageList.removeAt(mDeleteItemPosition)
                    setSpacePhotoAdapter()
                }

            })
        }

    }

}
