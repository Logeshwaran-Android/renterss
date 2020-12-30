package com.rent.renters.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.ui.base.Iconstants
import java.util.ArrayList


class CustomRecyclerViewAdapter(private var mListItems: ArrayList<Any>, private var mListener: CustomItemClickListener?, private val mIsFrom: String, private val mIsMultiSelect: Boolean) : RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>() {

    private var mSelectedListValues = ""
    private var mSelectedListItems: ArrayList<String> = ArrayList()

    interface CustomItemClickListener {
        fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String)
    }

    fun filterList(filteredNames: ArrayList<Any>) {
        this.mListItems = filteredNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_bottom_multi_select_list, parent, false)
        return ViewHolder(v, true)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when {
            mIsFrom.equals(Iconstants.LANGUAGE, true) -> {
                holder.tvListName.text = (mListItems[position] as UserLanguage).name
                if ((mListItems[position] as UserLanguage).isSelected) {
                    holder.ckListItem.isChecked = true
                }
                holder.rbListItem.visibility = GONE
            }
            mIsFrom.equals(Iconstants.SPACE_TYPE, true) -> {
                holder.tvListName.text = (mListItems[position] as SpaceTypeData).name
                holder.rbListItem.visibility = GONE
            }
            mIsFrom.equals(Iconstants.PROPERTY_TYPE, true) -> {
                holder.tvListName.text = (mListItems[position] as SubPropertyTypeData).name
                holder.rbListItem.visibility = GONE
            }
            mIsFrom.equals(Iconstants.SPACE_PRICE, true) -> {
                holder.tvListName.text = (mListItems[position] as CurrencyListData).currency_type
                holder.rbListItem.visibility = GONE
            }

            mIsFrom.equals(Iconstants.RESERVATION, true) -> {
                holder.tvListName.text = (mListItems[position] as ReservationFilterData).name
                holder.rbListItem.visibility = VISIBLE
                holder.rbListItem.isChecked = (mListItems[position] as ReservationFilterData).isSelected!!
            }
            mIsFrom.equals(Iconstants.CANCELLATION_POLICY, true) -> {
                holder.tvListName.text = (mListItems[position] as CancellationPolicyItem).name
                holder.rbListItem.visibility = GONE
                holder.rbListItem.isChecked = (mListItems[position] as CancellationPolicyItem).isSelected!!
            }
            else -> {
                holder.tvListName.text = mListItems[position].toString()
                holder.rbListItem.visibility = GONE
            }
        }

        if (mIsMultiSelect) {

            if (mIsFrom.equals(Iconstants.LANGUAGE, true)) {
                if((mListItems[position] as UserLanguage).isSelected)
                    mSelectedListItems.add((mListItems[position] as UserLanguage).id)

            }
            if (position == mListItems.size - 1) {
                holder.btnSave.visibility = VISIBLE
            } else {
                holder.btnSave.visibility = GONE
            }
            holder.ckListItem.visibility = VISIBLE

        } else {
            holder.ckListItem.visibility = GONE
            holder.btnSave.visibility = GONE
        }




        holder.parentLayout.setOnClickListener {
            if (!mIsMultiSelect) {
                if (mIsFrom.equals(Iconstants.SPACE_PRICE, true)) {
                    mListener?.onAdapterItemClick((mListItems[position] as CurrencyListData).currency_type as Any, position, mIsFrom)
                } else if (mIsFrom.equals(Iconstants.SPACE_TYPE, true)) {
                    mListener?.onAdapterItemClick((mListItems[position] as SpaceTypeData) as Any, position, mIsFrom)
                }else if (mIsFrom.equals(Iconstants.PROPERTY_TYPE, true)) {
                    mListener?.onAdapterItemClick((mListItems[position] as SubPropertyTypeData) as Any, position, mIsFrom)
                }else if (mIsFrom.equals(Iconstants.RESERVATION, true)) {
                    (mListItems[position] as ReservationFilterData).isSelected = true
                    holder.rbListItem.isChecked = true
                    mListener?.onAdapterItemClick((mListItems[position] as ReservationFilterData) as Any, position, mIsFrom)
                }else if (mIsFrom.equals(Iconstants.CANCELLATION_POLICY, true)) {
                    mListener?.onAdapterItemClick((mListItems[position] as CancellationPolicyItem) as Any, position, mIsFrom)
                } else {
                    mListener?.onAdapterItemClick(mListItems[position].toString(), position, mIsFrom)
                }
            }
        }
        holder.ckListItem.setOnCheckedChangeListener { _, isChecked ->
            if (mIsFrom.equals(Iconstants.LANGUAGE, true)) {
                if (isChecked) {
                    mSelectedListItems.add((mListItems[position] as UserLanguage).id)
                } else {
                    if (mSelectedListItems.contains((mListItems[position] as UserLanguage).id))
                        mSelectedListItems.remove((mListItems[position] as UserLanguage).id)
                }
            }
        }
        holder.btnSave.setOnClickListener {
            for (i in 0 until mSelectedListItems.size) {
                mSelectedListValues =
                        mSelectedListValues.plus(mSelectedListItems[i]).plus(",")
            }
            mListener?.onAdapterItemClick(mSelectedListValues, position, mIsFrom)
        }

        holder.rbListItem.setOnCheckedChangeListener{ _, isChecked ->
            holder.rbListItem.isChecked = true
            if(mIsFrom.equals(Iconstants.RESERVATION, true)) {
                (mListItems[position] as ReservationFilterData).isSelected = true
                notifyDataSetChanged()
                mListener?.onAdapterItemClick((mListItems[position] as ReservationFilterData) as Any, position, mIsFrom)
            }else if(mIsFrom.equals(Iconstants.CANCELLATION_POLICY, true)){
                (mListItems[position] as CancellationPolicyItem).isSelected = true
                notifyDataSetChanged()
                mListener?.onAdapterItemClick((mListItems[position] as CancellationPolicyItem) as Any, position, mIsFrom)
            }
        }

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mListItems.size
    }


    class ViewHolder internal constructor(itemView: View, b: Boolean) : RecyclerView.ViewHolder(itemView) {
        internal var ckListItem: CheckBox = itemView.findViewById(R.id.ckListItem)
        internal var rbListItem: RadioButton = itemView.findViewById(R.id.rbListItem)
        internal var btnSave: Button = itemView.findViewById(R.id.btnSave)

        internal var tvListName: TextView = itemView.findViewById(R.id.tvListName)
        internal var viewList: View = itemView.findViewById(R.id.viewList)
        internal var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)


    }


}