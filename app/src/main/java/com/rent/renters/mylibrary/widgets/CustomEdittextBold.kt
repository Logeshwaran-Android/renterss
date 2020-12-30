package com.rent.renters.mylibrary.widgets


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet


import androidx.appcompat.widget.AppCompatEditText
import com.rent.renters.R

/**
 * Created by user129 on 5/7/2018.
 */

class CustomEdittextBold : AppCompatEditText {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            val tf = Typeface.createFromAsset(context.assets, "fonts/Sofia Pro Bold.otf")
            setLineSpacing(1.25F,1.25F)
            setTextColor(resources.getColor(R.color.text_black))
            typeface = tf
        }
    }
}
