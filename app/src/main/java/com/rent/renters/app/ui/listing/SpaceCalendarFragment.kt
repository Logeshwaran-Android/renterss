package com.rent.renters.app.ui.listing

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle

import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.rent.renters.R
import com.rent.renters.app.data.model.BlockedData
import com.rent.renters.app.data.model.BlockedDatesResponse
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.mylibrary.util.*
import com.rent.renters.mylibrary.util.getDrawableCompat
import com.rent.renters.mylibrary.util.setTextColorRes
import kotlinx.android.synthetic.main.calendar_day_price_view.view.*
import kotlinx.android.synthetic.main.calendar_day_view.view.exFourDayText
import kotlinx.android.synthetic.main.calendar_day_view.view.exFourRoundBgView
import kotlinx.android.synthetic.main.calendar_month_header_view.view.*
import kotlinx.android.synthetic.main.fragment_space_calendar.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.header_layout.imgBtnBack
import org.jetbrains.anko.layoutInflater
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.ArrayList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import androidx.core.content.ContextCompat


class SpaceCalendarFragment : Fragment(),Util.BottomApproveClickListener {

    private var mPropId =""
    private var mIsFrom =""
    private var mDefaultPrice =""


    private lateinit var listingViewModel: ListingViewModel
    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    var mBlockedDateList: ArrayList<BlockedData> = ArrayList()

    var mBlockedListDates :ArrayList<LocalDate> = ArrayList()
    var mUnavailableDates :ArrayList<LocalDate> = ArrayList()
    var mSpecialPrice :ArrayList<String> = ArrayList()
    var mSpecialPriceDates :ArrayList<LocalDate> = ArrayList()
    var mPropertyCurrencySymbol = ""

    lateinit var mListener : Util.BottomApproveClickListener

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEEE'\n'd MMM yy")

