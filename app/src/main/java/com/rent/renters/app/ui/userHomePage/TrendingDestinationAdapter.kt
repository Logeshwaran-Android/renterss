package com.rent.renters.app.ui.userHomePage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.TrendingDestinationData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants


class TrendingDestinationAdapter(internal var mContext: Context, private val mSpaceFitList: ArrayList<TrendingDestinationData>) : RecyclerView.Adapter<TrendingDestinationAdapter.ViewHolder>() {


    private lateinit var mSessionManager: SessionManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_trending_destination_item, parent, false)
        mSessionManager = SessionManager(mContext)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSpaceDesc.text = mSpaceFitList[position].description

        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceItem,mSpaceFitList[position].image, R.drawable.ic_empty_space)

        holder.tvCity.text = mSpaceFitList[position].city_name


        holder.parentLayout.setOnClickListener{
            val spacetypeIntent = Intent(mContext, SearchSpaceActivity::class.java)
            spacetypeIntent.putExtra(Iconstants.LOCATION,  mSpaceFitList[position].city_name)
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

        internal var tvCity: TextView = itemView.findViewById(R.id.tvCity)
        internal var tvSpaceDesc: TextView = itemView.findViewById(R.id.tvSpaceDesc)
        internal var ivSpaceItem: ImageView = itemView.findViewById(R.id.ivSpaceItem)
        internal var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)

    }


}
