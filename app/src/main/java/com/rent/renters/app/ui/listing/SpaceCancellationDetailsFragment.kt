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
import com.rent.renters.app.data.model.CancellationPolicyItem
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.fragment_space_cancellation_details.*


/**
 * A simple [Fragment] subclass.
 */
class SpaceCancellationDetailsFragment : Fragment(), View.OnClickListener ,CustomRecyclerViewAdapter.CustomItemClickListener{


    private lateinit var listingViewModel: ListingViewModel

    private var mCancellationList = ArrayList<Any>()
    private var mInstantOptionList = ArrayList<Any>()
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener

    private var spaceCancellationDetails : HashMap<String, String> = HashMap()
    private var mCancellationId =""
    var mRoot: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            mRoot =  inflater.inflate(R.layout.fragment_space_cancellation_details, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this
        initView()
        initViewModel()
    }

    private fun initView() {
        if(arguments!=null){
            if(arguments?.containsKey(Iconstants.CANCELLATION_RULES)!!)
                etAddMoreCancellationVal.setText(arguments?.getString(Iconstants.CANCELLATION_RULES)!!)
            if(arguments?.containsKey(Iconstants.CANCELLATION_POLICY)!!){
                mCancellationId = arguments?.getString(Iconstants.CANCELLATION_POLICY)!!
                if(arguments?.containsKey(Iconstants.INSTANCE_BOOK)!!)
                    tvInstantBookVal.text = arguments?.getString(Iconstants.INSTANCE_BOOK)!!

            }
        }

        mInstantOptionList.clear()
        mInstantOptionList.add("Yes")
        mInstantOptionList.add("No")
        tvCancellationPolicyVal.setOnClickListener(this)
        tvInstantBookVal.setOnClickListener(this)

        tvCancellationPolicyDesc.text = (context as BaseActivity).spannableColorString(getString(R.string.cancellation_policy_tip),getString(R.string.tip))
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        listingViewModel.callGetCancellationPolicy().observe(viewLifecycleOwner, Observer{
            (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                if(it.data.canRules !=null){
                    mCancellationList.clear()
                    mCancellationList.addAll(it.data.canRules!!)
                    for(i in 0 until mCancellationList.size){
                        if(mCancellationId.equals((mCancellationList[i] as CancellationPolicyItem).to_id,true)){
                            tvCancellationPolicyVal.text = (mCancellationList[i] as CancellationPolicyItem).name
                        }
                    }
                }

            }
        })
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.tvCancellationPolicyVal ->
                Util.showBottomDialog(context!!,mCancellationList,mBottomDialogListener,Iconstants.CANCELLATION_POLICY,false)
            R.id.tvInstantBookVal ->
                Util.showBottomDialog(context!!,mInstantOptionList,mBottomDialogListener,Iconstants.INSTANCE_BOOK,false)
        }

    }

    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        if(isFrom.equals(Iconstants.CANCELLATION_POLICY,true)) {
            mCancellationId = (listItem as CancellationPolicyItem).to_id!!
            tvCancellationPolicyVal.text = (listItem ).name
        }
        else if(isFrom.equals(Iconstants.INSTANCE_BOOK,true))
            tvInstantBookVal.text = listItem.toString()
    }

    fun validateFields() : Boolean{
        return when {
            etAddMoreCancellationVal.text.toString().isEmpty() -> {
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.cencellation_rules_err))
                false
            }
            mCancellationId.isEmpty()->{
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.cencellation__err))
                false
            }
            /* etSpaceSizeVal.text.toString().isEmpty() -> {
                 (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_size_err))
                 false
             }*/
            else -> true
        }
    }

    fun getCancellationDetails() :HashMap<String,String>{
        spaceCancellationDetails.clear()
        spaceCancellationDetails[Iconstants.CANCELLATION_POLICY] = mCancellationId
        spaceCancellationDetails[Iconstants.CANCELLATION_RULES] = etAddMoreCancellationVal.text.toString()
        spaceCancellationDetails[Iconstants.INSTANCE_BOOK] = tvInstantBookVal.text.toString()
        return spaceCancellationDetails
    }
}