package com.rent.renters.app.ui.listing




import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_space_description.*
import com.rent.renters.R

import com.rent.renters.app.ui.base.Iconstants


/**
 * A simple [Fragment] subclass.
 */
class SpaceDescriptionFragment : Fragment(){


    private val  mMaxWordCount = 150
    private var mIsFrom = ""


    private var spaceDescriptionDetails : HashMap<String, String>
            = HashMap<String, String> ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return  inflater.inflate(R.layout.fragment_space_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){


        if(arguments!=null){

            if(arguments!!.containsKey(Iconstants.IS_FROM)) {
                mIsFrom = (arguments?.getString(Iconstants.IS_FROM))!!
            }


            if(arguments!!.containsKey(Iconstants.SUMMARY)){
                etSummary.setText(arguments?.getString(Iconstants.SUMMARY))
                if(etSummary.text.toString().isNotEmpty()) {
                    val input = etSummary.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvSummaryWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }
                }
            if(arguments!!.containsKey(Iconstants.OTHER_THINGS_TO_NOTE)) {
                etOtherThings.setText(arguments?.getString(Iconstants.OTHER_THINGS_TO_NOTE))
                if(etOtherThings.text.toString().isNotEmpty()) {
                    val input = etOtherThings.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvOtherThingsWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }
            }
            if(arguments!!.containsKey(Iconstants.ABOUT_NEIGHBORHOOD)) {
                etNeighborhood.setText(arguments?.getString(Iconstants.ABOUT_NEIGHBORHOOD))
                if(etNeighborhood.text.toString().isNotEmpty()) {
                    val input = etNeighborhood.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvNeighborhoodWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }
            }

            if(arguments!!.containsKey(Iconstants.ABOUT_YOUR_PLACE)) {

                etAboutYourPlace.setText(arguments?.getString(Iconstants.ABOUT_YOUR_PLACE))
                if(etAboutYourPlace.text.toString().isNotEmpty()) {
                    val input = etAboutYourPlace.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvAboutYourPlaceWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }
            }
            if(arguments!!.containsKey(Iconstants.SPACE_RULES)) {
                etSpaceRules.setText(arguments?.getString(Iconstants.SPACE_RULES))
                if(etSpaceRules.text.toString().isNotEmpty()) {
                    val input = etSpaceRules.text.toString().trim().replace("\n", " ")
                    val wordCount = input.split(" ")
                    tvSpaceRulesWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))
                }
            }

                if(etAboutYourPlace.text.toString().trim().isEmpty() && etOtherThings.text.toString().trim().isEmpty() && etSpaceRules.text.toString().trim().isEmpty() && etNeighborhood.text.toString().trim().isEmpty()){
                    llAddMore.visibility = View.VISIBLE
                    clOptionalDetails.visibility = View.GONE
                }else{
                    llAddMore.visibility = View.GONE
                    clOptionalDetails.visibility = View.VISIBLE

                }
        }

        llAddMore.setOnClickListener{
            llAddMore.visibility = View.GONE
            clOptionalDetails.visibility = View.VISIBLE
        }

        etSummary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etSummary.text.toString().trim().replace("\n", " ")
                val wordCount = input.split(" ")
                if(wordCount.size < mMaxWordCount) {
                    etSummary.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etSummary.text.toString().length+1000)))

                    tvSummaryWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    etSummary.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etSummary.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        etAboutYourPlace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etAboutYourPlace.text.toString().trim().replace("\n", " ")
                val wordCount = input.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if(wordCount.size < mMaxWordCount) {
                    etAboutYourPlace.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etAboutYourPlace.text.toString().length+1000)))

                    tvAboutYourPlaceWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    tvAboutYourPlaceWordCount.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etAboutYourPlace.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        etOtherThings.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etOtherThings.text.toString().trim().replace("\n", "")
                val wordCount = input.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if(wordCount.size < mMaxWordCount) {
                    etOtherThings.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etOtherThings.text.toString().length+1000)))

                    tvOtherThingsWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    tvOtherThingsWordCount.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etOtherThings.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        etSpaceRules.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etSpaceRules.text.toString().trim().replace("\n", "")
                val wordCount = input.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if(wordCount.size < mMaxWordCount) {
                    etSpaceRules.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etSpaceRules.text.toString().length+1000)))

                    tvSpaceRulesWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    tvSpaceRulesWordCount.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etSpaceRules.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        etNeighborhood.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = etNeighborhood.text.toString().trim().replace("\n", "")
                val wordCount = input.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if(wordCount.size < mMaxWordCount) {
                    etNeighborhood.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etNeighborhood.text.toString().length+1000)))

                    tvNeighborhoodWordCount.text = (mMaxWordCount - wordCount.size).toString().plus(" ").plus(getString(R.string.words_left))

                }
                else
                    tvNeighborhoodWordCount.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(etNeighborhood.text.toString().length)))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


    }

    fun getDescriptionDetails() :HashMap<String,String>{
        spaceDescriptionDetails.clear()
        spaceDescriptionDetails[Iconstants.SUMMARY] = etSummary.text.toString()
        spaceDescriptionDetails[Iconstants.ABOUT_YOUR_PLACE] = etAboutYourPlace.text.toString()
        spaceDescriptionDetails[Iconstants.OTHER_THINGS_TO_NOTE] = etOtherThings.text.toString()
        spaceDescriptionDetails[Iconstants.SPACE_RULES] = etSpaceRules.text.toString()
        spaceDescriptionDetails[Iconstants.ABOUT_NEIGHBORHOOD] = etNeighborhood.text.toString()
        return spaceDescriptionDetails
    }


}
