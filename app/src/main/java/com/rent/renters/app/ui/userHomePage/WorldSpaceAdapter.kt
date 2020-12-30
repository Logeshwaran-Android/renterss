package com.rent.renters.app.ui.userHomePage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.TrendingSpaceData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import java.text.DecimalFormat


class WorldSpaceAdapter(private val mContext: Context, private var mListener: SpaceTypeItemClickListener, private val mSearchSpaceList: ArrayList<TrendingSpaceData>) : RecyclerView.Adapter<WorldSpaceAdapter.ViewHolder>() {
    private lateinit var mSessionManager : SessionManager

    interface SpaceTypeItemClickListener {
        fun spaceTypeItemClick(item: TrendingSpaceData)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_trending_space_item, parent, false)
        mSessionManager = SessionManager(mContext)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // holder.ivSpaceItem.setOnClickListener { mListener.spaceTypeItemClick() }

// I'm storing the size of the window in the display var, so I can then play around

        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceItem,mSearchSpaceList[position].banner_photos!!, R.drawable.ic_empty_space)

        mSearchSpaceList[position].address?.let {
            it.city?.let {
                holder.tvSpace.text = mContext.getString(R.string.entire_space).plus(" - ").plus(it)
            }
        }
        holder.tvSpaceTypeItemName.text = mSearchSpaceList[position].name
        holder.tvSpacePrice.text = mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[position].price).plus(" ").plus(mContext.getString(R.string.per_day))

        mSearchSpaceList[position].rating?.let {
            val f = DecimalFormat("#.#")
            holder.tvRatingStar.text =(f.format(it.toDouble())).toString()
            holder.ratingBarReviews.rating = it.toFloat()
        }
        mSearchSpaceList[position].reviews_count?.let {
            holder.tvRatingCount.text = it
        }

        holder.parentLayout.setOnClickListener{
            mListener.spaceTypeItemClick(mSearchSpaceList[position])
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


        var ivSpaceItem: ImageView = itemView.findViewById(R.id.ivSpaceTypeItem)
        var tvSpace: TextView = itemView.findViewById(R.id.tvSpace)
        var tvSpaceTypeItemName: TextView = itemView.findViewById(R.id.tvSpaceTypeItemName)
        var tvSpacePrice: TextView = itemView.findViewById(R.id.tvSpacePrice)
        var tvRatingStar: TextView = itemView.findViewById(R.id.tvRatingStar)
        var tvRatingCount: TextView = itemView.findViewById(R.id.tvRatingCount)
        var ratingBarReviews: RatingBar = itemView.findViewById(R.id.ratingBarReviews)
        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)




    }

}
