package com.raunakgarments.model

import java.util.*
import kotlin.collections.HashMap

class UserOrders {
    var orders: HashMap<String, ConfirmationCartProduct> = HashMap<String, ConfirmationCartProduct>()
    var orderStatus = ""
    var dateStamp = ""
    var timeStamp = ""
    var fullDateStampRaw = ""
    var dateStampRaw: Date = Date()
    var dayStamp = ""
}

