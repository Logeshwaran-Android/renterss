package com.rent.renters.app.ui.reviews


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import com.rent.renters.R
import com.rent.renters.app.data.model.ReviewsListData
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import java.text.SimpleDateFormat
import java.util.ArrayList


class EditProfileReviewsListAdapter(private val mContext: Context, private val mReviewsList: ArrayList<ReviewsListData>, private val mFromPage: String?) : RecyclerView.Adapter<EditProfileReviewsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_reviews_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mFromPage.equals(mContext.getString(R.string.reviews_about_you), true) || mFromPage== Iconstants.HOST) {

            holder.tvReviewName.text = mReviewsList[position].firstname

            (mContext as BaseActivity).loadCircleImageWithGlide(holder.ivReviewProfile, mReviewsList[position].profile_image, R.drawable.ic_default_circle_profile_img)
            if (mReviewsList[position].review_rating!!.isNotEmpty())
                holder.ratingBarCategory.rating = mReviewsList[position].review_rating!!.toFloat()
            holder.tvPropertyName.text = mReviewsList[position].product_name

        } else if (mFromPage.equals(mContext.getString(R.string.reviews_by_you), true)) {
            holder.tvPropertyName.visibility = View.GONE
            holder.tvReviewName.text = mReviewsList[position].product_name
            (mContext as BaseActivity).loadCircleImageWithGlide(holder.ivReviewProfile, mReviewsList[position].profile_image, R.drawable.ic_default_circle_profile_img)

        }
        mReviewsList[position].dateAdded?.let {
            if (mReviewsList[position].dateAdded!!.isNotEmpty()) {
                holder.tvReviewDate.visibility = View.VISIBLE
                val reviewDate = SimpleDateFormat("yyyy-MM-dd").parse(mReviewsList[position].dateAdded!!)
                holder.tvReviewDate.text = SimpleDateFormat("MMMM yyyy").format(reviewDate!!)
            } else {
                holder.tvReviewDate.visibility = View.GONE
            }
        }
        holder.tvBookingNo.text = mReviewsList[position].booking_no

        holder.tvPublicReviewsDesc.text = mReviewsList[position].review_description


        if (mReviewsList[position].review_rating!!.isNotEmpty())
            holder.ratingBarCategory.rating = mReviewsList[position].review_rating!!.toFloat()

        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        holder.rvReviews.layoutManager = layoutManager
        mReviewsList[position].review_cat?.let{
            val mReviewsAverageCategoryAdapter = ReviewAverageCategoryAdapter(mContext,it)
            holder.rvReviews.adapter = mReviewsAverageCategoryAdapter
        }


        if(mFromPage != Iconstants.REVIEWS) {
            mReviewsList[position].private_description?.let {
                if (it.isNotEmpty()) {
                    holder.tvPrivateReviews.visibility = View.VISIBLE
                    holder.tvPrivateReviewsDesc.visibility = View.VISIBLE
                    holder.tvPrivateReviewsDesc.text = it
                } else {
                    holder.tvPrivateReviews.visibility = View.GONE
                    holder.tvPrivateReviewsDesc.visibility = View.GONE
                }
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
        return mReviewsList.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var tvReviewName: TextView = itemView.findViewById(R.id.tvReviewName)
        internal var tvPropertyName: TextView = itemView.findViewById(R.id.tvPropertyName)
        internal var tvBookingNo: TextView = itemView.findViewById(R.id.tvBookingNo)
        internal var tvReviewDate: TextView = itemView.findViewById(R.id.tvReviewDate)
        internal var tvPublicReviewsDesc: TextView = itemView.findViewById(R.id.tvPublicCommentVal)
        internal var tvPrivateReviewsDesc: TextView = itemView.findViewById(R.id.tvPrivateCommentVal)
        internal var tvPrivateReviews: TextView = itemView.findViewById(R.id.tvPrivateComment)
        internal var ivReviewProfile: ImageView = itemView.findViewById(R.id.ivReviewProfile)
        internal var ratingBarReviews: RatingBar = itemView.findViewById(R.id.ratingBarReviews)
        internal var ratingBarCategory: RatingBar = itemView.findViewById(R.id.ratingBarCategory)


        internal var rvReviews: RecyclerView = itemView.findViewById(R.id.rvReviews)



    }
}
