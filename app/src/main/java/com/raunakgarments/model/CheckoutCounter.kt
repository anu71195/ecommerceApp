package com.raunakgarments.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/*
*
*   userid/dates/products/countList
*
*
*
*
* */

public class CheckoutCounter() {

    var count = 0

    fun getTodayDate(): String {
        var todaysDate = SimpleDateFormat("ddMMMMyyyy")
        todaysDate.timeZone =
            TimeZone.getTimeZone("Asia/Kolkata")

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 0)

        return todaysDate.format(calendar.time)
    }

}

