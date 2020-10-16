package com.chazzca.pruebamovilforte.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.chazzca.pruebamovilforte.LoginActivity
import com.chazzca.pruebamovilforte.R

class SplashScreen : AppCompatActivity() {

    var splashTime = 2500L
    var myHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        myHandler = Handler()
        myHandler!!.postDelayed({ goToMainActivity() }, splashTime)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}