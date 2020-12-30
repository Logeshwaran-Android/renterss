package com.rent.renters.app.ui.map


import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable

import com.google.maps.android.R

/**
 * Draws a bubble with a shadow, filled with any color.
 */
internal class BubbleDrawable(res: Resources) : Drawable() {

    private val mShadow: Drawable
    private val mMask: Drawable
    private var mColor = Color.WHITE

    init {
        mMask = res.getDrawable(R.drawable.amu_bubble_mask)
        mShadow = res.getDrawable(R.drawable.amu_bubble_shadow)
    }

    fun setColor(color: Int) {
        mColor = color
    }

    override fun draw(canvas: Canvas) {
        mMask.draw(canvas)
        canvas.drawColor(mColor, PorterDuff.Mode.SRC_IN)
        mShadow.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        throw UnsupportedOperationException()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        throw UnsupportedOperationException()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        mMask.setBounds(left, top, right, bottom)
        mShadow.setBounds(left, top, right, bottom)
    }

    override fun setBounds(bounds: Rect) {
        mMask.bounds = bounds
        mShadow.bounds = bounds
    }

    override fun getPadding(padding: Rect): Boolean {
        return mMask.getPadding(padding)
    }
}
