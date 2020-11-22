package com.raunakgarments.model

import java.util.*
import kotlin.collections.HashMap

class UserOrders {
    var id = ""
    var orders: HashMap<String, UserOrderProduct> = HashMap<String, UserOrderProduct>()
    var orderStatus = ""
    var deliveryStatus = ""
    var dateStamp = ""
    var timeStamp = ""
    var fullDateStampRaw = ""
    var dateStampRaw: Date = Date()
    var dayStamp = ""
    var totalCost = ""
    var userOrderProfile = Profile()
}

