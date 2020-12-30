package com.rent.renters.app.ui.filter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

import android.widget.TextView

import com.rent.renters.R
import com.rent.renters.app.data.model.MoreFilterData

import java.util.ArrayList

class MoreFilterAdapter(private val mContext: Context, internal var mFilterList: ArrayList<MoreFilterData>, private var mListener: MoreFilterInterface, private val mListPposition: Int) : RecyclerView.Adapter<MoreFilterAdapter.ViewHolder>() {

    interface MoreFilterInterface{
        fun onMoreFilterItemClick(item : MoreFilterData, listPosition: Int, isSelected : Boolean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_filter_items, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFilterItem.text = mFilterList[position].listvalues

        holder.ckFilterItem.isChecked = mFilterList[position].isSelected
        holder.ckFilterItem.setOnCheckedChangeListener { _, isChecked ->
            mListener.onMoreFilterItemClick(mFilterList[position],mListPposition, isChecked)
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

        var tvFilterItem: TextView = itemView.findViewById(R.id.tvFilterItem)
        var ckFilterItem: CheckBox = itemView.findViewById(R.id.ckFilterItem)

    }


}