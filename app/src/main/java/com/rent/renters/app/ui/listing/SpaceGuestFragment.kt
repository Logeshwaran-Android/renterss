package com.rent.renters.app.ui.listing


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.fragment_space_guest.*

class SpaceGuestFragment : Fragment(), View.OnClickListener {


    private var guestDetails : HashMap<String, String> = HashMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_space_guest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.GUEST_COUNT)) {
                tvGuestCount.text = (arguments?.getString(Iconstants.GUEST_COUNT))
            }
            if(arguments!!.containsKey(Iconstants.BEDROOM_COUNT))
                tvBedroomCount.text = arguments?.getString(Iconstants.BEDROOM_COUNT)
        }

        ivPlusGuest.setOnClickListener(this)
        ivMinusGuest.setOnClickListener(this)
        ivPlusBedroom.setOnClickListener(this)
        ivMinusBedroom.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivPlusGuest ->{
                if(tvGuestCount.text.toString().toInt() < 100)
                    display(tvGuestCount.text.toString().toInt()+1,tvGuestCount)
            }
            R.id.ivPlusBedroom->{
                if(tvBedroomCount.text.toString().toInt() < 100)
                    display(tvBedroomCount.text.toString().toInt()+1,tvBedroomCount)
            }
            R.id.ivMinusGuest->{
                if(tvGuestCount.text.toString().toInt() > 0)
                    display(tvGuestCount.text.toString().toInt()-1,tvGuestCount)
            }
            R.id.ivMinusBedroom ->{
                if(tvBedroomCount.text.toString().toInt() > 0)
                    display(tvBedroomCount.text.toString().toInt()-1,tvBedroomCount)

            }
        }

    }
    private fun display(number: Int,textView: TextView) {
        textView.text = number.toString()
    }

    fun getGuestDetails() :HashMap<String,String>{
        guestDetails.clear()
        guestDetails[Iconstants.GUEST_COUNT] = tvGuestCount.text.toString()
        guestDetails[Iconstants.BEDROOM_COUNT] = tvBedroomCount.text.toString()
        return guestDetails
    }

}
