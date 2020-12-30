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
import com.rent.renters.app.data.model.PropertyTypeData
import com.rent.renters.app.data.model.SpaceTypeData
import com.rent.renters.app.data.model.SubPropertyTypeData
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.fragment_space_type.*

/**
 * A simple [Fragment] subclass.
 */
class SpaceTypeFragment : Fragment(),CustomRecyclerViewAdapter.CustomItemClickListener {

    private var spaceTypeDetails : HashMap<String, String> = HashMap()
    private var mSpaceType = "0"
    private var mPropertyType = "0"
    private lateinit var listingViewModel: ListingViewModel

    private var mSpaceTypeList: ArrayList<Any> = ArrayList()
    private var mPropertyTypeList: ArrayList<Any> = ArrayList()
    private lateinit var mBottomDialogListener: CustomRecyclerViewAdapter.CustomItemClickListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_space_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomDialogListener = this
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        listingViewModel.callGetPropertyTypes().observe(viewLifecycleOwner, Observer {
            if (it.status == "1") {
                mSpaceTypeList.addAll(it.data.list)
                for (i in 0 until mSpaceTypeList.size) {
                    if ((mSpaceTypeList[i] as SpaceTypeData).id.equals(mSpaceType, true)) {
                        tvSpaceTypeVal.text = (mSpaceTypeList[i] as SpaceTypeData).name
                        break
                    }
                }
            }
        })
        callGetSubCategoryType()

        }

    private fun callGetSubCategoryType(){
        listingViewModel.callGetSubPropertyTypes(mSpaceType).observe(viewLifecycleOwner, Observer {
            if (it.status == "1") {
                it.data.prop_type?.let { it1 ->
                    mPropertyTypeList.clear()
                    mPropertyTypeList.addAll(it1)
                    for (i in 0 until mPropertyTypeList.size) {
                        if ((mPropertyTypeList[i] as SubPropertyTypeData).id.equals(mPropertyType, true)) {
                            tvPropertyTypeVal.text = (mPropertyTypeList[i] as SubPropertyTypeData).name
                            break
                        }
                    }
                }
            }
        })
    }


    private fun initView() {

        if(arguments !=null){
            if(arguments!!.containsKey(Iconstants.SPACE_TYPE))
                mSpaceType = arguments?.getString(Iconstants.SPACE_TYPE)!!

                if(arguments!!.containsKey(Iconstants.PROPERTY_TYPE))
                    mPropertyType = arguments?.getString(Iconstants.PROPERTY_TYPE)!!

            if(arguments!!.containsKey(Iconstants.SPACE_SIZE)) {
                if(!arguments?.getString(Iconstants.SPACE_SIZE).equals("0"))
                etSpaceSizeVal.setText(arguments?.getString(Iconstants.SPACE_SIZE))
            }
        }
        tvSpaceTypeVal.setOnClickListener{

           Util.showBottomDialog(context!!,mSpaceTypeList,mBottomDialogListener,Iconstants.SPACE_TYPE,false)
        }
        tvPropertyTypeVal.setOnClickListener{
            Util.showBottomDialog(context!!,mPropertyTypeList,mBottomDialogListener,Iconstants.PROPERTY_TYPE,false)
        }
    }



    fun validateFields() : Boolean{
        return when {
            tvSpaceTypeVal.text.toString().isEmpty() -> {
                tvSpaceTypeVal.text = tvSpaceTypeVal.text.toString()
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_type_err))
                false
            }
           /* etSpaceSizeVal.text.toString().isEmpty() -> {
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_size_err))
                false
            }*/
            else -> true
        }
    }
    override fun onAdapterItemClick(listItem: Any, position: Int, isFrom: String) {
        Util.dismissBottomDialog()
        if(isFrom == Iconstants.SPACE_TYPE) {
            tvSpaceTypeVal.text = (listItem as SpaceTypeData).name
            tvPropertyTypeVal.text =""
            mSpaceType = (listItem).id!!
            mPropertyType =""
            callGetSubCategoryType()
        } else if(isFrom == Iconstants.PROPERTY_TYPE){
            tvPropertyTypeVal.text = (listItem as SubPropertyTypeData).name
            mPropertyType = (listItem).id!!
        }
    }

    fun getSpaceTypeDetails() :HashMap<String,String>{
        spaceTypeDetails.clear()
        spaceTypeDetails[Iconstants.SPACE_TYPE] = mSpaceType
        spaceTypeDetails[Iconstants.SPACE_SIZE] = etSpaceSizeVal.text.toString()
        spaceTypeDetails[Iconstants.PROPERTY_TYPE] = mPropertyType
        return spaceTypeDetails
    }

}// Required empty public constructor
