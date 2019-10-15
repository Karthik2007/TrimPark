package com.karthik.trimpark.base.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.karthik.trimpark.home.view.HomeActivity
import com.karthik.trimpark.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var splashHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashHandler = Handler()

        //splash animation handler
        splashHandler.postDelayed({
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }, 3000)


    }

    override fun onResume() {
        super.onResume()
        animateSplashImage()
    }

    override fun onStop() {
        super.onStop()

        // removes splash handler when user press back from splash screen
        // to avoid executing the runnable and opening the HomeActivity
        splashHandler.removeCallbacksAndMessages(null)
    }


    private fun animateSplashImage() {
        val bounceAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        splash_image.startAnimation(bounceAnimation)
    }
}
