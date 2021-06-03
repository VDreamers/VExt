@file:Suppress("unused")

package com.vdreamers.vandroid.ext

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.*
import kotlin.math.pow
import kotlin.math.sqrt


fun Context.portrait(): Boolean {
    return this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Context.tabletMode(): Boolean {
    return this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >=
            Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun Context.hasMenuKey(): Boolean {
    return ViewConfiguration.get(this).hasPermanentMenuKey()
}

fun hasBackKey(): Boolean {
    return KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
}

fun Context.screenInches(): Double {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display = wm.defaultDisplay
    val realSize = Point()
    display.getRealSize(realSize)
    val dm = this.resources.displayMetrics
    val x = (realSize.x / dm.xdpi).toDouble().pow(2.0)
    val y = (realSize.y / dm.ydpi).toDouble().pow(2.0)
    return sqrt(x + y)
}

fun Context.navBarHeight(): Int {
    val result = 0
    // The device has a navigation bar
    val res = this.resources

    // Check if the device is a tablet
    val resourceId: Int = if (tabletMode()) {
        res.getIdentifier(
            if (portrait()) "navigation_bar_height" else "navigation_bar_height_landscape",
            "dimen", "android"
        )
    } else {
        res.getIdentifier(
            if (portrait()) "navigation_bar_height" else "navigation_bar_width",
            "dimen", "android"
        )
    }
    if (resourceId > 0) {
        return res.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.navigationBarShow(): Boolean {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val bounds = wm.currentWindowMetrics.bounds
        val realSize = Point()
        this.display?.getRealSize(realSize)
        return if (portrait()) {
            realSize.y != bounds.height()
        } else {
            realSize.x != bounds.width()
        }
    } else {
        @Suppress("DEPRECATION")
        val display: Display = wm.defaultDisplay
        val size = Point()
        val realSize = Point()
        @Suppress("DEPRECATION")
        display.getSize(size)
        display.getRealSize(realSize)
        return if (portrait()) {
            realSize.y != size.y
        } else {
            realSize.x != size.x
        }
    }
}

fun Context.screenWidth(): Int {
    val wm = this.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val bounds = wm.currentWindowMetrics.bounds
        return if (portrait()) {
            bounds.width()
        } else {
            bounds.height()
        }
    } else {
        val point = Point()
        @Suppress("DEPRECATION")
        wm.defaultDisplay.getSize(point)
        return if (portrait()) {
            point.x
        } else {
            point.y
        }
    }
}

fun Context.screenHeight(): Int {
    val wm = this.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val bounds = wm.currentWindowMetrics.bounds
        return if (portrait()) {
            bounds.height()
        } else {
            bounds.width()
        }
    } else {
        val point = Point()
        @Suppress("DEPRECATION")
        wm.defaultDisplay.getSize(point)
        return if (portrait()) {
            point.y
        } else {
            point.x
        }
    }
}

fun Context.statusBarHeight(): Int {
    var result = 0
    val resourceId: Int = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result =  this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}