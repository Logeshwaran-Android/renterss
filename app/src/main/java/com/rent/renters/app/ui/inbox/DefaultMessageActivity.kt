package com.rent.renters.app.ui.inbox

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rent.renters.R
import com.rent.renters.app.data.model.*
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.bookingSteps.ReviewAndPayActivity
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.mylibrary.util.BottomDialogButtonInterface
import com.rent.renters.mylibrary.util.Util
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_default_messages.*
import java.text.SimpleDateFormat
import java.util.*


class DefaultMessageActivity : BaseActivity(),  MessagesListAdapter.OnLoadMoreListener,MessageInput.InputListener,
        MessageInput.TypingListener,BottomDialogButtonInterface {

    companion object{
         var isOnline = false
    }

    lateinit var mSessionManager : SessionManager

    private var messagesAdapter: MessagesListAdapter<Message>? = null

    private var selectionCount: Int = 0

    private lateinit var imageLoader: ImageLoader

    private var mInstantBook =""
    private var mIsHost = false

    private val messages = ArrayList<Message>()

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var mListener : BottomDialogButtonInterface

    private var mAddress = ""
    private var mPropId = ""
    private var mBookingNo = ""
    private var mReceiverId = ""
    private var mBookingStatus = ""
    private var mSenderImg = ""
    private var mSenderName = ""
    private var mType = ""
    private var mProductName = ""
    private var mOwnerName = ""
    private var mOwnerImage = ""
    private var mProductImage = ""
    private var mStartDate = ""
    private var mEndDate = ""
    private var mShowDetails = false
    private lateinit var mMessageItem : MessagesItem
    private var updateChatReceiver : BroadcastReceiver ?= null
    private val filter : IntentFilter = IntentFilter()
    private var TOTAL_MESSAGES_COUNT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_messages)

        mListener = this
        mSessionManager = SessionManager(this)
        imageLoader = ImageLoader { imageView, url, payload -> Glide.with(this).load(url).into(imageView) }
        initAdapter()
        initView()
        initViewModel()
        input.setInputListener(this)
        input.setTypingListener(this)

        updateChatReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // TODO Auto-generated method stub


                if (intent!= null) {
                   if(intent.hasExtra(Iconstants.BUNDLE)){
                       val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
                       if(bundle!!.containsKey(Iconstants.BOOKING_NO)) {
                           if (bundle.getString(Iconstants.BOOKING_NO, "").equals(mBookingNo, true)) {
                               if (bundle!!.containsKey(Iconstants.MESSAGE)) {
                                   setResult(Activity.RESULT_OK)
                                   val mMessage = bundle?.getParcelable<Message>(Iconstants.MESSAGE)
                                   messages.add(mMessage!!)
                                   messagesAdapter?.addToStart(
                                           mMessage, true)

                                   //initAdapter()
                                   // loadMessages()
                                   //messagesAdapter!!.addToEnd(messages, false)
                               }
                           }
                       }
                   }

                }


            }
        }
        filter.addAction(getString(R.string.package_name))
        registerReceiver(updateChatReceiver,filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        isOnline = false
        unregisterReceiver(updateChatReceiver)
    }

   /* override fun onPause() {
        super.onPause()
        isOnline = false
    }
*/
    override fun onResume() {
        super.onResume()
        isOnline = true
    }

    private fun initAdapter() {
        if(messagesAdapter == null) {
            messagesAdapter = MessagesListAdapter(mSessionManager.getUserDetails().id, imageLoader)

            messagesAdapter?.setLoadMoreListener(this)

            messagesList.setAdapter(messagesAdapter)
        } else{
            messagesAdapter!!.notifyDataSetChanged()
        }
    }

    private fun initView() {
        if (intent != null) {
            if(intent.hasExtra(Iconstants.BUNDLE)) {
                val bundle = intent.getBundleExtra(Iconstants.BUNDLE)
                if (bundle!!.containsKey(Iconstants.MESSAGE)) {
                    mShowDetails = true
                    mMessageItem = bundle.getParcelable<MessagesItem>(Iconstants.MESSAGE)!!
                    if(bundle.containsKey(Iconstants.IS_HOST))
                    mIsHost = bundle.getBoolean(Iconstants.IS_HOST)
                    mMessageItem?.let {
                        mMessageItem.booking_no?.let {
                            mBookingNo =it
                        }
                        mMessageItem.receiver_id?.let {
                            mReceiverId = it
                        }
                        mMessageItem.sender_id?.let{
                            if(mReceiverId.equals(mSessionManager.getUserDetails().id,true))
                                mReceiverId = it
                        }
                        mMessageItem.booking_status?.let {
                            mBookingStatus = it
                        }
                        mMessageItem.type?.let {
                            mType = it
                        }
                        mMessageItem.profile_image?.let {
                            mSenderImg = it
                        }
                        mMessageItem.firstname?.let {
                            mSenderName = it
                        }
                        mMessageItem.owner_name?.let {
                            mOwnerName = it
                        }

                        mMessageItem.product_name?.let {
                            mProductName = it
                        }
                        mMessageItem.profile_image?.let {
                            mProductImage = it
                        }
                        mMessageItem.product_id?.let {
                            mPropId = it
                        }
                        mMessageItem.check_in?.let {
                            mStartDate = it
                        }
                        mMessageItem.check_out?.let {
                            mEndDate = it
                        }
                        mMessageItem.instant_booking?.let{
                            mInstantBook = it
                        }

                    }
                } else{
                    if (bundle!!.containsKey(Iconstants.BOOKING_NO))
                        mBookingNo = bundle.getString(Iconstants.BOOKING_NO)!!
                    if (bundle.containsKey(Iconstants.ID))
                        mReceiverId = bundle.getString(Iconstants.ID)!!
                    if (bundle.containsKey(Iconstants.STATUS))
                        mBookingStatus = bundle.getString(Iconstants.STATUS)!!
                    if (bundle.containsKey(Iconstants.IMAGE))
                        mSenderImg = bundle.getString(Iconstants.IMAGE)!!
                    if (bundle.containsKey(Iconstants.FIRST_NAME))
                        mSenderName = bundle.getString(Iconstants.FIRST_NAME)!!
                    if (bundle.containsKey(Iconstants.TYPE))
                        mType = bundle.getString(Iconstants.TYPE)!!


                }

               /* if (bundle.containsKey(Iconstants.STATUS))
                    mBookingStatus = bundle.getString(Iconstants.STATUS)!!
                if (bundle.containsKey(Iconstants.IMAGE))
                    mSenderImg = bundle.getString(Iconstants.IMAGE)!!
                if (bundle.containsKey(Iconstants.FIRST_NAME))
                    mSenderName = bundle.getString(Iconstants.FIRST_NAME)!!
                if (bundle.containsKey(Iconstants.TYPE))
                    mType = bundle.getString(Iconstants.TYPE)!!*/
            }
        }
        if(mSenderImg.isNotEmpty())
            loadImageWithGlide(ivSender,mSenderImg,R.drawable.ic_default_circle_profile_img)
        tvSenderName.text = mSenderName

        if(mShowDetails) {

            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(mStartDate)
            val checkout = SimpleDateFormat("yyyy-MM-dd").parse(mEndDate)

            val mEnquiryData = (getString(R.string.enquiry)).plus(SimpleDateFormat("dd MMM").format(checkin!!).plus(" - ").plus(SimpleDateFormat("dd MMM").format(checkout!!)))
            tvEnquiryDetails.text = mEnquiryData

            if (mBookingStatus.equals(Iconstants.ENQUIRY, true)) {
                if (mSessionManager.getUserType().equals(Iconstants.USER, true)) {
                    tvDetails.visibility = View.VISIBLE
                    llBook.visibility = View.VISIBLE
                    btnBook.setOnClickListener{
                      Util.showBottomSheetDialogWithButtons(this,getString(R.string.app_name),getString(R.string.booking_msg),mListener,true)
                    }


                } else {
                    btnBook.visibility = View.GONE
                    tvDetails.visibility = View.GONE
                }
            }

            tvDetails.setOnClickListener{
                        val bookingDetails = Intent(this, BookingDetailsActivity::class.java)
                        bookingDetails.putExtra(Iconstants.BOOKING_NO, mBookingNo)
                        bookingDetails.putExtra(Iconstants.STATUS,mBookingStatus)
                        bookingDetails.putExtra(Iconstants.INSTANCE_BOOK,mInstantBook)
                bookingDetails.putExtra(Iconstants.IS_HOST,mIsHost)
                        startActivity(bookingDetails)
            }


        }else{
            tvDetails.visibility = View.GONE
        }

        imgBtnBack.setOnClickListener{
            finish()
        }


        initAdapter()




    }

    private fun initViewModel() {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.initMethod(this)

        chatViewModel.callGetConversationDetails(mBookingNo).observe(this, Observer<ViewChatResponse> {
             baseViewModel.rentersLoader.postValue(false)
            if(it.status == "1"){
              /*  it.data?.total_item?.let{
                    try {
                        TOTAL_MESSAGES_COUNT = it.toInt()
                    }catch (ex : Exception){
                        ex.printStackTrace()
                    }
                }*/
                val formatter1= SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.ENGLISH)
                for(i in 0 until it.data?.conv_details!!.size) {
                    val user = User(it.data?.conv_details!![i].sender_id,it.data?.conv_details!![i].sender_firstname,"",true)
                    val message = Message(it.data?.conv_details!![i].message_id,user,it.data?.conv_details!![i].message,formatter1.parse(it.data?.conv_details!![i].dateAdded))
                    messages.add(message)
                }

                loadMessages()

            }
        })


    }



    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter!!.unselectAllItems()
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
      //  Log.i("TAG", "onLoadMore: $page $totalItemsCount")
      /*  if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages()
        }*/
    }



    private fun loadMessages() {
        messages.reverse()
        messagesAdapter!!.addToEnd(messages, false)
    }

    override fun onSubmit(input: CharSequence?): Boolean {
        setResult(Activity.RESULT_OK)
        sendMessage(input.toString())
        if(input.toString().trim().isNotEmpty()) {
            val user = User(mSessionManager.getUserDetails().id, mSessionManager.getUserDetails().firstname, "", true)
            val msg = Message("", user, input.toString())
            messagesAdapter?.addToStart(
                    msg, true)
        }
        return true

        }

    private fun sendMessage(message: String) {
        chatViewModel.callSendMessage(message,mSessionManager.getUserDetails().id,mReceiverId,mBookingNo,mBookingStatus).observe(this, Observer<CommonResponse> {
            if(it.status == "1"){

            }

        })

    }


    override fun onStartTyping() {

    }

    override fun onStopTyping() {

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
