package com.rent.renters.app.ui.filter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterList

import java.util.ArrayList

class MoreFilterListAdapter(private val mContext: Context, internal var mFilterList: ArrayList<MoreFilterList>, private val mFilterListener: MoreFilterAdapter.MoreFilterInterface) : RecyclerView.Adapter<MoreFilterListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_filter_list_items, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mFilterList!![position].values.size > 0) {
            holder.tvFilterTitle.text = mFilterList[position].name

            val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

            (holder.rvFilterList).layoutManager = layoutManager
            val mAdapter = MoreFilterAdapter(mContext, mFilterList[position].values, mFilterListener, position)
            holder.rvFilterList.adapter = mAdapter
        } else{
            holder.tvFilterTitle.visibility = View.GONE
            holder.rvFilterList.visibility = View.GONE


        }
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

        var tvFilterTitle: TextView = itemView.findViewById(R.id.tvFilterTitle)
        var rvFilterList: RecyclerView = itemView.findViewById(R.id.rvFilterList)

    }


}