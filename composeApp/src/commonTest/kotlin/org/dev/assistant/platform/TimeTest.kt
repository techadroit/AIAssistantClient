package org.dev.assistant.platform

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test

val currentMoment: Instant = Clock.System.now()
val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

class TimeTest{

    @Test
    fun testUtcTime(){

        println("UTC Time: $currentMoment")
        println("UTC Time: $datetimeInUtc")
        println("System Time: $datetimeInSystemZone")
    }
}