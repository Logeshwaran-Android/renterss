package com.rent.renters.app.ui.myBookings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.rent.renters.R
import com.rent.renters.app.data.model.CancellationReasonData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity


class CancellationReasonAdapter(internal var mContext: Context, private val mListItems: ArrayList<CancellationReasonData>, private val mListener: CancellationClickListener) : RecyclerView.Adapter<CancellationReasonAdapter.ViewHolder>() {

    private lateinit var mSessionManager: SessionManager

    interface CancellationClickListener {
        fun cancellationClick(item: CancellationReasonData, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cancellation_reason, parent, false)
        mSessionManager = SessionManager(mContext)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.rbCancellationItem.text = mListItems[position].reason.toString().trim()
        holder.rbCancellationItem.isChecked = mListItems[position].isSelected!!
        holder.rbCancellationItem.setOnCheckedChangeListener{button,isChecked ->
            if(button.isPressed) {
                for (i in 0 until mListItems.size) {
                    mListItems[i].isSelected = i == position
                }

                mListener.cancellationClick(mListItems[position], position)
                (mContext as BaseActivity).runOnUiThread {
                    notifyDataSetChanged()
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
        return mListItems.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rbCancellationItem: RadioButton = itemView.findViewById(R.id.rbCancellationItem)

    }


}
