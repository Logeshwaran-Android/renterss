package com.rent.renters.app.ui.myBookings


import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rent.renters.R
import com.rent.renters.app.data.model.MyTripsListData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants

import java.text.SimpleDateFormat


class MyBookingsAdapter(internal var mContext: Context, private val mBookingList: ArrayList<MyTripsListData>, private val mListener: MyBookingsClickListener) : RecyclerView.Adapter<MyBookingsAdapter.ViewHolder>() {

    private lateinit var mSessionManager:SessionManager

    interface MyBookingsClickListener {
        fun bookingClick(item: MyTripsListData, isFrom: String,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_booking_list_item, parent, false)
        mSessionManager = SessionManager(mContext)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val checkin = SimpleDateFormat("yyyy-MM-dd").parse(mBookingList[position].check_in!!)
        val checkout = SimpleDateFormat("yyyy-MM-dd").parse(mBookingList[position].check_out!!)
        holder.tvSpaceName.text = mBookingList[position].product_name
        holder.tvBookedDate.text =  SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
        holder.tvTotalFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mBookingList[position].total_booking_fee)
        mBookingList[position].address.let {
            holder.tvLocation.text = it.city.plus(",").plus(it.state).plus(",").plus(it.country)
        }
        holder.tvBookingFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mBookingList[position].booking_fee)
        holder.tvServiceFeeVal.text =  mSessionManager.getCurrencySymbol().plus(" ").plus(mBookingList[position].service_fee)
        holder.tvGuestVal.text = mBookingList[position].firstname
        holder.tvStatusVal.text = mBookingList[position].status
        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpace, mBookingList[position].property_image, R.drawable.ic_empty_space)

        holder.tvBookingNo.text = mBookingList[position].booking_no

        mBookingList[position].discount_amt.let {
            if (it.equals("0") || it.equals("0.00")){
                holder.tvDiscountFeeVal.visibility = View.GONE
                holder.tvDiscountFee.visibility = View.GONE
                holder.viewDiscountFee.visibility = View.GONE
                holder.viewDiscountFeeVal.visibility = View.GONE

            } else{
                holder.tvDiscountFeeVal.visibility = View.VISIBLE
                holder.tvDiscountFee.visibility = View.VISIBLE
                holder.viewDiscountFee.visibility = View.VISIBLE
                holder.viewDiscountFeeVal.visibility = View.VISIBLE
                holder.tvDiscountFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(it)

            }
        }

        when {
            mBookingList[position].status!!.equals("accepted", true) -> {
                holder.llBottomLayout.visibility = View.GONE
                holder.btnPay.visibility = View.VISIBLE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext, R.color.text_green))
            }
            mBookingList[position].status!!.equals("Paid", true) -> {
                holder.llBottomLayout.visibility = View.VISIBLE
                holder.btnPay.visibility = View.GONE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext, R.color.text_green))
            }
            mBookingList[position].status!!.equals("pending", true) -> {
                holder.llBottomLayout.visibility = View.GONE
                holder.btnMessage.visibility = View.GONE
                holder.btnPay.visibility = View.GONE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext, R.color.text_yellow))
            }
            else -> {
                holder.llBottomLayout.visibility = View.GONE
                holder.btnPay.visibility = View.GONE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext, R.color.text_red))
            }
        }

        if (mBookingList[position].review_status!! == "1") {
            holder.btnReview.visibility = View.VISIBLE
            if (mBookingList[position].review_added!! == "1") {
                holder.btnReview.tag = Iconstants.VIEW_REVIEW
                holder.btnReview.text = mContext.getString(R.string.view_review)
            } else {
                holder.btnReview.tag = Iconstants.ADD_REVIEW
                holder.btnReview.text = mContext.getString(R.string.write_review)
            }
        } else {
            holder.btnReview.visibility = View.GONE
            val param = LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    1.0f
            )
            holder.btnInvoice.layoutParams = param
        }
        if(mBookingList[position].canc_status.equals(Iconstants.YES,true)){
            holder.btnCancel.visibility = View.VISIBLE
        }else
            holder.btnCancel.visibility = View.GONE

        holder.btnMessage.setOnClickListener {
            //  DefaultMessageActivity.open(mContext)
            mListener.bookingClick(mBookingList[position], Iconstants.MESSAGE,position)

        }
        holder.btnInvoice.setOnClickListener {
            mListener.bookingClick(mBookingList[position], Iconstants.INVOICE,position)
        }

        holder.btnReview.setOnClickListener {
            mListener.bookingClick(mBookingList[position], holder.btnReview.tag.toString(),position)
        }

        holder.btnPay.setOnClickListener {
            mListener.bookingClick(mBookingList[position], Iconstants.PAY,position)

        }

        holder.clBooking.setOnClickListener{
            mListener.bookingClick(mBookingList[position], Iconstants.PROPERTY,position)
        }

        holder.btnCancel.setOnClickListener{
            mListener.bookingClick(mBookingList[position], Iconstants.CANCEL_BOOKING,position)
        }


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mBookingList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivSpace: ImageView = itemView.findViewById(R.id.ivSpace)
        var tvSpaceName: TextView = itemView.findViewById(R.id.tvSpaceName)
        var tvBookedDate: TextView = itemView.findViewById(R.id.tvBookedDate)
        var tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        var tvBookingNo: TextView = itemView.findViewById(R.id.tvBookingNo)
        var tvBookingFeeVal: TextView = itemView.findViewById(R.id.tvBookingFeeVal)
        var tvServiceFeeVal: TextView = itemView.findViewById(R.id.tvServiceFeeVal)
        var tvTotalFeeVal: TextView = itemView.findViewById(R.id.tvTotalVal)
        var tvGuestVal: TextView = itemView.findViewById(R.id.tvGuestVal)
        var tvStatusVal: TextView = itemView.findViewById(R.id.tvStatusVal)
        var btnMessage: Button = itemView.findViewById(R.id.btnMessage)
        var btnInvoice: Button = itemView.findViewById(R.id.btnInvoice)
        var btnReview: Button = itemView.findViewById(R.id.btnReview)
        var btnPay: Button = itemView.findViewById(R.id.btnPay)
        var btnCancel: Button = itemView.findViewById(R.id.btnCancel)
        var llBottomLayout: LinearLayout = itemView.findViewById(R.id.llBottomLayout)
        var clBooking: ConstraintLayout = itemView.findViewById(R.id.clBooking)
        var tvDiscountFeeVal: TextView = itemView.findViewById(R.id.tvDiscountFeeVal)
        var tvDiscountFee: TextView = itemView.findViewById(R.id.tvDiscountFee)
        var viewDiscountFee: View = itemView.findViewById(R.id.viewDiscountFee)
        var viewDiscountFeeVal: View = itemView.findViewById(R.id.viewDiscountFeeVal)


    }


}
