package com.rent.renters.app.ui.amenities


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterData
import com.rent.renters.app.ui.base.BaseActivity
import java.util.*

class AmenitiesImageAdapter(internal var mContext: Context, private val mAmenitiesList: ArrayList<MoreFilterData>) : RecyclerView.Adapter<AmenitiesImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_amenities_image_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        (mContext as BaseActivity).loadImageWithGlide(holder.ivAmenities,mAmenitiesList[position].image,R.drawable.ic_empty_amenities)

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return if(mAmenitiesList.size > 5)
            5
        else
            mAmenitiesList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var ivAmenities: ImageView = itemView.findViewById(R.id.ivAmenities)


    }


}
