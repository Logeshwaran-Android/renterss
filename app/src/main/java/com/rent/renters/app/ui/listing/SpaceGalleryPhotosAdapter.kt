package com.rent.renters.app.ui.listing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.PropertiesImagesData
import com.rent.renters.app.ui.base.BaseActivity


class SpaceGalleryPhotosAdapter(private val mContext: Context, private val mImageList: ArrayList<PropertiesImagesData>, private val mListener: SpaceGalleryImageClickListener) : RecyclerView.Adapter<SpaceGalleryPhotosAdapter.ViewHolder>() {

    interface SpaceGalleryImageClickListener {
        fun spaceGalleryImageClick(item: PropertiesImagesData,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gallery_image_list, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceGalleryImage,mImageList[position].url, R.drawable.ic_empty_space)

        holder.imgBtnDeleteGalleryImage.setOnClickListener{mListener.spaceGalleryImageClick(mImageList[position],position)}


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mImageList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgBtnDeleteGalleryImage: ImageButton = itemView.findViewById(R.id.imgBtnDeleteGalleryImage)
        var ivSpaceGalleryImage: ImageView = itemView.findViewById(R.id.ivSpaceGalleryImage)


    }


}