    private val startBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.continuous_day_selected_bg)!! as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.continuous_selected_bg_end)!! as GradientDrawable
    }

    private var radiusUpdated = false

    /**
     * We set the radius of the continuous selection background drawable dynamically
     * since the view size is `match parent` hence we cannot determine the appropriate
     * radius value which would equal half of the view's size beforehand.
     */
    private fun updateDrawableRadius(textView: TextView) {
        if (radiusUpdated) return
        radiusUpdated = true
        val radius = (textView.height / 2).toFloat()
        startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
        endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_space_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener = this
        if(arguments !=null){
            if(arguments!!.containsKey(Iconstants.ID))
                mPropId = arguments?.getString(Iconstants.ID)!!
            if(arguments!!.containsKey(Iconstants.IS_FROM))
                mIsFrom = arguments?.getString(Iconstants.IS_FROM)!!
        }


        initViewModel()
    }

    private fun initViewModel() {
        listingViewModel = ViewModelProvider(this).get(ListingViewModel::class.java)
        listingViewModel.initMethod(context as Activity)
        callGetBlockedDates()

    }

    private fun callGetBlockedDates() {
        listingViewModel.callGetBlockedDates(mPropId).observe(viewLifecycleOwner, Observer<BlockedDatesResponse> { it ->
             (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                it.data?.dates?.let {

                    mBlockedDateList.clear()
                    mBlockedListDates.clear()
                    mUnavailableDates.clear()
                    mBlockedDateList.addAll(it)
                    mBlockedListDates = (context as BaseActivity).getBlockedDate(mBlockedDateList)
                    mUnavailableDates = (context as BaseActivity).getUnAvailableDates(mBlockedDateList)
                    initView()
                }
                it.data?.specialprice?.let{
                    for(i in 0 until it.size) {
                        mSpecialPrice.add(it[i].price!!)
                        mSpecialPriceDates.add(LocalDate.parse(it[i].value!!))
                    }
                }
                it.data?.details?.let{
                    mPropertyCurrencySymbol = it.currency_symbol
                    mDefaultPrice = it.defaultprice!!
                }
            } else {

                (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
                initView()
            }
        })

    }

    private fun initView() {
        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        calendarView?.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        calendarView?.scrollToMonth(currentMonth)

        tvRightOption.text = getString(R.string.reset)
        tvRightOption.visibility = View.GONE

        tvRightOption.setOnClickListener {
            startDate = null
            endDate = null
            calendarView.notifyCalendarChanged()
            bindSummaryViews()
        }
        imgBtnBack.visibility = View.GONE

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFourDayText
            val textViewPrice = view.tvPrice
            val roundBgView = view.exFourRoundBgView

            init {
                view.setOnClickListener {

                        if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(today)) && (!mBlockedListDates.contains(day.date))) {
                            val date = day.date
                            if (startDate != null) {
                                if (date < startDate || endDate != null) {
                                    startDate = date
                                    endDate = null
                                } else {
                                    endDate = date

                                }
                            } else {
                                startDate = date
                            }
                            calendarView!!.notifyCalendarChanged()
                            bindSummaryViews()

                        }
                    }
            }
        }
        calendarView?.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val tvPrice = container.textViewPrice

                val roundBgView = container.roundBgView

                textView?.text = null
                textView?.background = null
                tvPrice?.text = null
                tvPrice?.background = null
                roundBgView?.makeInVisible()



                if (day.owner == DayOwner.THIS_MONTH) {
                    textView?.text = day.day.toString()

                    if (day.date.isBefore(today)) {
                        textView?.setTextColorRes(R.color.example_4_grey_past)
                    } else {

                        if(mSpecialPriceDates.contains(day.date)){
                            val mPosition = mSpecialPriceDates.indexOf(day.date)
                            tvPrice.setTextColorRes(R.color.text_app_color)
                            tvPrice.text = mPropertyCurrencySymbol.plus(mSpecialPrice[mPosition])

                        }else{
                            tvPrice.setTextColorRes(R.color.text_black)
                            tvPrice.text = mPropertyCurrencySymbol.plus(mDefaultPrice)
                        }
                        when {
                            startDate == day.date && endDate == null -> {
                                textView?.setTextColorRes(R.color.white)
                                roundBgView?.makeVisible()
                                roundBgView?.setBackgroundResource(R.drawable.single_day_selected_bg)
                                tvPrice.setTextColorRes(R.color.text_white)
                            }
                            day.date == startDate -> {
                                textView?.setTextColorRes(R.color.white)
                                updateDrawableRadius(textView)
                                textView?.background = startBackground
                                tvPrice.setTextColorRes(R.color.text_white)
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView?.setTextColorRes(R.color.white)
                                textView?.setBackgroundResource(R.drawable.middle_day_selected_bg)
                                tvPrice.setTextColorRes(R.color.text_white)
                            }
                            day.date == endDate -> {
                                textView?.setTextColorRes(R.color.white)
                                updateDrawableRadius(textView)
                                textView?.background = endBackground
                                tvPrice.setTextColorRes(R.color.text_white)
                            }
                            day.date == today -> {
                                textView?.setTextColorRes(R.color.text_black)
                                roundBgView?.makeVisible()
                                roundBgView?.setBackgroundResource(R.drawable.today_bg)
                            }
                            else -> {

                                if(mSpecialPriceDates.contains(day.date)){
                                    textView?.setTextColorRes(R.color.text_app_color)

                                }else{
                                    textView?.setTextColorRes(R.color.text_black)
                                }

                            }
                        }
                    }

                    if(mBlockedListDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_yellow)
                        tvPrice?.text = null
                        tvPrice?.background = null

                    }else if(mUnavailableDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_red)
                        tvPrice?.text = null
                        tvPrice?.background = null

                    }


                } else {

                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                        && ( startDate.monthValue == day.date.monthValue && startDate.year == day.date.year)
                                        && endDate.monthValue != day.date.monthValue) ||
                                // Mimic selection of outDates that are greater than the endDate.
                                // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                                // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                                (day.owner == DayOwner.NEXT_MONTH
                                        && startDate.monthValue != day.date.monthValue
                                        &&(endDate.monthValue == day.date.monthValue && endDate.year == day.date.year)) ||

                                // Mimic selection of in and out dates of intermediate
                                // months if the selection spans across multiple months.
                                (startDate < day.date && endDate > day.date
                                        && startDate.monthValue != day.date.monthValue
                                        && endDate.monthValue != day.date.monthValue)
                        ) {
                            tvPrice.setTextColorRes(R.color.text_white)
                            textView.setBackgroundResource(R.drawable.middle_day_selected_bg)
                        }
                    }
                    if(mBlockedListDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_yellow)
                        tvPrice?.text = null
                        tvPrice?.background = null

                    }else if(mUnavailableDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_red)
                        tvPrice?.text = null
                        tvPrice?.background = null

                    }



                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        calendarView?.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }


        bindSummaryViews()

    }

    private fun bindSummaryViews() {
        if (startDate != null) {
            tvStartDate.visibility = View.VISIBLE
            tvEndDate.visibility = View.VISIBLE
            tvStartDate?.text = headerDateFormatter.format(startDate)
            tvStartDate?.setTextColorRes(R.color.text_black)
        } else {
            tvStartDate.visibility = View.GONE
            tvEndDate.visibility = View.GONE
            tvStartDate?.text = getString(R.string.start_date)
            tvStartDate?.setTextColor(Color.GRAY)
        }
        if (endDate != null) {

            tvEndDate?.text = headerDateFormatter.format(endDate)
            tvEndDate?.setTextColorRes(R.color.text_black)

        } else {
            tvEndDate?.text = getString(R.string.end_date)
            tvEndDate?.setTextColor(Color.GRAY)
        }
        if(startDate !=null || endDate!=null){
            tvRightOption.visibility = View.VISIBLE
        }
        if(startDate!=null && endDate!=null){
            showCalendarDialog()
        }


    }





    override fun onStart() {
        super.onStart()
        val closeIndicator = requireContext().getDrawableCompat(R.drawable.ic_close)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(R.color.text_black, BlendMode.SRC_ATOP)
            } else {
                setColorFilter(ContextCompat.getColor(context!!,R.color.text_black), PorterDuff.Mode.SRC_ATOP)
            }
            //setColorFilter(requireContext().getColorCompat(R.color.example_4_grey), PorterDuff.Mode.SRC_ATOP)
        }
        (activity as BaseActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = requireContext().getColorCompat(R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor = requireContext().getColorCompat(R.color.colorPrimaryDark)
            decorView.systemUiVisibility = 0
        }
    }

    override fun onApproveClick(message: String, isFrom: String) {
        val formatter = DateTimeFormatter.ofPattern(Iconstants.API_DATE_TIME_FORMAT)

            when (isFrom) {
                Iconstants.AVAILABLE -> {

                    listingViewModel.callManageCalendar(formatter.format(startDate), formatter.format(endDate), mPropId, "available", message).observe(this, Observer {
                        (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
                        (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
                        startDate = null
                        endDate = null
                        if(it.status == "1")
                        callGetBlockedDates()
                    })

                }
                Iconstants.UNAVAILABLE -> {
                    listingViewModel.callManageCalendar(formatter.format(startDate), formatter.format(endDate), mPropId, "unavailable", message).observe(this, Observer {

                        (context as BaseActivity).baseViewModel.rentersLoader.postValue(false)
                        (context as BaseActivity).baseViewModel.rentersError.postValue(it.message)
                        startDate = null
                        endDate = null
                        if(it.status == "1")
                        callGetBlockedDates()

                    })
                }
        }
    }

    private fun showManageCalendarDialog(listener: Util.BottomApproveClickListener, startDate:String, endDate:String, currencySymbol:String) {
        val theme: Int = R.style.BottomSheetDialogTheme
        val mManageCalendarDialog = BottomSheetDialog(context!!,theme)
        val sheetView = context!!.layoutInflater.inflate(R.layout.dialog_manage_calendar_bottom_sheet, null)
        mManageCalendarDialog.setContentView(sheetView)

        val etPrice = sheetView.findViewById<EditText>(R.id.etPrice)
        val tvCurrencySymbol = sheetView.findViewById<TextView>(R.id.tvCurrencySymbol)
        val btnAvailable = sheetView.findViewById<Button>(R.id.btnAvailable)
        val btnUnavailable = sheetView.findViewById<Button>(R.id.btnUnAvailable)
        val ivClose = sheetView.findViewById<ImageButton>(R.id.ivClose)
        val tvStartDate = sheetView.findViewById<TextView>(R.id.tvStartDate)
        val tvEndDate = sheetView.findViewById<TextView>(R.id.tvEndDate)
        tvCurrencySymbol.text = currencySymbol
        etPrice.hint = (" 0.00")
        tvStartDate.text = startDate
        tvEndDate.text = endDate

        ivClose.setOnClickListener{
            mManageCalendarDialog.dismiss()
        }

        btnAvailable.setOnClickListener{
            if(etPrice.text.toString().isEmpty()){
                (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.space_price_err))

            } else {
                mManageCalendarDialog.dismiss()
                listener.onApproveClick(etPrice.text.toString(), Iconstants.AVAILABLE)
            }
        }

        btnUnavailable.setOnClickListener{

                mManageCalendarDialog.dismiss()
                listener.onApproveClick(etPrice.text.toString(), Iconstants.UNAVAILABLE)


        }

        if (!mManageCalendarDialog.isShowing)
            mManageCalendarDialog.show()
    }

    public fun showCalendarDialog(){
        when {
            (tvStartDate.text.toString().isEmpty() || tvStartDate.text.toString().equals(getString(R.string.start_date),true)) -> (context as BaseActivity).baseViewModel.rentersError.postValue(getString(R.string.select_date_err))
            (tvEndDate.text.toString().isEmpty() || tvEndDate.text.toString().equals(getString(R.string.end_date),true))-> {
                endDate = startDate
                showManageCalendarDialog(mListener,tvStartDate.text.toString(),tvStartDate.text.toString(),mPropertyCurrencySymbol)
            }
            else -> showManageCalendarDialog(mListener,tvStartDate.text.toString(),tvEndDate.text.toString(),mPropertyCurrencySymbol)
        }
    }

}
