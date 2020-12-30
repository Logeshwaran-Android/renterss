package com.rent.renters.app.fcm


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rent.renters.R
import com.rent.renters.app.data.model.Message
import com.rent.renters.app.data.model.MessagesItem
import com.rent.renters.app.data.model.User
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.hostHomePage.HostingActivity
import com.rent.renters.app.ui.inbox.DefaultMessageActivity
import com.rent.renters.app.ui.userHomePage.HomeActivity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var mSenderId = ""
    var mReceiverId = ""
    var mMessageId = ""
    var mSenderName = ""
    var mSenderimage = ""
    var mCreatedAt = ""
    var mMessage = ""
    var mBookingStatus =""
    var mType =""
    var mBookingNo =""

    private val setUserType = "SET_USER_TYPE"

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        var mSharedPreferenceName = applicationContext.getString(R.string.app_name)
        var mSharedPreferenceMode = 0
        var mSharedPreference: SharedPreferences = applicationContext.getSharedPreferences(mSharedPreferenceName, mSharedPreferenceMode)
        var mEditor = mSharedPreference.edit()
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            remoteMessage.notification!!.body?.let { it1 -> sendNotification(it1) }
        }

        // Check if message contains a data payload.
        remoteMessage.data?.let {

                var key =""
                var message =""

                remoteMessage.data.containsKey("msg")
                message = (remoteMessage.data["msg"]!!)
                remoteMessage.data.containsKey("key")
                key = (remoteMessage.data["key"]!!)

                if(key == Iconstants.PUSH_CHAT){
                    if(remoteMessage.data.containsKey("data")) {
                        val mData = JSONObject(remoteMessage.data["data"]!!)
                        if (mData.has("sender_id"))
                            mSenderId = mData.getString("sender_id")
                        if (mData.has("receiver_id"))
                            mReceiverId = mData.getString("receiver_id")
                        if (mData.has("messageId"))
                            mMessageId = mData.getString("messageId")
                        if (mData.has("sendername"))
                            mSenderName = mData.getString("sendername")
                        if (mData.has("senderimage"))
                            mSenderimage = mData.getString("senderimage")
                        if (mData.has("createdAt"))
                            mCreatedAt = mData.getString("createdAt")
                        if (mData.has("dateAdded"))
                            mCreatedAt = mData.getString("dateAdded")
                        if (mData.has("message"))
                            mMessage = mData.getString("message")
                        if (mData.has("booking_no"))
                            mBookingNo = mData.getString("booking_no")
                        if(mData.has("booking_status"))
                            mBookingStatus = mData.getString("booking_status")
                    }
                    if(DefaultMessageActivity.isOnline){
                        val formatter1 = SimpleDateFormat("yyyy-MM-dd HH:mm",
                                Locale.ENGLISH)
                            val mUser = User(mSenderId,mSenderName,"",true)
                            val mMessageItem = Message(mMessageId,mUser,mMessage,formatter1.parse(mCreatedAt))
                            val bundle = Bundle()
                            bundle.putParcelable(Iconstants.MESSAGE,mMessageItem)
                        bundle.putString(Iconstants.BOOKING_NO,mBookingNo)
                            val intent = Intent()
                            intent.action = getString(R.string.package_name)
                            intent.putExtra(Iconstants.BUNDLE, bundle)
                            sendBroadcast(intent)
                        } else{
                        val bundle = Bundle()
                        val mMessageItem = MessagesItem("",mMessage,mSenderId,mReceiverId,"","","",mCreatedAt,mSenderName,
                                mSenderimage,"","",mBookingNo,"",mBookingStatus,"","","",
                                "","","","","")

                        bundle.putParcelable(Iconstants.MESSAGE,mMessageItem)
                        val intent = Intent(this, DefaultMessageActivity::class.java).putExtra(Iconstants.BUNDLE,bundle)
                        sendNotification(intent,message)


                    }

                }
                else if(key == Iconstants.PUSH_TRANSACTION){
                    mEditor.putString(setUserType,Iconstants.HOST)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HostingActivity::class.java).putExtra(Iconstants.IS_FROM,Iconstants.COMPLETED_TRANSACTIONS)
                    sendNotification(intent,message)

                }else if(key == Iconstants.PUSH_VERIFICATION){
                    mEditor.putString(setUserType,Iconstants.USER)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HomeActivity::class.java).putExtra(Iconstants.IS_FROM,Iconstants.PUSH_VERIFICATION)
                    sendNotification(intent,message)

                }else if(key == Iconstants.PUSH_LISTING){
                    mEditor.putString(setUserType,Iconstants.HOST)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HostingActivity::class.java).putExtra(Iconstants.IS_FROM,Iconstants.PUSH_LISTING)
                    sendNotification(intent,message)

                }else if(key == Iconstants.PUSH_BOOKINGS){
                    mEditor.putString(setUserType,Iconstants.USER)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HomeActivity::class.java).putExtra(Iconstants.IS_FROM,Iconstants.PUSH_BOOKINGS)
                    sendNotification(intent,message)

                }else if(key == Iconstants.PUSH_RESERVATION){
                    mEditor.putString(setUserType,Iconstants.HOST)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HostingActivity::class.java).putExtra(Iconstants.IS_FROM,Iconstants.PUSH_RESERVATION)
                    sendNotification(intent,message)
                }else {
                    mEditor.putString(setUserType,Iconstants.USER)
                    mEditor.commit()
                    mEditor.apply()
                    val intent = Intent(this, HomeActivity::class.java)
                    sendNotification(intent,message)
                }


        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_transparent)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendNotification(intent:Intent, message:String) {
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_transparent)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}