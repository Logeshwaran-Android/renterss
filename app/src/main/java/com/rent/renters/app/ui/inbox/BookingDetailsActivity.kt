package com.rent.renters.app.ui.inbox


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rent.renters.R
import com.rent.renters.app.data.model.BookingDetailsResponse
import com.rent.renters.app.data.model.CommonResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.bookingSteps.ReviewAndPayActivity
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_booking_details.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat

class BookingDetailsActivity : BaseActivity(),BottomDialogButtonInterface {


    private var mBookingNo =""
    private var mStatus =""
    private var mIsHost = false

    private lateinit var chatViewModel: ChatViewModel
    private var mSessionManager: SessionManager?= null
    private lateinit var mListener : BottomDialogButtonInterface
    private var mInstantBook =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
        mSessionManager= SessionManager(this)
        mListener = this
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.initMethod(this)
        chatViewModel.callGetBookingDetails(mBookingNo).observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
                    setBookingData(it)
            }else{
                baseViewModel.rentersError.postValue(it.message)
            }
        })
    }

    private fun setBookingData(it: BookingDetailsResponse?) {

        it?.data?.details?.let {
            tvSpaceName.text = it.property_name
            loadImageWithGlide(ivSpace,it.PropImage,R.drawable.ic_default_circle_profile_img)
            tvServiceFeeVal.text = mSessionManager!!.getCurrencySymbol().plus(it.service_fee)
            tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(it.total_fee)
            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(it.check_in!!)
            val checkout = SimpleDateFormat("yyyy-MM-dd").parse(it.check_out!!)
            tvBookedDate.text = SimpleDateFormat("dd MMM yyyy").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM yyyy").format(checkout!!))
            tvBookingNo.text = it.booking_no

            tvPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(it.booking_fee)
            val mTotalTxt = getString(R.string.total).plus(" (").plus(mSessionManager!!.getCurrencyCode()).plus(")")
            tvTotalPrice.text = spannableColorString(mTotalTxt, mSessionManager!!.getCurrencyCode())
        }

    }



    private fun initView(){
        if(intent != null){
            if(intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
            if(intent.hasExtra(Iconstants.STATUS))
                mStatus = intent.getStringExtra(Iconstants.STATUS)!!
            if(intent.hasExtra(Iconstants.INSTANCE_BOOK))
                mInstantBook = intent.getStringExtra(Iconstants.INSTANCE_BOOK)!!
            if(intent.hasExtra(Iconstants.IS_HOST))
                mIsHost = intent.getBooleanExtra(Iconstants.IS_HOST,false)
        }
        if(mIsHost)
            llBook.visibility = View.GONE
        else {
            if(mStatus.equals(Iconstants.ENQUIRY,true))
                llBook.visibility = View.VISIBLE
            else
            llBook.visibility = View.GONE
        }


        when {
            mStatus.equals("pending", true) -> tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_yellow))
            mStatus.equals("Expired", true) -> tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_red))
            else -> tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_green))
        }
        clBooking.setOnClickListener{
           /* val detailPageIntent = Intent(this, DetailPageActivity::class.java)
            detailPageIntent.putExtra(Iconstants.ID, mPr)
            startActivity(detailPageIntent)*/
        }

        btnBook.setOnClickListener{
            Util.showBottomSheetDialogWithButtons(this,getString(R.string.app_name),getString(R.string.booking_msg),mListener,true)
        }
        imgBtnBack.setOnClickListener{
            finish()
        }
    }

    override fun onBottomCookieItemClick() {
        chatViewModel.callPostBookingRequest(mBookingNo).observe(this, Observer<CommonResponse> {
            baseViewModel.rentersLoader.postValue(false)
            baseViewModel.rentersError.postValue(it.message)
            if(it.status == "1"){
                if(mInstantBook.equals("Yes",true)){
                    val reviewAndPayIntent = Intent(this, ReviewAndPayActivity::class.java)
                    reviewAndPayIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                    startActivity(reviewAndPayIntent)
                }else{
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_SUCCESS)
                    startActivity(homeIntent)
                    finishAffinity()

                }

            }

        })
    }
    }
