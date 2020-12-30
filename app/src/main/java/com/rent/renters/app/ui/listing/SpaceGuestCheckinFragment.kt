package com.rent.renters.app.ui.listing

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rent.renters.R
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.fragment_space_guest_checkin.*


class SpaceGuestCheckinFragment : Fragment(), View.OnClickListener {


    private var guestCheckinDetails : HashMap<String, String> = HashMap()

    private var mItemClicked = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_space_guest_checkin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if(arguments!=null){
            if(arguments!!.containsKey(Iconstants.CHECK_IN_FROM_TIME))
                tvCheckinStartTimeVal.text = arguments?.getString(Iconstants.CHECK_IN_FROM_TIME)!!
            if(arguments!!.containsKey(Iconstants.CHECK_IN_TO_TIME))
                tvCheckinEndTimeVal.text = arguments?.getString(Iconstants.CHECK_IN_TO_TIME)!!
            if(arguments!!.containsKey(Iconstants.CHECK_OUT_TIME))
                tvCheckOutTimeVal.text = arguments?.getString(Iconstants.CHECK_OUT_TIME)!!
        }

        tvCheckinStartTimeVal.setOnClickListener(this)
        tvCheckinEndTimeVal.setOnClickListener(this)
        tvCheckOutTimeVal.setOnClickListener(this)
    }

    private fun showCalendarDialog(hour: Int, minutes: Int) {

        val dialog = TimePickerDialog(context!!, timePickerListener, hour, minutes, false)

        dialog.show()
    }

    private val timePickerListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinutes ->

        updateTime(selectedHour,selectedMinutes)

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private fun updateTime(hour: Int, mins: Int) {
        var hours = hour

        var timeSet = ""
        when {
            hours > 12 -> {
                hours -= 12
                timeSet = "PM"
            }
            hours == 0 -> {
                hours += 12
                timeSet = "AM"
            }
            hours == 12 -> timeSet = "PM"
            else -> timeSet = "AM"
        }


        val minutes = if (mins < 10)
            "0$mins"
        else
            mins.toString()

        // Append in a StringBuilder
        val aTime = StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString()

        when (mItemClicked) {
            Iconstants.CHECK_IN_FROM_TIME -> tvCheckinStartTimeVal.text = aTime
            Iconstants.CHECK_IN_TO_TIME -> tvCheckinEndTimeVal.text = aTime
            Iconstants.CHECK_OUT_TIME -> tvCheckOutTimeVal.text = aTime
        }
    }

    fun getCheckInDetails() :HashMap<String,String>{
        guestCheckinDetails.clear()
        guestCheckinDetails[Iconstants.CHECK_IN_FROM_TIME] = tvCheckinStartTimeVal.text.toString()
        guestCheckinDetails[Iconstants.CHECK_IN_TO_TIME] = tvCheckinEndTimeVal.text.toString()
        guestCheckinDetails[Iconstants.CHECK_OUT_TIME] = tvCheckOutTimeVal.text.toString()
        return guestCheckinDetails
    }

    override fun onClick(view: View?) {
        mItemClicked =""
        when(view!!.id){
            R.id.tvCheckinStartTimeVal ->{
                mItemClicked = Iconstants.CHECK_IN_FROM_TIME
                showCalendarDialog(12,0)
            }
            R.id.tvCheckinEndTimeVal ->{
                mItemClicked = Iconstants.CHECK_IN_TO_TIME
                showCalendarDialog(12,0)
            }
            R.id.tvCheckOutTimeVal ->{
                mItemClicked = Iconstants.CHECK_OUT_TIME
                showCalendarDialog(12,0)
            }
        }

    }


}
