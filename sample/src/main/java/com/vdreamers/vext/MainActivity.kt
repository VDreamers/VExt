package com.vdreamers.vext

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.vdreamers.vandroid.ext.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: tabletDevice ${this@MainActivity.tabletMode()}")

        Log.d(TAG, "onCreate: screenWidth ${this@MainActivity.screenWidth()}")
        Log.d(TAG, "onCreate: screenHeight ${this@MainActivity.screenHeight()}")
        val df = DecimalFormat("#.0")
        Log.d(TAG, "onCreate: screenInches ${df.format(this@MainActivity.screenInches())}")

        Log.d(TAG, "onCreate: navigationBarShow ${this@MainActivity.navigationBarShow()}")
        Log.d(TAG, "onCreate: navBarHeight ${this@MainActivity.navBarHeight()}")

        Log.d(TAG, "onCreate: statusBarHeight ${this@MainActivity.statusBarHeight()}")
    }

    companion object {
        const val TAG = "MainActivity"

    }
}