package com.rent.renters.app.ui.splashPage


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler

import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi

import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.rent.renters.R
import com.rent.renters.app.sessionManager.SessionManager
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.hostHomePage.HostingActivity
import com.rent.renters.app.ui.signUpSignInPage.SignUpSignInActivity
import com.rent.renters.app.ui.userHomePage.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    private var splashTime = 2000
    lateinit var sessionManager : SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sessionManager = SessionManager(this)
        Glide.with(this).load(R.drawable.ic_splash).into(ivSplash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDrawOverlayPermission()
        } else {
            callNextActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkDrawOverlayPermission() {


        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(this)) {
            // if not construct intent to request permission
            val mySnackBar = Snackbar.make(getRootView(),
                    getString(R.string.enable_floating_window_permission), Snackbar.LENGTH_INDEFINITE)
            mySnackBar.setAction(getString(R.string.ok)) {
                val intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()))
                // request permission via start activity for result
                startActivityForResult(intent, Iconstants.SCREEN_OVERLAY_REQUEST_CODE)
            }
            mySnackBar.show()
        } else {
            callNextActivity()
            // disablePullNotificationTouch();
            // Do your stuff, we got permission captain
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Iconstants.SCREEN_OVERLAY_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission Granted by Overlay
                    // Do your Stuff
                    callNextActivity()
                } else {
                    val mySnackBar = Snackbar.make(getRootView(),
                            getString(R.string.enable_floating_window_permission), Snackbar.LENGTH_INDEFINITE)
                    mySnackBar.setAction(getString(R.string.ok)) {
                        val intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getApplicationContext().getPackageName()))
                        // request permission via start activity for result
                        startActivityForResult(intent, Iconstants.SCREEN_OVERLAY_REQUEST_CODE)
                    }
                    mySnackBar.show()
                }
            }
        }
    }

    fun callNextActivity(){
        Handler().postDelayed({
            if(sessionManager.getLoginStatus()) {
                if(sessionManager.getUserType().equals(Iconstants.HOST,true))
                    startActivity(Intent(this@SplashActivity,
                            HostingActivity::class.java))
                else
                    startActivity(Intent(this@SplashActivity,
                            HomeActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity,
                        SignUpSignInActivity::class.java))
            }
            finish()
        }, splashTime.toLong())
    }

}
