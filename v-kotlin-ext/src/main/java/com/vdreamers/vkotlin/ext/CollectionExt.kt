package com.vdreamers.vkotlin.ext

/**
 * List etx fun to find first matched [predicate] condition index
 * @receiver List<T> data list
 * @param predicate Function1<T, Boolean> condition
 * @return Int? matched condition index
 */
fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    var index = 0
    for (item in this) {
        if (predicate(item))
            return index
        index++
    }
    return null
}