package com.rent.renters.app.ui.payment


import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paypal.android.sdk.payments.*
import com.rent.renters.R
import com.rent.renters.app.data.model.PaymentGateWayResponse
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.mylibrary.util.Util
import kotlinx.android.synthetic.main.activity_select_payment.*
import kotlinx.android.synthetic.main.header_layout.*
import org.json.JSONException
import java.lang.Exception
import java.math.BigDecimal
import java.util.ArrayList

class SelectPaymentActivity : BaseActivity(), View.OnClickListener,Util.BottomSuccessClickListener    {
    private lateinit var paymentViewModel: PaymentViewModel

    private lateinit var mSessionManager : SessionManager
    private val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX
    private val REQUEST_CODE_PAYMENT = 1

    private lateinit var mSuccessListener : Util.BottomSuccessClickListener

    private var mBookingNo = ""
    private var mTotalPrice = 0.0

    private val paypalConfig = PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId("AVjVogOmHhF34YrzPJbfr23rThCs5whhs-grksnssH14rygTaFlVueiUpyA80YFVShiT5Xq5aC5gcanN")



    private val payPalItems = ArrayList<PayPalItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        mSessionManager = SessionManager(this)
        mSuccessListener = this
        initView()
        initViewModel()
        startPayPalService()
    }

    private fun initView() {

        if (intent != null) {
            if (intent.hasExtra(Iconstants.BOOKING_NO))
                mBookingNo = intent.getStringExtra(Iconstants.BOOKING_NO)!!
            if(intent.hasExtra(Iconstants.TOTAL)) {
                try {
                    mTotalPrice = intent.getStringExtra(Iconstants.TOTAL)!!.toDouble()
                }catch (ex: Exception){
                    ex.printStackTrace()
                }
            }
        }
        imgBtnBack.setOnClickListener(this)
        tvCreditCard.setOnClickListener(this)
        tvPaypal.setOnClickListener(this)
        tvStripe.setOnClickListener(this)
    }

    private fun initViewModel() {
        paymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        paymentViewModel.initMethod(this)
        paymentViewModel.callGetAvailablePaymentMethod().observe(this, Observer<PaymentGateWayResponse> {
            baseViewModel.rentersLoader.postValue(false)
            if (it.status == "1") {
                setData(it)

            }
        })
    }
    fun setData(it:PaymentGateWayResponse){
        for (i in 0 until it.data.gateways.size) {
            if (it.data.gateways[i].gateway_name!!.contains("Paypal", true)) {
                tvPaypal.visibility = View.VISIBLE
                viewPaypal.visibility = View.VISIBLE
                tvPaypal.text = it.data.gateways[i].gateway_name
            }
            if(it.data.gateways[i].gateway_name!!.contains("Stripe", true)) {
                tvStripe.visibility = View.VISIBLE
                viewStripe.visibility = View.VISIBLE
                tvStripe.text = it.data.gateways[i].gateway_name
            }
            if(it.data.gateways[i].gateway_name!!.contains("Credit", true)) {
                tvCreditCard.visibility = View.VISIBLE
                viewCreditCard.visibility = View.VISIBLE
                tvCreditCard.text = it.data.gateways[i].gateway_name
            }
        }

    }

    private fun launchPayPalPayment() {

        val thingsToBuy = prepareFinalCart()

        val intent = Intent(this, PaymentActivity::class.java)

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy)

        startActivityForResult(intent, REQUEST_CODE_PAYMENT)
    }

    private fun startPayPalService() {
        // Starting PayPal service
        val intent = Intent(this, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        startService(intent)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBtnBack -> finish()
            R.id.tvCreditCard -> {
                val cardPaymentIntent = Intent(this, CardPaymentActivity::class.java)
                cardPaymentIntent.putExtra(Iconstants.BOOKING_NO, mBookingNo)
                cardPaymentIntent.putExtra(Iconstants.TYPE, "credit_card")
                startActivity(cardPaymentIntent)

            }
            R.id.tvStripe -> {
                val cardPaymentIntent = Intent(this, CardPaymentActivity::class.java)
                cardPaymentIntent.putExtra(Iconstants.BOOKING_NO, mBookingNo)
                cardPaymentIntent.putExtra(Iconstants.TYPE, "stripe")
                startActivity(cardPaymentIntent)

            }
            R.id.tvPaypal ->{
                if (!payPalItems.isEmpty())
                    payPalItems.clear()

                // adding shopping cart items to paypal
                // val item = PayPalItem(mCardItems.get(i).getName(), mCardItems.get(i).getQuantity(), BigDecimal.valueOf(mCardItems.get(i).getPrice()), Config.DEFAULT_CURRENCY, mCardItems.get(i).getSku())

                    val item = PayPalItem(mBookingNo, 1, BigDecimal.valueOf(mTotalPrice), "USD","1")

                    payPalItems.add(item)


                launchPayPalPayment()

            }

        }
    }

    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     */
    private fun prepareFinalCart(): PayPalPayment {

        val items: Array<PayPalItem?> = payPalItems.toTypedArray()

        // Total amount
        val subtotal = PayPalItem.getItemTotal(items)

        // If you have shipping cost, add it here
        val shipping = BigDecimal("0.0")

        // If you have tax, add it here
        val tax = BigDecimal("0.0")

        val paymentDetails = PayPalPaymentDetails(
                shipping, subtotal, tax)

        val amount = subtotal.add(shipping).add(tax)

        val payment = PayPalPayment(
                amount,
                mSessionManager.getCurrencyCode(),
                "we are happy to serve.",
                PayPalPayment.PAYMENT_INTENT_SALE)

        payment.items(items).paymentDetails(paymentDetails)

        // Custom field like invoice_number etc.,
        payment.custom("This is text that will be associated with the payment that the app can use.")

        return payment
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    try {
                        /**
                         * TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */

                        val payment_id = confirm.toJSONObject().getJSONObject("response").getString("id")
                        var state = confirm.toJSONObject().getJSONObject("response").getString("state")

                        verifyPaymentOnServer(payment_id, "1")

                     //   displayResultText("PaymentConfirmation info received from PayPal")

                    } catch (e: JSONException) {
                      //  Log.e(TAG, "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
             //   Log.i(TAG, "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
              //  Log.i( TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        }
    }

    private fun verifyPaymentOnServer(paymentId: String, state: String) {
        paymentViewModel.callPaymentByPaypal(mBookingNo,paymentId,state,"").observe(this, Observer {
            baseViewModel.rentersLoader.postValue(false)

           // if(it.status == "1") {
              /*  val homeIntent = Intent(this, HomeActivity::class.java)
                homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_PAGE)
                homeIntent.putExtra(Iconstants.MESSAGE,it.message)
                homeIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
                startActivity(homeIntent)
                finishAffinity()*/
                val mMessage = (it.message)
            if(it.status=="1") {
                Util.showSuccessDialog(this, getString(R.string.success),mMessage, mSuccessListener, Iconstants.BOOKING_SUCCESS)
            }else{
                Util.showSuccessDialog(this,getString(R.string.unsuccess), mMessage, mSuccessListener, Iconstants.BOOKING_UNSUCCESS)
            }

           /* }else{
                baseViewModel.rentersError.postValue(it.message)
                val homeIntent = Intent(this, HomeActivity::class.java)
                homeIntent.putExtra(Iconstants.IS_FROM,Iconstants.BOOKING_PAGE)
                homeIntent.putExtra(Iconstants.MESSAGE,it.message)
                startActivity(homeIntent)
                finishAffinity()
            }*/

        })

    }

    override fun onSuccessClick(isFrom: String) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        homeIntent.putExtra(Iconstants.IS_FROM,isFrom)
        homeIntent.putExtra(Iconstants.BOOKING_NO,mBookingNo)
        startActivity(homeIntent)
        Util.dismissSuccessDialog()
        finishAffinity()
    }
}
