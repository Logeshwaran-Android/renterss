package com.rent.renters.app.ui.userHomePage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

import com.rent.renters.R
import com.rent.renters.app.data.model.SearchSpaceData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity


class SimilarSpaceTypeItemAdapter(private val mContext: Context, private var mListener: SpaceTypeItemClickListener, private val mSearchSpaceList: ArrayList<SearchSpaceData>) : RecyclerView.Adapter<SimilarSpaceTypeItemAdapter.ViewHolder>() {
    private lateinit var mSessionManager : SessionManager

       interface SpaceTypeItemClickListener {
        fun spaceTypeItemClick(item:SearchSpaceData)
           fun addRemoveWishList(item : SearchSpaceData,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_space_type_horizontal_items, parent, false)
            mSessionManager = SessionManager(mContext)

            return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       // holder.ivSpaceItem.setOnClickListener { mListener.spaceTypeItemClick() }
        if(mSearchSpaceList[position].isSelected)
            holder.progressBar.visibility = View.VISIBLE
        else
            holder.progressBar.visibility = View.INVISIBLE


        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpaceItem,mSearchSpaceList[position].banner_photos!!,R.drawable.ic_empty_space)

        holder.tvSpaceCity.text = mSearchSpaceList[position].full_address
        holder.tvSpaceTypeItemName.text = mSearchSpaceList[position].product_name
        holder.tvSpacePrice.text = mSessionManager.getCurrencySymbol().plus(mSearchSpaceList[position].base_price).plus(" ").plus(mContext.getString(R.string.per_day))
        holder.tvSpaceSize.text = mContext.getString(R.string.entire_space).plus("-").plus(mSearchSpaceList[position].property_size).plus(" ").plus(mContext.getString(R.string.sq_feet))

        if(mSearchSpaceList[position].is_favorite)
            holder.imgBtnFavorite.setImageResource(R.drawable.ic_favorite_red)
        else
            holder.imgBtnFavorite.setImageResource(R.drawable.ic_favorite)

        holder.parentLayout.setOnClickListener{
            mListener.spaceTypeItemClick(mSearchSpaceList[position])
        }

        holder.imgBtnFavorite.setOnClickListener{
            mListener.addRemoveWishList(mSearchSpaceList[position],position)
        }
        if(mSearchSpaceList[position].instant_booking!=null) {
            if (mSearchSpaceList[position].instant_booking!!.equals("yes", true))
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


        var ivSpaceItem: ImageView = itemView.findViewById(R.id.ivSpaceTypeItem)
        var tvSpaceSize: TextView = itemView.findViewById(R.id.tvSpaceSize)
        var tvSpaceTypeItemName: TextView = itemView.findViewById(R.id.tvSpaceTypeItemName)
        var tvSpacePrice: TextView = itemView.findViewById(R.id.tvSpacePrice)
        var tvSpaceCity: TextView = itemView.findViewById(R.id.tvSpaceCity)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        var imgBtnFavorite: ImageButton = itemView.findViewById(R.id.imgBtnFavorite)
        var imgBtnInstant: ImageButton = itemView.findViewById(R.id.imgBtnInstant)
        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)




    }

}
