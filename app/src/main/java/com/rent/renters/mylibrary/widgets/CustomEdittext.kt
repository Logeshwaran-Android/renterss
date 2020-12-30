package com.rent.renters.mylibrary.widgets


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet


import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by Prem Kumar and Anitha on 10/8/2015.
 */
class CustomEdittext : AppCompatEditText {

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
            val tf = Typeface.createFromAsset(context.assets, "fonts/Sofia Pro Light.otf")
            setLineSpacing(1.25F,1.25F)
            typeface = tf
        }
    }

}
