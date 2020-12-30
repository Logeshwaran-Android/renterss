package com.rent.renters.app.ui.inbox


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat


import com.rent.renters.R
import com.rent.renters.app.data.model.MessagesItem
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.TimeAgo
import java.text.SimpleDateFormat


class InboxDetailsAdapter(private val mContext: Context?, private val mMessageList: ArrayList<MessagesItem>,private val isHost:Boolean) : RecyclerView.Adapter<InboxDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_inbox_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mMessageList[position].is_read.equals(Iconstants.YES)){
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext!!,R.color.app_background_color))
        }else
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext!!,R.color.inbox_gray_color))

        (mContext as BaseActivity).loadCircleImageWithGlide(holder.ivInboxProfile,mMessageList[position].profile_image,R.drawable.ic_default_circle_profile_img)
        holder.tvInboxName.text = mMessageList[position].firstname

        holder.tvInboxMsg.text = mMessageList[position].message

            holder.tvInboxStatus.visibility = View.VISIBLE
            holder.tvInboxPeriod.visibility = View.VISIBLE
            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(mMessageList[position].check_in)
            val checkout = SimpleDateFormat("yyyy-MM-dd").parse(mMessageList[position].check_out!!)

            holder.tvInboxPeriod.text =  SimpleDateFormat("dd MMM").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM").format(checkout!!))


        mMessageList[position].dateAdded?.let {
            val messageDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(it)
            val time = TimeAgo().locale(mContext)
            val result = time.getTimeAgo(messageDate!!)
            holder.tvInboxTime.text = result
        }
        holder.tvInboxStatus.text = mMessageList[position].booking_status

        when {
            mMessageList[position].booking_status.equals("Paid", true) -> {

                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_green))
            }
            mMessageList[position].booking_status.equals("Accepted", true) -> {
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_green))
            }
            mMessageList[position].booking_status.equals("pending", true) -> {
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_yellow))
            }
            mMessageList[position].booking_status.equals("Enquiry", true) -> {
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_app_color))
            }
            mMessageList[position].booking_status.equals("Expired", true) ->  {
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_red))
            }
            mMessageList[position].booking_status.equals("Declined", true) ->  {
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_red))
            }
            else->{
                holder.tvInboxStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_red))
            }
        }



        holder.parentLayout.setOnClickListener{
            val messageIntent = Intent(mContext,DefaultMessageActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(Iconstants.MESSAGE,mMessageList[position])
            bundle.putBoolean(Iconstants.IS_HOST,isHost)

            messageIntent.putExtra(Iconstants.BUNDLE,bundle)
            mContext.startActivityForResult(messageIntent,Iconstants.MESSAGE_REQUEST_CODE)
        }


    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mMessageList.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal var ivInboxProfile: ImageView = itemView.findViewById(R.id.ivInboxProfile)
        internal var tvInboxName: TextView = itemView.findViewById(R.id.tvInboxName)
        internal var tvInboxTime: TextView = itemView.findViewById(R.id.tvInboxTime)
        internal var tvInboxMsg: TextView = itemView.findViewById(R.id.tvInboxMsg)
        internal var tvInboxStatus: TextView = itemView.findViewById(R.id.tvInboxStatus)
        internal var tvInboxPeriod: TextView = itemView.findViewById(R.id.tvInboxPeriod)
        internal var parentLayout: RelativeLayout = itemView.findViewById(R.id.parentLayout)




    }


}
