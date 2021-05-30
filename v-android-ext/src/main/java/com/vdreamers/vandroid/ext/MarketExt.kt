@file:Suppress("unused")

package com.vdreamers.vandroid.ext

import android.content.Context
import android.content.Intent
import android.net.Uri

typealias NoFoundAction = () -> Unit

fun Context.goMarket(noFoundAction: NoFoundAction? = null): Boolean {
    var findMarket = false
    applicationContext?.let { app ->
        findMarket = app.goTargetMarket()
    }
    if (!findMarket) {
        noFoundAction?.invoke()
    }
    return findMarket
}


fun Context.goTargetMarket(targetPackage: String? = ""): Boolean {
    applicationContext?.let { app ->
        try {
            val goToMarketIntent = Intent(Intent.ACTION_VIEW)
            goToMarketIntent.apply {
                data = Uri.parse("market://details?id=${app.packageName}")
                if (!targetPackage.isNullOrBlank()) {
                    `package` = targetPackage
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (goToMarketIntent.resolveActivity(app.packageManager) != null) {
                app.startActivity(goToMarketIntent)
                return true
            }
        } catch (e: Exception) {
            // goTargetMarket error
            return false
        }
    }
    return false
}