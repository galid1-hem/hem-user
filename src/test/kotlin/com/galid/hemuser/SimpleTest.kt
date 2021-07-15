package com.galid.hemuser

import java.util.*
import kotlin.test.Test

class SimpleTest {
    @Test
    fun test() {
        val today = Date()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        println(Date(Date().time + (24*60*60*1000)))
    }
}