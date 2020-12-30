package com.rent.renters.app.ui.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.ReviewAverageCategoryItem


class ReviewAverageCategoryAdapter(private val mContext: Context, private val mReviewsList: ArrayList<ReviewAverageCategoryItem>) : RecyclerView.Adapter<ReviewAverageCategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_review_average_category_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvReviewCategory.text = mReviewsList[position].name

            if (mReviewsList[position].avg_review != null) {
                if (mReviewsList[position].avg_review!!.isNotEmpty())
                    holder.ratingBarReviews.rating = mReviewsList[position].avg_review!!.toFloat()

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
        internal var ratingBarReviews: RatingBar = itemView.findViewById(R.id.ratingBarCategory)


    }
}
