package com.rent.renters.app.ui.userHomePage


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager

import com.rent.renters.R
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.adapter.ImagePagerAdapter
import com.rent.renters.mylibrary.util.CircleIndicator
import kotlin.collections.ArrayList

class SpaceTypeItemAdapter(private val mContext: Context, private val mSearchSpaceList: ArrayList<SearchSpaceData>, private val mSpaceTypeListener: SpaceTypeItemClickListener) : RecyclerView.Adapter<SpaceTypeItemAdapter.ViewHolder>() {

    private var currentPage = 1
    private lateinit var mSessionManager : SessionManager

    interface SpaceTypeItemClickListener {
        fun spaceTypeItemClick(item :SearchSpaceData,position: Int)
        fun addRemoveWishList(item : SearchSpaceData,position: Int)

    }

    fun addData(listItems: ArrayList<SearchSpaceData>) {
        val size = mSearchSpaceList.size
        this.mSearchSpaceList.addAll(listItems)
        val sizeNew = this.mSearchSpaceList.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_space_type_items, parent, false)
            mSessionManager = SessionManager(mContext)
            return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(mSearchSpaceList[position].galleryimg!=null) {
            holder.viewPager.adapter = ImagePagerAdapter(mContext, mSearchSpaceList[position].galleryimg!!,mSearchSpaceList[position],mSpaceTypeListener,position)
            holder.viewPager.offscreenPageLimit = mSearchSpaceList[position].galleryimg!!.size
            holder.indicator.setViewPager(holder.viewPager)
        }



        holder.tvSpaceCity.text = mSearchSpaceList[position].address?.city
        holder.tvSpaceTypeItemName.text = mSearchSpaceList[position].product_name
        holder.tvSpacePrice.text = mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[position].base_price).plus(" ").plus(mContext.getString(R.string.per_day))
        holder.tvSpaceSize.text = mContext.getString(R.string.entire_space).plus("-").plus(mSearchSpaceList[position].property_size).plus(" ").plus(mContext.getString(R.string.sq_feet))

        holder.parentLayout.setOnClickListener { mSpaceTypeListener.spaceTypeItemClick(mSearchSpaceList[position],position) }

        if(mSearchSpaceList[position].is_favorite)
            holder.imgBtnFavorite.setImageResource(R.drawable.ic_favorite_red)
        else
            holder.imgBtnFavorite.setImageResource(R.drawable.ic_favorite)

        holder.imgBtnFavorite.setOnClickListener{
            mSpaceTypeListener.addRemoveWishList(mSearchSpaceList[position],position)

        }

        if(mSearchSpaceList[position].instant_booking!=null) {
            if(mSearchSpaceList[position].instant_booking.equals("yes",true))
            holder.imgBtnInstant.visibility = View.VISIBLE
            else
            holder.imgBtnInstant.visibility = View.GONE
        }





    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mSearchSpaceList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var viewPager: ViewPager = itemView.findViewById(R.id.viewPager)
        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)
        var tvSpaceSize: TextView = itemView.findViewById(R.id.tvSpaceSize)
        var tvSpaceTypeItemName: TextView = itemView.findViewById(R.id.tvSpaceTypeItemName)
        var tvSpacePrice: TextView = itemView.findViewById(R.id.tvSpacePrice)
        var tvSpaceCity: TextView = itemView.findViewById(R.id.tvSpaceCity)
        var imgBtnFavorite:ImageButton = itemView.findViewById(R.id.imgBtnFavorite)
        var imgBtnInstant:ImageButton = itemView.findViewById(R.id.imgBtnInstant)

        val indicator: CircleIndicator  = itemView.findViewById(R.id.indicator)


    }

}
