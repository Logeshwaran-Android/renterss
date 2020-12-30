package com.rent.renters.app.ui.userHomePage


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import com.rent.renters.R
import com.rent.renters.app.data.model.FeatureSpaceData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants


class SpaceItemAdapter(internal var mContext: Context,private val mSpaceFitList: ArrayList<FeatureSpaceData>) : RecyclerView.Adapter<SpaceItemAdapter.ViewHolder>() {


    private lateinit var mSessionManager: SessionManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_space_items, parent, false)
        mSessionManager = SessionManager(mContext)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSpaceItem.text = mSpaceFitList[position].city
        holder.tvSpacePrice.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mSpaceFitList[position].average_price).plus("/").plus(mContext.getString(R.string.night_average))
        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceItem,mSpaceFitList[position].image,R.drawable.ic_empty_space)

      /*  val displayMetrics = DisplayMetrics()
        (mContext as BaseActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val params = ConstraintLayout.LayoutParams(
                150, width)
        holder.ivSpaceItem.layoutParams = params*/
        holder.parentLayout.setOnClickListener{
            val spacetypeIntent = Intent(mContext, SearchSpaceActivity::class.java)
            spacetypeIntent.putExtra(Iconstants.LOCATION,  mSpaceFitList[position].city)
            mContext.startActivity(spacetypeIntent)
        }



    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mSpaceFitList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tvSpaceItem: TextView = itemView.findViewById(R.id.tvSpaceItem)
        internal var tvSpacePrice: TextView = itemView.findViewById(R.id.tvSpacePrice)
        internal var ivSpaceItem: ImageView = itemView.findViewById(R.id.ivSpaceItem)
        internal var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)

    }


}
