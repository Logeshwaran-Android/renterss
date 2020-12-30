package com.rent.renters.app.ui.adapter



import android.content.Context
import android.view.LayoutInflater


import android.view.View

import android.view.ViewGroup
import android.widget.ImageView

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity


class DetailPageImagePagerAdapter(internal var mContext: Context, var mSeachSpaceData: ArrayList<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return mSeachSpaceData.size
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageLayout = LayoutInflater.from(mContext).inflate(R.layout.slidingimages_layout, container, false)!!

        val imageView = imageLayout
                .findViewById(R.id.image) as ImageView

        (mContext as BaseActivity).loadImageWithGlide(imageView,mSeachSpaceData[position],R.drawable.ic_empty_space)

        container.addView(imageLayout, 0)


        return imageLayout

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }


}
