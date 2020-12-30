package com.rent.renters.app.ui.reservations


import android.content.Context

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.ImageView

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rent.renters.R
import com.rent.renters.app.data.model.MyReservationListData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants

import java.text.SimpleDateFormat


class MyReservationsAdapter(internal var mContext: Context,private val mReservationList:ArrayList<MyReservationListData>,private val mListener:MyReservationClickListener) : RecyclerView.Adapter<MyReservationsAdapter.ViewHolder>() {
    private lateinit var mSessionManager: SessionManager

    interface MyReservationClickListener {
        fun reseravtionClick(item: MyReservationListData,isFrom : String,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_reservation_list_item, parent, false)
        mSessionManager = SessionManager(mContext)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val checkin = SimpleDateFormat("yyyy-MM-dd").parse(mReservationList[position].check_in!!)
        val checkout = SimpleDateFormat("yyyy-MM-dd").parse(mReservationList[position].check_out!!)
        holder.tvSpaceName.text = mReservationList[position].product_name
        holder.tvBookedDate.text = SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
        holder.tvBookingNo.text = (mReservationList[position].booking_no)

         mReservationList[position].address?.let{
             var address = mReservationList[position].address!!.city
             if (address != "null"){
                 address = address.plus(",").plus(mReservationList[position].address!!.state)
             }
             if(address !="null"){
                 address = address.plus(",").plus(mReservationList[position].address!!.country)
             }
             holder.tvLocation.text = address
        }
        holder.tvLocation.text = mReservationList[position].full_address
       // holder.tvTotalFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus( mReservationList[position].total_booking_fee)
        holder.tvBookingFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus( mReservationList[position].booking_fee)
       // holder.tvServiceFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mReservationList[position].service_fee)
        holder.tvGuestVal.text = mReservationList[position].firstname
        holder.tvStatusVal.text = mReservationList[position].booking_status
        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpace,mReservationList[position].banner_photos,R.drawable.ic_empty_space)

        holder.btnMessage.setOnClickListener {
            mListener.reseravtionClick(mReservationList[position],Iconstants.MESSAGE,position)
        }
        holder.btnApproveDecline.setOnClickListener{
            holder.btnApproveDecline.tag?.let{
            if(holder.btnApproveDecline.tag.toString().equals(Iconstants.PRE_APPROVE,true)) {
                mListener.reseravtionClick(mReservationList[position],Iconstants.PRE_APPROVE,position)
            }else{
                mListener.reseravtionClick(mReservationList[position],Iconstants.INVOICE,position)
            }

            }

        }
        when {

            mReservationList[position].booking_status!!.equals("pending",true) -> {
                holder.btnApproveDecline.visibility = View.VISIBLE
                holder.btnApproveDecline.tag = Iconstants.PRE_APPROVE
                holder.btnApproveDecline.setTextColor(ContextCompat.getColor(mContext,(R.color.text_white)))
                holder.btnApproveDecline.background = mContext.getDrawable(R.drawable.rectangle_appcolor_border_button)
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext,R.color.text_yellow))
            }
            mReservationList[position].booking_status!!.equals("paid",true) -> {
                holder.btnApproveDecline.visibility = View.VISIBLE
                holder.btnApproveDecline.tag = Iconstants.INVOICE
                holder.btnApproveDecline.text = mContext.getString(R.string.invoice)
                holder.btnApproveDecline.setTextColor(ContextCompat.getColor(mContext,(R.color.text_black)))
                holder.btnApproveDecline.background = mContext.getDrawable(R.drawable.rectangle_lavender_button)
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext,R.color.text_green))
            }
            mReservationList[position].booking_status!!.equals("accepted",true) -> {
                holder.btnApproveDecline.visibility = View.GONE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext,R.color.text_green))
            }
            else -> {
                holder.btnApproveDecline.visibility = View.GONE
                holder.tvStatusVal.setTextColor(ContextCompat.getColor(mContext,R.color.text_red))
            }
        }
        holder.clBooking.setOnClickListener{
            mListener.reseravtionClick(mReservationList[position], Iconstants.PROPERTY,position)
        }


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mReservationList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivSpace: ImageView = itemView.findViewById(R.id.ivSpace)
        var tvSpaceName: TextView = itemView.findViewById(R.id.tvSpaceName)
        var tvBookedDate: TextView = itemView.findViewById(R.id.tvBookedDate)
        var tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        var tvBookingFeeVal: TextView = itemView.findViewById(R.id.tvBookingFeeVal)
       // var tvServiceFeeVal: TextView = itemView.findViewById(R.id.tvServiceFeeVal)
        var tvBookingNo: TextView = itemView.findViewById(R.id.tvBookingNoVal)
        var tvGuestVal: TextView = itemView.findViewById(R.id.tvGuestVal)
        var tvStatusVal: TextView = itemView.findViewById(R.id.tvStatusVal)
        var btnMessage: Button = itemView.findViewById(R.id.btnMessage)
        var btnApproveDecline: Button = itemView.findViewById(R.id.btnApproveDecline)
        var clBooking: ConstraintLayout = itemView.findViewById(R.id.clBooking)


    }


}
