package com.rent.renters.app.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R

import java.util.ArrayList

class CountryCodeAdapter(private val mContext: Context, private val mCountryList: ArrayList<String>) : RecyclerView.Adapter<CountryCodeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_country_code_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCountryName.text = mCountryList[position]

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mCountryList.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)

    }


}