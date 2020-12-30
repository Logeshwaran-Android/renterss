package com.rent.renters.app.ui.userHomePage


import android.content.Context

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


import com.rent.renters.R
import com.rent.renters.app.data.model.PropertyTypeData
import com.rent.renters.app.ui.base.BaseActivity


class SpaceTypesAdapter(private val mContext: Context, private val mSpaceTypeList: ArrayList<PropertyTypeData>, private val mListener: SpaceTypeClickListener) : RecyclerView.Adapter<SpaceTypesAdapter.ViewHolder>() {

    interface SpaceTypeClickListener {
        fun spaceTypeClick(item: PropertyTypeData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_space_types, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.parentLayout.setOnClickListener{mListener.spaceTypeClick(mSpaceTypeList[position])}
        holder.tvSpaceType.text = mSpaceTypeList[position].name
        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceType,mSpaceTypeList[position].image,R.drawable.ic_empty_space)


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mSpaceTypeList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)
        var tvSpaceType: TextView = itemView.findViewById(R.id.tvSpaceType)
        var ivSpaceType: ImageView = itemView.findViewById(R.id.ivSpaceType)


    }


}
