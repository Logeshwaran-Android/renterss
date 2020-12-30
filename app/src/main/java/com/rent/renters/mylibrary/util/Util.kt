package com.rent.renters.mylibrary.util

import android.annotation.SuppressLint
import android.os.Build
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.widget.TextView
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.text.HtmlCompat
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.text.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rent.renters.R
import com.rent.renters.app.ui.adapter.CustomRecyclerViewAdapter
import com.rent.renters.app.ui.base.Iconstants

import org.jetbrains.anko.layoutInflater
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList



class Util {

    interface BottomApproveClickListener {
        fun onApproveClick(message: String,isFrom: String)
    }

    interface BottomSuccessClickListener {
        fun onSuccessClick(isFrom: String)
    }

    companion object {

        private lateinit var mBottomSheetDialog: BottomSheetDialog
        private lateinit var mSuccessDialog: BottomSheetDialog
        private lateinit var  mManageCalendarDialog :BottomSheetDialog

        fun isValidPassword(password: String): Boolean {

            val pattern: Pattern
            val matcher: Matcher

            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!%^&+=])(?=\\S+$).{4,}$"

            pattern = Pattern.compile(passwordPattern)
            matcher = pattern.matcher(password)

            return matcher.matches()

        }

        fun isValidEmail(email: String): Boolean {

            val pattern: Pattern
            val matcher: Matcher

            val emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

            pattern = Pattern.compile(emailPattern)
            matcher = pattern.matcher(email)

            return matcher.matches()

        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.getActiveNetworkInfo()
            return activeNetworkInfo != null && activeNetworkInfo.isConnected()
        }




        fun showBottomSheetDialogWithButtons(activity: Activity, title: String, message: String, listener: BottomDialogButtonInterface, isBothButtons : Boolean) {
            val theme: Int = R.style.BottomSheetDialogTheme
            val mCustomBottomSheetDialog = BottomSheetDialog(activity,theme)
            val sheetView = activity.layoutInflater.inflate(R.layout.custom_bottomsheet_with_buttons, null)
            mCustomBottomSheetDialog.setContentView(sheetView)
            mCustomBottomSheetDialog.setCancelable(false)

            val tvTitle = sheetView.findViewById<TextView>(R.id.tv_title)
            val tvMessage = sheetView.findViewById<TextView>(R.id.tv_message)
            tvTitle.text = title
            tvMessage.text = message
            val btnPositive = sheetView.findViewById<Button>(R.id.btnPositive)
            val btnNegative = sheetView.findViewById<Button>(R.id.btnNegative)
            if(isBothButtons){
                btnPositive.visibility = View.VISIBLE
                btnNegative.visibility = View.VISIBLE
            } else{
                btnPositive.visibility = View.VISIBLE
                btnPositive.text = activity.getString(R.string.ok)
                btnNegative.visibility = View.GONE
            }
            btnPositive.setOnClickListener{
                listener.onBottomCookieItemClick()
                if(mCustomBottomSheetDialog.isShowing)
                mCustomBottomSheetDialog.dismiss()
            }
            btnNegative.setOnClickListener{
                if(mCustomBottomSheetDialog.isShowing)
                mCustomBottomSheetDialog.dismiss()
            }
            if(!mCustomBottomSheetDialog.isShowing)
            mCustomBottomSheetDialog.show()
        }
        
        @SuppressLint("InflateParams")
        fun showBottomDialog(context: Context, listValues: ArrayList<Any>, listener: CustomRecyclerViewAdapter.CustomItemClickListener, isFrom: String, isMultiSelect: Boolean) {
            val theme: Int = R.style.BottomSheetDialogTheme
            mBottomSheetDialog = BottomSheetDialog(context,theme)
            val sheetView = context.layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
            mBottomSheetDialog.setContentView(sheetView)

            val rvBottomSheetDialog = sheetView.findViewById<RecyclerView>(R.id.rvBottomSheet)
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvBottomSheetDialog.layoutManager = layoutManager

            val mCustomRecyclerViewAdapter = CustomRecyclerViewAdapter(listValues, listener,isFrom,isMultiSelect)
            rvBottomSheetDialog.adapter = mCustomRecyclerViewAdapter
            if (!mBottomSheetDialog.isShowing)
                mBottomSheetDialog.show()
        }

        fun dismissBottomDialog() {
            if (mBottomSheetDialog.isShowing)
                mBottomSheetDialog.dismiss()
        }

        fun showSuccessDialog(context: Context,title:String, message: String, listener: BottomSuccessClickListener, isFrom: String) {
            val theme: Int = R.style.BottomSheetDialogTheme
            mSuccessDialog = BottomSheetDialog(context,theme)
            val sheetView = context.layoutInflater.inflate(R.layout.dialog_success_alert, null)
            mSuccessDialog.setContentView(sheetView)
            mSuccessDialog.setCancelable(false)
            val tvMessage = sheetView.findViewById<TextView>(R.id.tvDescription)
            val tvTitle = sheetView.findViewById<TextView>(R.id.tvTitle)
            val btnOk = sheetView.findViewById<Button>(R.id.btnOk)
            tvMessage.text = message
            tvTitle.text = title
            btnOk.setOnClickListener{
                listener.onSuccessClick(isFrom)

            }

            if (!mSuccessDialog.isShowing)
                mSuccessDialog.show()
        }
        fun dismissSuccessDialog(){
            if(mSuccessDialog.isShowing)
                mSuccessDialog.dismiss()
        }

        @SuppressLint("InflateParams")
        fun showAcceptDeclineDialog(context: Context,  listener: BottomApproveClickListener) {
            val theme: Int = R.style.BottomSheetDialogTheme
           val mBottomAcceptDialog = BottomSheetDialog(context,theme)
            val sheetView = context.layoutInflater.inflate(R.layout.dialog_accept_bottom_sheet, null)
            mBottomAcceptDialog.setContentView(sheetView)

            val etMessage = sheetView.findViewById<EditText>(R.id.etMessage)
            val btnApprove = sheetView.findViewById<Button>(R.id.btnApprove)
            val btnDecline = sheetView.findViewById<Button>(R.id.btnDecline)
            val ivClose = sheetView.findViewById<ImageButton>(R.id.ivClose)

            ivClose.setOnClickListener{
                mBottomAcceptDialog.dismiss()
            }

            btnApprove.setOnClickListener{

                mBottomAcceptDialog.dismiss()

                listener.onApproveClick(etMessage.text.toString(), Iconstants.PRE_APPROVE)
            }

            btnDecline.setOnClickListener{
                listener.onApproveClick(etMessage.text.toString(),Iconstants.DECLINE)

                mBottomAcceptDialog.dismiss()

            }

            if (!mBottomAcceptDialog.isShowing)
                mBottomAcceptDialog.show()
        }

        fun dismissManageCalendarDialog(){
            if(mManageCalendarDialog.isShowing)
                mManageCalendarDialog.dismiss()
        }



        fun makeTextViewResizable(textview: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
            val tv = textview

            if (tv.tag == null) {
                tv.tag = tv.text
            }
            val vto = tv.viewTreeObserver


            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                override fun onGlobalLayout() {

                    val obs = tv.viewTreeObserver

                    obs.removeOnGlobalLayoutListener(this)

                    if (maxLine == 0) {
                        val lineEndIndex = tv.layout.getLineEnd(0)
                        val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                        tv.text = text
                        tv.movementMethod = LinkMovementMethod.getInstance()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv.text = ""
                            tv.setText(
                                    addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, maxLine, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        } else {
                            tv.text = ""
                            tv.setText(
                                    addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        }


                    } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                        val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                        val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                        tv.text = text
                        tv.movementMethod = LinkMovementMethod.getInstance()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv.setText(
                                    addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, maxLine, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        }else {
                            tv.setText(
                                    addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        }
                    } else {
                        val lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                        val text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                        tv.text = text
                        tv.movementMethod = LinkMovementMethod.getInstance()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv.setText(
                                    addClickablePartTextViewResizable(HtmlCompat.fromHtml(tv.text.toString(), Html.FROM_HTML_MODE_LEGACY), tv, lineEndIndex, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        } else {
                            tv.setText(
                                    addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                                            viewMore), TextView.BufferType.SPANNABLE)
                        }

                    }
                }
            })

        }

        private fun addClickablePartTextViewResizable(strSpanned: Spanned, tv: TextView,
                                                      maxLine: Int, spanableText: String, viewMore: Boolean): SpannableStringBuilder {
            val str = strSpanned.toString()
            val ssb = SpannableStringBuilder(strSpanned)

            if (str.contains(spanableText)) {


                ssb.setSpan(object : MySpannable(false) {
                    override fun onClick(widget: View) {
                        if (viewMore) {
                            tv.layoutParams = tv.layoutParams
                            tv.text = ""
                            tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                            tv.invalidate()
                            makeTextViewResizable(tv, -1, "Read Less", false)
                        } else {
                            tv.layoutParams = tv.layoutParams
                            tv.text = ""
                            tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                            tv.invalidate()
                            makeTextViewResizable(tv, 2, "Read More", true)
                        }
                    }
                }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)

            }
            return ssb

        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
            return Uri.parse(path)
        }

        fun getRealPathFromURI(context: Context, contentURI: Uri): String {
            val cursor = context.contentResolver.query(contentURI, null, null, null, null)
            return if (cursor == null) { // Source is Dropbox or other similar local file path
                contentURI.path.toString()
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(idx)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun splitDate(date:String):String{
            val checkin = SimpleDateFormat("yyyy-MM-dd").parse(date)
            val month = checkin?.month.toString()
            val year = checkin?.year.toString()
            val date = checkin?.date.toString()

            try {
                val mDate = LocalDate.parse(year.plus(month).plus(date), DateTimeFormatter.BASIC_ISO_DATE)

                return mDate.toString()
            }catch ( ex:Exception){
                ex.printStackTrace()
            }
            return ""
        }



    }


}