package com.rent.renters.app.ui.transactionHistory


import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.header_layout.*

class TransactionHistoryFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_transaction_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }


    private fun initView() {
        if(arguments!=null){
            if(arguments?.containsKey(Iconstants.TYPE)!!) {
                val mType = arguments?.getString(Iconstants.TYPE)
                val transactionIntent = Intent(context, TransactionListActivity::class.java)
                transactionIntent.putExtra(Iconstants.TYPE,mType)
                startActivity(transactionIntent)
            }
        }

        tvTitle.text = getString(R.string.transaction_history)
        imgBtnBack.visibility = View.GONE
        cvTransactions.setOnClickListener(this)
        cvPendingTransaction.setOnClickListener(this)
        cvCompletedTransaction.setOnClickListener(this)


    }
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.cvTransactions -> {
                val transactionIntent = Intent(context, TransactionListActivity::class.java)
                transactionIntent.putExtra(Iconstants.TYPE,Iconstants.TRANSACTIONS)
                startActivity(transactionIntent)
                //overridePendingTransition(R.anim.enter,R.anim.exit)
            }
            R.id.cvPendingTransaction -> {
                val transactionIntent = Intent(context, TransactionListActivity::class.java)
                transactionIntent.putExtra(Iconstants.TYPE,Iconstants.REQUESTED_TRANSACTIONS)
                startActivity(transactionIntent)
                //overridePendingTransition(R.anim.enter,R.anim.exit)
            }
            R.id.cvCompletedTransaction -> {
                val transactionIntent = Intent(context, TransactionListActivity::class.java)
                transactionIntent.putExtra(Iconstants.TYPE,Iconstants.COMPLETED_TRANSACTIONS)
                startActivity(transactionIntent)
                //overridePendingTransition(R.anim.enter,R.anim.exit)
            }
        }

    }

}