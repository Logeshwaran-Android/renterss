package com.rent.renters.mylibrary.widgets


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by user129 on 4/26/2018.
 */

class CustomTextViewMedium : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }


    fun init() {
        val tf = Typeface.createFromAsset(context.assets, "fonts/Sofia Pro Medium.otf")
        setLineSpacing(1.25F,1.25F)
        typeface = tf
    }
}
