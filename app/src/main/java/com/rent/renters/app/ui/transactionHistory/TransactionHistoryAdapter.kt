package com.rent.renters.app.ui.transactionHistory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.MyTripsListData
import com.rent.renters.app.data.model.TransactionItem
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import java.text.SimpleDateFormat

class TransactionHistoryAdapter(internal var mContext: Context, private val mTransactionList: ArrayList<TransactionItem>, private val mListener: MyTransactionClickListener,private val mIsFrom:String) : RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    private lateinit var mSessionManager: SessionManager

    interface MyTransactionClickListener {
        fun transactionClick(item: MyTripsListData, isFrom: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_transaction_list_item, parent, false)
        mSessionManager = SessionManager(mContext)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mIsFrom.equals(Iconstants.COMPLETED_TRANSACTIONS,true)){
            holder.llBillingDetails.visibility = View.VISIBLE
            val paidDate = SimpleDateFormat("yyyy-MM-dd").parse(mTransactionList[position].paid_date)!!
            holder.tvPaidAmountVal.text = mTransactionList[position].amount
            holder.tvTransactionDateVal.text = SimpleDateFormat("dd MMM yyyy").format(paidDate)
            holder.tvPaymentTypeVal.text = mContext.getString(R.string.via_bank)
            holder.tvTransactionNoVal.text = mTransactionList[position].transaction_id
        }else{
            holder.viewBillingLineTop.visibility = View.GONE
            holder.llBillingDetails.visibility = View.GONE
        }

        val checkin = SimpleDateFormat("yyyy-MM-dd").parse(mTransactionList[position].check_in!!)
        val checkout = SimpleDateFormat("yyyy-MM-dd").parse(mTransactionList[position].check_out!!)
        holder.tvSpaceName.text = mTransactionList[position].product_name
        holder.tvBookingNoVal.text = mTransactionList[position].booking_no
        holder.tvGuestVal.text = mTransactionList[position].firstname
        holder.tvBookedDate.text =  SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
        holder.tvAcceptanceFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mTransactionList[position].acceptance_fee)
        mTransactionList[position].address.let {
            holder.tvAddressVal.text = mTransactionList[position].address.city
        }
        holder.tvBookingFeeVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mTransactionList[position].total_fee)
        holder.tvServiceFeeVal.text =  mSessionManager.getCurrencySymbol().plus(" ").plus(mTransactionList[position].service_fee)
        holder.tvNetAmountVal.text = mSessionManager.getCurrencySymbol().plus(" ").plus(mTransactionList[position].host_fee)
       // holder.tvStatusVal.text = mBookingList[position].status
        (mContext as BaseActivity).loadImageWithGlide(holder.ivSpace, mTransactionList[position].profile_image, R.drawable.ic_empty_space)

    }


    override fun getItemViewType(position: Int): Int {

        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mTransactionList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivSpace: ImageView = itemView.findViewById(R.id.ivSpace)
        var tvSpaceName: TextView = itemView.findViewById(R.id.tvSpaceName)
        var tvBookedDate: TextView = itemView.findViewById(R.id.tvBookedDate)
        var tvBookingFeeVal: TextView = itemView.findViewById(R.id.tvBookingFeeVal)
        var tvServiceFeeVal: TextView = itemView.findViewById(R.id.tvServiceFeeVal)
        var tvAcceptanceFeeVal: TextView = itemView.findViewById(R.id.tvAcceptanceFeeVal)
        var tvNetAmountVal: TextView = itemView.findViewById(R.id.tvNetAmountVal)
        var tvPaidAmountVal: TextView = itemView.findViewById(R.id.tvPaidAmountVal)
        var tvTransactionNoVal: TextView = itemView.findViewById(R.id.tvTransactionNoVal)
        var tvTransactionDateVal: TextView = itemView.findViewById(R.id.tvTransactionDateVal)
        var tvPaymentTypeVal: TextView = itemView.findViewById(R.id.tvPaymentTypeVal)
        var llBillingDetails: LinearLayout = itemView.findViewById(R.id.llBillingDetails)
        var tvBookingNoVal: TextView = itemView.findViewById(R.id.tvBookingNoVal)
        var tvGuestVal: TextView = itemView.findViewById(R.id.tvGuestVal)
        var viewBillingLineTop: View = itemView.findViewById(R.id.viewBillingLineTop)
        var tvAddressVal: TextView = itemView.findViewById(R.id.tvAddressVal)




    }


}
