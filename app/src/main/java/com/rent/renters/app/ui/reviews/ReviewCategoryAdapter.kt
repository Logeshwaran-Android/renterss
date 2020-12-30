package com.rent.renters.app.ui.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.ReviewCategoryItem
import com.rent.renters.app.ui.base.Iconstants


class ReviewCategoryAdapter(private val mContext: Context, private val mReviewsList: ArrayList<ReviewCategoryItem>, private val mIsFrom:String, private val mListener: ReviewCategoryInterface?) : RecyclerView.Adapter<ReviewCategoryAdapter.ViewHolder>() {


    interface ReviewCategoryInterface{
        fun onReviewCategoryItemClick(item : ReviewCategoryItem, listPosition: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_review_category_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvReviewCategory.text = mReviewsList[position].title.toString().trim()

        holder.tvReviewCategoryDesc.text = mReviewsList[position].details.toString().trim()

        if(mIsFrom == Iconstants.ADD_REVIEW){
            holder.ratingBarReviews.isClickable = true
            holder.ratingBarReviews.setOnRatingBarChangeListener{_,rating, isChecked->
                mReviewsList[position].rating = rating.toString()
                mListener!!.onReviewCategoryItemClick(mReviewsList[position],position)
            }
        }else if(mIsFrom == Iconstants.VIEW_REVIEW) {
            if (mReviewsList[position].rating != null) {
                if (mReviewsList[position].rating!!.isNotEmpty())
                    holder.ratingBarReviews.rating = mReviewsList[position].rating!!.toFloat()
            }
            holder.ratingBarReviews.isClickable = false
            holder.ratingBarReviews.setIsIndicator(true)

        }


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mReviewsList.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tvReviewCategory: TextView = itemView.findViewById(R.id.tvCategory)
        internal var tvReviewCategoryDesc: TextView = itemView.findViewById(R.id.tvCategoryDesc)
        internal var ratingBarReviews: RatingBar = itemView.findViewById(R.id.ratingBarCategory)


    }
}
