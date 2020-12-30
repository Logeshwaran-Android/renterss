package com.rent.renters.app.ui.searchLocation


import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import android.widget.TextView
import androidx.annotation.NonNull

import com.rent.renters.R

import java.util.ArrayList
import com.rent.renters.app.data.model.CustomPlaceSearchModal


class CustomPlaceSearchAdapter(internal var mContext: Context, private var mAddressList: ArrayList<CustomPlaceSearchModal>, private var listener: CustomOnClickListener) : RecyclerView.Adapter<CustomPlaceSearchAdapter.ViewHolder1>() {


    interface CustomOnClickListener {
        fun onItemClickListener(customPlaceSearchModal: CustomPlaceSearchModal)
    }

    @SuppressLint("InflateParams")
    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder1 {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_place_search_items, null)
        return ViewHolder1(view)
    }

    override fun onBindViewHolder(@NonNull viewHolder1: ViewHolder1, i: Int) {

        viewHolder1.Tv_address.text = mAddressList[i].address
        viewHolder1.placeSearchLayout.setOnClickListener { listener.onItemClickListener(mAddressList[i]) }
    }

    override fun getItemCount(): Int {
        return mAddressList.size
    }

    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var Tv_address: TextView = itemView.findViewById(R.id.tvPlaceName)
        var placeSearchLayout: RelativeLayout = itemView.findViewById(R.id.placeSearchLayout)

    }

}