@file:Suppress("unused")

package com.vdreamers.vandroid.ext

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

fun View?.expendTouchArea(expendSize: Int) {
    if (this != null) {
        if (this.parent is View) {
            val parentView: View? = this.parent as? View
            parentView?.post {
                val rect = Rect()
                this.getHitRect(rect)
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, this)
            }
        }
    }
}

fun View?.restoreTouchArea() {
    if (this != null) {
        if (this.parent is View) {
            val parentView: View? = this.parent as? View
            parentView?.post {
                val rect = Rect()
                rect.setEmpty()
                parentView.touchDelegate = TouchDelegate(rect, this)
            }
        }
    }
}