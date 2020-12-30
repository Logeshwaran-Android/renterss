package com.rent.renters.app.ui.listing


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.fragment_space_enhance_search.*


/**
 * A simple [Fragment] subclass.
 */
class SpaceEnhanceSearchFragment : Fragment() {


    private var searchDetails : HashMap<String, String> = HashMap()

    var mRoot: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(mRoot == null) {
            return inflater.inflate(R.layout.fragment_space_enhance_search, container, false)
        }
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.META_TITLE))
                etTitle.setText(arguments?.getString(Iconstants.META_TITLE)!!)
            if(arguments!!.containsKey(Iconstants.META_KEYWORD))
                etKeywords.setText(arguments?.getString(Iconstants.META_KEYWORD)!!)
            if(arguments!!.containsKey(Iconstants.META_DESCRIPTION))
                etDescription.setText(arguments?.getString(Iconstants.META_DESCRIPTION)!!)
            if(arguments!!.containsKey(Iconstants.MINIMUM_STAY))
                etMinimumStay.setText(arguments?.getString(Iconstants.MINIMUM_STAY)!!)
            if(arguments!!.containsKey(Iconstants.MAXIMUM_STAY))
                etMaximumStay.setText(arguments?.getString(Iconstants.MAXIMUM_STAY)!!)
        }
    }

    fun getEnhanceSearchDetails() :HashMap<String,String>{
        searchDetails.clear()
        searchDetails[Iconstants.META_TITLE] = etTitle.text.toString()
        searchDetails[Iconstants.META_KEYWORD] = etKeywords.text.toString()
        searchDetails[Iconstants.META_DESCRIPTION] = etDescription.text.toString()
        searchDetails[Iconstants.MINIMUM_STAY] = etMinimumStay.text.toString()
        searchDetails[Iconstants.MAXIMUM_STAY] = etMaximumStay.text.toString()
        return searchDetails
    }

}
