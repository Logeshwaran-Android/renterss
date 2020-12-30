package com.rent.renters.mylibrary.util

import android.graphics.Color.*
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


open class MySpannable
/**
 * Constructor
 */
(isUnderline: Boolean) : ClickableSpan() {

    private var isUnderline = true

    init {
        this.isUnderline = isUnderline
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.color = parseColor("#008489")
    }

    override fun onClick(widget: View) {


    }
}