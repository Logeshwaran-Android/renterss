package com.rent.renters.app.ui.profile


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.rent.renters.R
import com.rent.renters.app.data.model.CommonData
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.userHomePage.HomeActivity
import com.rent.renters.app.ui.hostHomePage.HostingActivity
import com.rent.renters.app.ui.settings.PayoutPreferenceActivity
import com.rent.renters.app.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.ivProfile
import kotlinx.android.synthetic.main.fragment_profile.tvEditProfile
import kotlinx.android.synthetic.main.fragment_profile.tvProfileName


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mActivity: Activity
    private var mSessionManager: SessionManager? = null
    private lateinit var mUserData : CommonData


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = view.context as Activity
        mSessionManager = SessionManager(view.context)
        initView()
    }

    private fun initView( ) {
        if(arguments!=null){
            if(arguments?.containsKey(Iconstants.TYPE)!!){
                val mType = arguments?.getString(Iconstants.TYPE)
                if(mType == Iconstants.PUSH_VERIFICATION) {
                    val editProfileIntent = Intent(mActivity, EditProfileActivity::class.java).putExtra(Iconstants.TYPE,Iconstants.PUSH_VERIFICATION)
                    startActivityForResult(editProfileIntent, Iconstants.EDIT_PROFILE_REQUEST_CODE)
                }

            }
        }

        tvSettings.setOnClickListener(this)
        tvSwitchToHost.setOnClickListener(this)
        rlProfile.setOnClickListener(this)
        tvEditProfile.setOnClickListener(this)
        tvPayoutPreference.setOnClickListener(this)


        setProfileData()
        if (arguments != null) {
            val bundle = arguments
            if (bundle!!.containsKey(Iconstants.TYPE)) {
                val mType = bundle.getString(Iconstants.TYPE)
                if (mType!!.equals(Iconstants.HOST, ignoreCase = true)) {
                    tvSwitchToHost.text = getString(R.string.switch_to_user)
                    tvPayoutPreference.visibility = VISIBLE
                    viewPayoutPreference.visibility = VISIBLE
                } else if (mType.equals(Iconstants.USER, ignoreCase = true)) {
                    tvSwitchToHost.text = getString(R.string.switch_to_host)
                    tvPayoutPreference.visibility = GONE
                    viewPayoutPreference.visibility = GONE
                }
            }
        }
    }

    fun setProfileData(){
        mUserData = mSessionManager!!.getUserDetails()
        tvProfileName.text = (mUserData.firstname).plus(" ").plus(mUserData.lastname)
        (mActivity as BaseActivity).loadCircleImageWithGlide(ivProfile,mUserData.profile_image,R.drawable.ic_default_circle_profile_img)
        if(mUserData.is_verified.equals("yes",true)) {
            ivVerifiedImage.visibility = VISIBLE

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Iconstants.EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK){
            setProfileData()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSwitchToHost -> if (tvSwitchToHost.text == getString(R.string.switch_to_host)) {
                tvSwitchToHost.text = getString(R.string.switch_to_user)
                val hostIntent = Intent(mActivity, HostingActivity::class.java)
                startActivity(hostIntent)
                mActivity.finish()
            } else {
                tvSwitchToHost.text = getString(R.string.switch_to_host)
                val hostIntent = Intent(mActivity, HomeActivity::class.java)
                startActivity(hostIntent)
                mActivity.finish()
            }
            R.id.tvSettings -> {
                val settingIntent = Intent(mActivity, SettingsActivity::class.java)
                startActivity(settingIntent)
            }
            R.id.rlProfile,R.id.tvEditProfile -> {
                val editProfileIntent = Intent(mActivity, EditProfileActivity::class.java)
                startActivityForResult(editProfileIntent,Iconstants.EDIT_PROFILE_REQUEST_CODE)
            }
            R.id.tvPayoutPreference ->{
                val payoutIntent = Intent(mActivity, PayoutPreferenceActivity::class.java)
                startActivityForResult(payoutIntent,Iconstants.EDIT_PROFILE_REQUEST_CODE)

            }
        }
    }
}