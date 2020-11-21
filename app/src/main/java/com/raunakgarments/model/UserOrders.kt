package com.raunakgarments.model

import java.util.*
import kotlin.collections.HashMap

class UserOrders {
    //todo change confirmationCartproduct
    var orders: HashMap<String, ConfirmationCartProduct> = HashMap<String, ConfirmationCartProduct>()
    var orderStatus = ""
    var deliveryStatus = ""
    var dateStamp = ""
    var timeStamp = ""
    var fullDateStampRaw = ""
    var dateStampRaw: Date = Date()
    var dayStamp = ""
    var totalCost = ""
}

