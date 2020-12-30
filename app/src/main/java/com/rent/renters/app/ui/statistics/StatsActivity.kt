package com.rent.renters.app.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rent.renters.R
import com.rent.renters.app.data.model.StatsResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat
import java.util.*

class StatsActivity : BaseActivity() {

    private lateinit var statsViewModel: StatsViewModel
    private lateinit var mSessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_stats)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()

    }


    private fun initView() {
        imgBtnBack.visibility = View.VISIBLE
        tvTitle.text = getString(R.string.hosting_summary)

        imgBtnBack.setOnClickListener{finish()}
    }
    private fun initViewModel() {
        statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        statsViewModel.initMethod(this)
        statsViewModel.callGetHostStats().observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                setStatsData(it)
            }
        })
    }

    private fun setStatsData(statsResponse: StatsResponse?) {
        statsResponse?.data?.earnings?.let { it ->
            it.current_payout?.let{
                val date = Calendar.getInstance().time
                tvEarnings.text = SimpleDateFormat("MMMM").format(date).plus(" ").plus(getString(R.string.earnings))
                tvEarningsVal.text = mSessionManager.getCurrencySymbol().plus(it)
            }
            it.total_payout?.let{
                tvTotalPayoutVal.text = mSessionManager.getCurrencySymbol().plus(it)
            }
            it.upcoming_payout?.let{
                tvUpcomingPayoutVal.text = mSessionManager.getCurrencySymbol().plus(it)
            }
            it.overallratings?.let{
                tvRatingVal.text = it
            }
            it.tot_review?.let{
                tvTotalReviewsVal.text = it
            }
        }


    }


}
