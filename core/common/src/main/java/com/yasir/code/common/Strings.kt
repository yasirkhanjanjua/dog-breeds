package com.yasir.code.common

import java.util.Locale

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }