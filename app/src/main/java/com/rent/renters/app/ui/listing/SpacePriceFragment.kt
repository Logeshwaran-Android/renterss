package com.rent.renters.app.ui.listing


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.CurrencyListData
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.fragment_space_price.*


/**
 * A simple [Fragment] subclass.
 */
class SpacePriceFragment : Fragment() ,CustomRecyclerViewAdapter.CustomItemClickListener{
    private lateinit var listingViewModel: ListingViewModel

    private var priceingDetails : HashMap<String, String> = HashMap()

    private var mCurrencyList: ArrayList<Any> = ArrayList()
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener
    private var mCurrencyCode =""

    var mRoot: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            return inflater.inflate(R.layout.fragment_space_price, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this

        initViewModel()
        initView()
    }

    private fun initView() {

        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.CURRENCY)) {
                mCurrencyCode = arguments?.getString(Iconstants.CURRENCY)!!
                tvCurrencyVal.text = mCurrencyCode
            }
            if(arguments!!.containsKey(Iconstants.SPACE_PRICE))
                etBasePriceVal.setText(arguments?.getString(Iconstants.SPACE_PRICE))
            if(arguments!!.containsKey(Iconstants.WEEKLY_DISCOUNT))
                etWeeklyDiscount.setText(arguments?.getString(Iconstants.WEEKLY_DISCOUNT))
            if(arguments!!.containsKey(Iconstants.MONTHLY_DISCOUNT))
                etMonthlyDiscount.setText(arguments?.getString(Iconstants.MONTHLY_DISCOUNT))
        }

        tvCurrencyVal.setOnClickListener{
            Util.showBottomDialog(context!!,mCurrencyList,mBottomDialogListener, Iconstants.SPACE_PRICE,false)
        }
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        listingViewModel.callGetCurrencyList().observe(viewLifecycleOwner, Observer {
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                mCurrencyList.clear()
                if(it.data?.currency!=null)
                mCurrencyList.addAll(it.data?.currency!!)
                it.data?.currency?.let {
                    for (i in 0 until it.size){
                        if(it[i].currency_type.equals(mCurrencyCode,true)){
                            tvBasePrice.text = getString(R.string.base_price).plus(" (").plus(it[i].currency_symbols).plus(")")
                            break
                        }

                    }
                }
            }
        })
    }

    fun validateFields() : Boolean{
        return when {
            tvCurrencyVal.text.toString().isEmpty() -> {
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_currency_err))
                false
            }
            etBasePriceVal.text.toString().isEmpty() -> {
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_price_err))
                false
            }
            else -> true
        }
    }


    fun getPricingDetails() :HashMap<String,String>{
        priceingDetails.clear()
        priceingDetails[Iconstants.CURRENCY] = tvCurrencyVal.text.toString()
        priceingDetails[Iconstants.SPACE_PRICE] = etBasePriceVal.text.toString()
        priceingDetails[Iconstants.WEEKLY_DISCOUNT] = etWeeklyDiscount.text.toString()
        priceingDetails[Iconstants.MONTHLY_DISCOUNT] = etMonthlyDiscount.text.toString()
        return priceingDetails
    }



    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        tvCurrencyVal.text = listItem.toString()
        tvBasePrice.text = getString(R.string.base_price).plus(" (").plus((mCurrencyList.get(position) as CurrencyListData).currency_symbols).plus(")")

    }
}
