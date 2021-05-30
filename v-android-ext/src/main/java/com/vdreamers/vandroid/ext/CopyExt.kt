@file:Suppress("unused")

package com.vdreamers.vandroid.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * copy text into clipboard
 *
 * @param[label] User-visible label for the clip data.
 * @param[text] The actual text in the clip.
 */
fun Context.copyTextIntoClipboard(text: CharSequence?, label: String? = "") {
    if (text.isNullOrEmpty()) return
    val cbs = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return
    cbs.setPrimaryClip(ClipData.newPlainText(label, text))
}
