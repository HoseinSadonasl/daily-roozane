package com.abc.daily.util

import android.content.Context
import android.util.TypedValue

fun Context.getColorFromAttr(attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = false): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}