package com.raunakgarments.model

import java.text.SimpleDateFormat
import java.util.*

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

    fun getTodayDate(offset: Int): String {
        var todaysDate = SimpleDateFormat("ddMMMMyyyy")
        todaysDate.timeZone =
            TimeZone.getTimeZone("Asia/Kolkata")

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, offset)

        return todaysDate.format(calendar.time)
    }

}

