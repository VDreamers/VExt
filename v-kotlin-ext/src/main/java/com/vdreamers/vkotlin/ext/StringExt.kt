package com.vdreamers.vkotlin.ext

fun String.trimFirstAndLastChar(element: String?): String {
    var str = this
    var beginIndexFlag: Boolean
    var endIndexFlag: Boolean
    do {
        val beginIndex = if (str.indexOf(element!!) == 0) 1 else 0
        val endIndex =
            if (str.lastIndexOf(element) + 1 == str.length) {
                str.lastIndexOf(element)
            } else {
                str.length
            }
        str = str.substring(beginIndex, endIndex)
        beginIndexFlag = str.indexOf(element) == 0
        endIndexFlag = str.lastIndexOf(element) + 1 == str.length
    } while (beginIndexFlag || endIndexFlag)
    return str
}