package com.rent.renters.app.ui.myBookings


import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rent.renters.R
import com.rent.renters.app.data.model.InvoiceResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.detailPage.ViewHostDetailsActivity
import kotlinx.android.synthetic.main.activity_invoice.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat

class InvoiceActivity : BaseActivity() , OnMapReadyCallback {


    private var mBookingNo =""
    private lateinit var googleMap: GoogleMap
    private var mSessionManager: SessionManager?= null
    private lateinit var myBookingsViewModel: MyBookingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        mSessionManager = SessionManager(this)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        myBookingsViewModel = ViewModelProvider(this).get(MyBookingsViewModel::class.java)
        myBookingsViewModel.initMethod(this)
        myBookingsViewModel.callGetInvoice(mBookingNo).observe(this, Observer<InvoiceResponse> {
            baseViewModel.rentersLoader.postValue(false)
            setInvoiceData(it)

        })
    }

    private fun setInvoiceData(invoiceResponse: InvoiceResponse?) {
        invoiceResponse?.data?.invoice_details?.product_name?.let{
            tvPropertyName.text = it
        }
        tvBookingNo.text = getString(R.string.booking_no).plus(" ").plus(mBookingNo)
        invoiceResponse?.data?.invoice_details?.pay_date?.let{
            val payDate = SimpleDateFormat("yyyy-MM-dd").parse(it)
            tvBookingDate.text = SimpleDateFormat("dd MMM yyyy").format(payDate!!)
        }
        tvAddress.text = invoiceResponse?.data?.invoice_details?.full_address
        val mHostText = getString(R.string.hosted_by).plus(" ").plus(invoiceResponse?.data?.invoice_details?.firstname)
        //tvHostName.text = spannableColorString(mHostText,invoiceResponse?.data?.invoice_details?.firstname!!)

        val spanString = SpannableString(mHostText)
        val clickableHostNameSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val webViewIntent = Intent(this@InvoiceActivity, ViewHostDetailsActivity::class.java)
                webViewIntent.putExtra(Iconstants.ID,invoiceResponse?.data?.invoice_details?.ownerid)
                startActivity(webViewIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@InvoiceActivity,R.color.text_app_color)
                ds.isUnderlineText = true
            }
        }
        spanString.setSpan(clickableHostNameSpan, mHostText.indexOf(invoiceResponse?.data?.invoice_details?.firstname!!),
                mHostText.indexOf(invoiceResponse.data.invoice_details.firstname!!) + invoiceResponse.data.invoice_details.firstname!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvHostName.text = spanString
        tvHostName.movementMethod = LinkMovementMethod.getInstance()
        var mMobileCode = invoiceResponse.data.invoice_details.mobile_code
        mMobileCode =   mMobileCode!!.replace("\n","")
        mMobileCode = mMobileCode.replace("+","")
        tvPhone.text = getString(R.string.phone).plus(" : ").plus("+").plus(mMobileCode).plus(" ").plus(invoiceResponse.data.invoice_details.phone_number)

      //  tvPhone.text = getString(R.string.phone).plus(" : ").plus(invoiceResponse.data.invoice_details.mobile_code).plus(invoiceResponse.data.invoice_details.phone_number)

        loadImageWithGlide(ivProperty,invoiceResponse.data.invoice_details.banner_photos,R.drawable.ic_empty_space)



        invoiceResponse.data.invoice_details.check_in?.let {
            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(it)
            tvCheckInVal.text = SimpleDateFormat("dd MMM yyyy").format(checkin!!)
        }
        invoiceResponse.data.invoice_details.check_out?.let{
            val checkout = SimpleDateFormat("yyyy-MM-dd").parse(it)
            tvCheckOutVal.text =  SimpleDateFormat("dd MMM yyyy").format(checkout!!)

        }

            invoiceResponse.data.invoice_details.discount_fee?.let {
                if (it != "0"  && it.isNotEmpty()) {
                    tvDiscountFee.visibility = View.VISIBLE

                    tvDiscountFeeVal.visibility = View.VISIBLE
                    tvDiscountFeeVal.text = mSessionManager!!.getCurrencySymbol().plus(it)
                } else {
                    tvDiscountFee.visibility = View.GONE
                    tvDiscountFeeVal.visibility = View.GONE
                }
            }



        tvDayPrice.text = mSessionManager!!.getCurrencySymbol().plus(invoiceResponse.data.invoice_details.base_price).plus(" x ").plus(invoiceResponse.data.invoice_details.days).plus(" ").plus(getString(R.string.days))

        tvPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(invoiceResponse.data.invoice_details.booking_fee)

        tvServiceFeeVal.text =mSessionManager!!.getCurrencySymbol().plus(invoiceResponse.data.invoice_details.service_fee)
        tvTotalPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(invoiceResponse.data.invoice_details.total_booking_fee)

        val mTotalTxt = getString(R.string.total).plus(" (").plus(mSessionManager!!.getCurrencyCode()).plus(")")
        tvTotalPrice.text = spannableColorString(mTotalTxt,mSessionManager!!.getCurrencyCode())

        if(invoiceResponse.data.invoice_details.payment_type!=null)
        tvPaymentMethod.text = getString(R.string.charged_to).plus(" ").plus(invoiceResponse.data.invoice_details.payment_type)
        tvPaymentPriceVal.text = mSessionManager!!.getCurrencySymbol().plus(invoiceResponse.data.invoice_details.total_booking_fee)
        if(invoiceResponse.data.invoice_details.pay_date != null)
        tvPaymentDate.text = invoiceResponse.data.invoice_details.pay_date

        val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_circle)
        val markerOptions = MarkerOptions().position(LatLng(invoiceResponse.data?.invoice_details?.latitude!!.toDouble(), invoiceResponse.data?.invoice_details?.longitude!!.toDouble()))
                .icon(icon)
        googleMap.addMarker(markerOptions)

    }

    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tvTitle.text = getString(R.string.invoice)
        imgBtnBack.setOnClickListener{ finish()}
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = false

    }
}
