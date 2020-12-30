package com.rent.renters.app.ui.amenities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterData
import com.rent.renters.app.ui.base.BaseActivity
import java.util.*


class AmenitiesAdapter(private val mContext: Context, private var mFilterList: ArrayList<MoreFilterData>) : RecyclerView.Adapter<AmenitiesAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_amenities_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAmenitiesName.text = mFilterList[position].listvalues

        (mContext as BaseActivity).loadImageWithGlide(holder.ivAmenities,mFilterList[position].image,android.R.drawable.ic_btn_speak_now)

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mFilterList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvAmenitiesName: TextView = itemView.findViewById(R.id.tvAmenitiesName)
        var ivAmenities: ImageView = itemView.findViewById(R.id.ivAmenities)

    }


}