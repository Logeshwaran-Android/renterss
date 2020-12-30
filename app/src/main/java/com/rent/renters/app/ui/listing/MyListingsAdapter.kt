package com.rent.renters.app.ui.listing

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import com.rent.renters.R
import com.rent.renters.app.data.model.ListingItem
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants


class MyListingsAdapter(internal var mContext: Context, private val mListings: ArrayList<ListingItem>, private val mListener: MyListingClickListener) : RecyclerView.Adapter<MyListingsAdapter.ViewHolder>() {
  //  private var mIsBinding = true

    interface MyListingClickListener {
        fun myListingClick(item: ListingItem, isFrom: String,position:Int)
        fun activeInActiveClick(item: ListingItem, status: String,position:Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_listing_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (mListings[position].banner_photos != null)
            (mContext as BaseActivity).loadCircleImageWithGlide(holder.ivListImg, mListings[position].banner_photos, R.drawable.ic_empty_space)

        holder.tvListName.text = mListings[position].product_name

        if (!mListings[position].pending_steps.equals("0")) {
            holder.btnStatus.visibility = View.VISIBLE
            holder.switchActive.visibility = View.GONE
            holder.tvManageCalendar.visibility = View.GONE
            holder.btnStatus.tag = mContext.getString(R.string.steps)
            holder.btnStatus.text = mListings[position].pending_steps.plus(" ").plus(mContext.getString(R.string.steps))
            holder.btnStatus.background = mContext.getDrawable(R.drawable.rectangle_app_color_border_button)
            holder.btnStatus.setTextColor(getColor(mContext,R.color.text_app_color))
        } else {
            if(mListings[position].status.equals("Active",true) || mListings[position].status.equals("Inactive",true)){
                holder.tvManageCalendar.visibility = View.VISIBLE
                holder.btnStatus.visibility = View.GONE
                holder.switchActive.visibility = View.VISIBLE
                holder.switchActive.isChecked = mListings[position].host_status.equals("Active",true)
            } else {
                holder.tvManageCalendar.visibility = View.INVISIBLE
                holder.btnStatus.visibility = View.VISIBLE
                holder.btnStatus.background = mContext.getDrawable(R.drawable.rectangle_curve_button)
                holder.btnStatus.setTextColor(getColor(mContext,R.color.text_white))
                holder.switchActive.visibility = View.GONE
                holder.btnStatus.tag = mListings[position].status
                holder.btnStatus.text = mListings[position].status
            }
        }


        holder.tvManageListing.setOnClickListener {
            mListener.myListingClick(mListings[position], Iconstants.EDIT_LISTING,position)
        }
        holder.tvListName.setOnClickListener {
            mListener.myListingClick(mListings[position], Iconstants.EDIT_LISTING,position)
        }
        holder.tvManageCalendar.setOnClickListener {
            mListener.myListingClick(mListings[position], Iconstants.MANAGE_CALENDAR,position)
        }
        holder.btnStatus.setOnClickListener {
            if (holder.btnStatus.tag.toString().equals(mContext.getString(R.string.publish), true)) {
                mListener.myListingClick(mListings[position], Iconstants.PUBLISH_LISTING,position)
            }else  if(!holder.btnStatus.tag.toString().equals(mContext.getString(R.string.pending), true)){
                mListener.myListingClick(mListings[position], Iconstants.EDIT_LISTING,position)
            }
        }
        holder.switchActive.setOnCheckedChangeListener { button, isChecked ->
            /*if(!mIsBinding) {
                mIsBinding = true*/
            if(button.isPressed) {
                if (isChecked)
                    mListener.activeInActiveClick(mListings[position], Iconstants.ACTIVE, position)
                else
                    mListener.activeInActiveClick(mListings[position], Iconstants.INACTIVE, position)
            }
          //  }
        }
      /*  if(position == (mListings.size-1))
            mIsBinding = false
*/

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mListings.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var rlParentLayout: RelativeLayout = itemView.findViewById(R.id.parentLayout)
        internal var ivListImg: ImageView = itemView.findViewById(R.id.ivListImg)
        internal var tvListName: TextView = itemView.findViewById(R.id.tvListName)
        internal var btnStatus: Button = itemView.findViewById(R.id.btnStatus)
        internal var tvManageListing: TextView = itemView.findViewById(R.id.tvManageListing)
        internal var tvManageCalendar: TextView = itemView.findViewById(R.id.tvManageCalendar)
        internal var switchActive: Switch = itemView.findViewById(R.id.switchActive)

    }


}

