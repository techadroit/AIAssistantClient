package org.dev.assistant.platform

import kotlin.test.Test
import kotlin.test.assertEquals

//expect val expectedPlatformType: PlatformType

class PlatformDetectionTest {
//    @Test
//    fun returnsExpectedPlatformType() {
//        assertEquals(
//            expectedPlatformType,
//            currentPlatformType(),
//            "currentPlatformType() should reflect the platform under test"
//        )
//    }

    @Test
    fun currentPlatformMatchesGetPlatform() {
        println(getPlatform().type)
        assertEquals(
            getPlatform().type,
            currentPlatformType(),
            "currentPlatformType() must delegate to getPlatform().type"
        )
    }
}

