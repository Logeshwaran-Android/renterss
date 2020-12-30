package com.rent.renters.app.ui.amenities


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterList

import java.util.ArrayList

class AmenitiesListAdapter(private val mContext: Context, private val mAmenitiesList: ArrayList<MoreFilterList>) : RecyclerView.Adapter<AmenitiesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_amenities_list, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvFilterTitle.text = mAmenitiesList[position].name

        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        (holder.rvFilterList).layoutManager = layoutManager
        val mAdapter = AmenitiesAdapter(mContext, mAmenitiesList[position].values)
        holder.rvFilterList.adapter = mAdapter

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mAmenitiesList.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvFilterTitle: TextView = itemView.findViewById(R.id.tvFilterTitle)
        var rvFilterList: RecyclerView = itemView.findViewById(R.id.rvFilterList)

    }


}
