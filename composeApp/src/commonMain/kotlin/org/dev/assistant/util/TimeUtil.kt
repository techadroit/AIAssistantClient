package org.dev.assistant.util

import kotlinx.datetime.Clock

fun currentUtcTimestamp(): String {
    val utcDateTime = Clock.System.now()
    return utcDateTime.toString()
//    return "%04d-%02d-%02dT%02d:%02d:%02dZ".format(
//        utcDateTime.year,
//        utcDateTime.monthNumber,
//        utcDateTime.dayOfMonth,
//        utcDateTime.hour,
//        utcDateTime.minute,
//        utcDateTime.second
//    )
}