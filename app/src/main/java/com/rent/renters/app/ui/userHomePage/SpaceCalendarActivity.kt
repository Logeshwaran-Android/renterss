package com.rent.renters.app.ui.userHomePage

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.GradientDrawable

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.rent.renters.R
import com.rent.renters.app.data.model.BlockedData
import com.rent.renters.app.data.model.BlockedDatesResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.detailPage.DetailPageViewModel
import com.rent.renters.mylibrary.util.*
import com.rent.renters.mylibrary.util.setTextColorRes
import kotlinx.android.synthetic.main.activity_space_calendar.*
import kotlinx.android.synthetic.main.calendar_day_price_view.view.*
import kotlinx.android.synthetic.main.calendar_day_view.view.exFourDayText
import kotlinx.android.synthetic.main.calendar_day_view.view.exFourRoundBgView
import kotlinx.android.synthetic.main.calendar_month_header_view.view.*
import kotlinx.android.synthetic.main.header_layout.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.util.ArrayList

class SpaceCalendarActivity : BaseActivity() {

    private lateinit var detailPageViewModel: DetailPageViewModel

    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    var mBlockedDateList: ArrayList<BlockedData> = ArrayList()

    var mBlockedListDates : ArrayList<LocalDate> = ArrayList()
    var mUnavailableDates : ArrayList<LocalDate> = ArrayList()
    var mSpecialPrice :ArrayList<String> = ArrayList()
    var mSpecialPriceDates :ArrayList<LocalDate> = ArrayList()

    private var mDefaultPrice =""
    private var mPropId =""
    private var mShowBlockedDates = false
    private  lateinit var mSessionManager : SessionManager

    var position = 0
    private var mDisableClick = false

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEEE'\n'd MMM yy")
    private val headerDateYearFormatter = DateTimeFormatter.ofPattern("EEEE'\n'd MMM YYYY")

    private val startBackground: GradientDrawable by lazy {
        return@lazy getDrawable(R.drawable.continuous_day_selected_bg)!! as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        return@lazy getDrawable(R.drawable.continuous_selected_bg_end)!! as GradientDrawable
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_calendar)
        mSessionManager = SessionManager(this)
        getIntentValues()
        customizeToolbar()
        if(mShowBlockedDates)
             initViewModel()
        else {

            initView()
        }

    }

    private fun initViewModel() {
        detailPageViewModel = ViewModelProvider(this).get(DetailPageViewModel::class.java)
        detailPageViewModel.initMethod(this)
        detailPageViewModel.callGetBlockedDates(mPropId).observe(this, Observer<BlockedDatesResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                if (it.data?.dates != null) {
                    mBlockedDateList.clear()
                    mBlockedDateList.addAll(it.data?.dates!!)
                    mBlockedListDates = getBlockedDate(mBlockedDateList)
                    mUnavailableDates =getUnAvailableDates(mBlockedDateList)
                    initView()

                }
                it.data?.specialprice?.let{
                    for(i in 0 until it.size) {
                        mSpecialPrice.add(it[i].price!!)
                        mSpecialPriceDates.add(LocalDate.parse(it[i].value!!))
                    }
                }
                it.data?.details?.let{

                    mDefaultPrice = it.defaultprice!!
                }
            } else {
                baseViewModel.rentersError.postValue(it.message)
                initView()
            }
        })
    }

    private fun initView() {

        tvRightOption.setOnClickListener {
            startDate = null
            endDate = null
            spaceCalendar.notifyCalendarChanged()
            bindSummaryViews()
            tvRightOption.visibility = View.GONE

        }
        imgBtnBack.setOnClickListener {

            finish()
        }

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        spaceCalendar.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.last())
        spaceCalendar.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFourDayText
            val textViewPrice = view.tvPrice
            val roundBgView = view.exFourRoundBgView

            init {

                view.setOnClickListener {
                    if(!mDisableClick) {
                        if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(today)) && (!mBlockedListDates.contains(day.date)) && (!mUnavailableDates.contains(day.date))) {
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
                            spaceCalendar.notifyCalendarChanged()
                            bindSummaryViews()
                        }
                    }
                }
            }
        }
        spaceCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val tvPrice = container.textViewPrice
                val roundBgView = container.roundBgView


                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()


                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()
                    textView.background = null

                    if (day.date.isBefore(today)) {
                        tvPrice.text = ""
                        textView.setTextColorRes(R.color.example_4_grey_past)
                    } else {
                        if(!mShowBlockedDates)
                        tvPrice.visibility = View.GONE
                        else
                            tvPrice.visibility = VISIBLE
                        if(mSpecialPriceDates.contains(day.date)){
                            val mPosition = mSpecialPriceDates.indexOf(day.date)
                            tvPrice.setTextColorRes(R.color.text_app_color)
                            tvPrice.text = mSessionManager.getCurrencySymbol().plus(mSpecialPrice[mPosition])

                        }else{
                            tvPrice.setTextColorRes(R.color.text_black)
                            tvPrice.text = mSessionManager.getCurrencySymbol().plus(mDefaultPrice)
                        }
                        when {

                            startDate == day.date && endDate == null -> {

                                textView.setTextColorRes(R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.single_day_selected_bg)
                                tvPrice.setTextColorRes(R.color.white)

                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.background = startBackground
                                tvPrice.setTextColorRes(R.color.white)


                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                        textView.setTextColorRes(R.color.text_white)
                                        tvPrice.setTextColorRes(R.color.text_white)
                                        textView.setBackgroundResource(R.drawable.middle_day_selected_bg)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                tvPrice.setTextColorRes(R.color.white)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.example_4_grey)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.today_bg)
                            }

                            else -> textView.setTextColorRes(R.color.example_4_grey)
                        }
                    }

                    if(!day.date.isBefore(today) && mBlockedListDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_yellow)
                        tvPrice.text =""


                    }else if(!day.date.isBefore(today) && mUnavailableDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_red)
                        tvPrice.text =""


                    }
                } else {

                    tvPrice.visibility = View.GONE
                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

                    val startDate = startDate
                    val endDate = endDate
                    var isEnd = false
                    if(day.date == endDate)
                        isEnd = true

                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                        && ( startDate.monthValue == day.date.monthValue && startDate.year == day.date.year)
                                        && endDate.monthValue != day.date.monthValue ) ||
                                // Mimic selection of outDates that are greater than the endDate.
                                // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                                // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                                (day.owner == DayOwner.NEXT_MONTH
                                        && startDate.monthValue != day.date.monthValue
                                        &&(endDate.monthValue == day.date.monthValue && endDate.year == day.date.year))||

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


                    if(!day.date.isBefore(today) && mBlockedListDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_yellow)

                    }else if(!day.date.isBefore(today) && mUnavailableDates.contains(day.date)){
                        textView.setTextColorRes(R.color.text_red)


                    }
                }

            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        spaceCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        btnSave.setOnClickListener click@{
            val startDate = startDate
            val endDate = endDate

            if (startDate != null && endDate != null) {
                val formatter = DateTimeFormatter.ofPattern(Iconstants.API_DATE_TIME_FORMAT)
                val resultIntent = Intent()
                resultIntent.putExtra(Iconstants.START_DATE, formatter.format(startDate))
                resultIntent.putExtra(Iconstants.END_DATE, formatter.format(endDate))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else if(startDate ==null){
                baseViewModel.rentersError.postValue(getString(R.string.start_date_err))
            } else if(endDate == null){
                baseViewModel.rentersError.postValue(getString(R.string.end_date_err))
            }
        }

        bindSummaryViews()

    }

    private fun getIntentValues() {
        if (intent != null) {
            if(intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)!!
                if (bundle.containsKey(Iconstants.START_DATE)) {
                    val mStartDate = bundle.getString(Iconstants.START_DATE)
                    startDate = LocalDate.parse(mStartDate)

                }
                if (bundle.containsKey(Iconstants.END_DATE)) {
                    val mEndDate = bundle.getString(Iconstants.END_DATE)
                    endDate = LocalDate.parse(mEndDate)

                }

                if (bundle.containsKey(Iconstants.ID)) {
                    mPropId = bundle.getString(Iconstants.ID)!!
                }

                if (bundle.containsKey(Iconstants.SHOW_BLOCKED_DATES)) {
                    mShowBlockedDates = bundle.getBoolean(Iconstants.SHOW_BLOCKED_DATES)
                }

                if (bundle.containsKey(Iconstants.DISABLE_INTERACTION)) {
                    if(bundle.getBoolean(Iconstants.DISABLE_INTERACTION)) {
                        mDisableClick = true
                        date_header_layout.visibility = View.GONE
                        btnSave.visibility = View.GONE
                        tvRightOption.visibility = View.GONE
                    }

                }
            }
        }

        if(startDate !=null && endDate!=null){
            tvRightOption.visibility = VISIBLE
        }else
            tvRightOption.visibility = View.GONE


    }

    private fun customizeToolbar() {
        tvRightOption.text = getString(R.string.reset)
        imgBtnBack.setImageResource(R.drawable.ic_close)
    }

    private fun bindSummaryViews() {
        if (startDate != null) {
            tvStartDate.visibility = View.VISIBLE
            tvEndDate.visibility = View.VISIBLE
           /* if(startDate!!.year.equals(today!!.year)){
                tvStartDate.text = headerDateFormatter.format(startDate)
            }else{*/
                tvStartDate.text = headerDateYearFormatter.format(startDate)
          //  }

        } else {
           // tvStartDate.text = getString(R.string.start_date)
            tvStartDate.visibility = View.GONE
            tvEndDate.visibility = View.GONE
        }
        if (endDate != null) {

          /*  if(endDate!!.year.equals(today!!.year)) {
                tvEndDate.text = headerDateFormatter.format(endDate)
            }else{*/
                tvEndDate.text = headerDateYearFormatter.format(endDate)
            //}
        } else {

            tvEndDate.text = getString(R.string.end_date)
        }


        if(startDate !=null || endDate!=null){
            tvRightOption.visibility = VISIBLE

        }
        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
        //btnSave.isEnabled = (startDate != null && endDate != null)
    }
}


