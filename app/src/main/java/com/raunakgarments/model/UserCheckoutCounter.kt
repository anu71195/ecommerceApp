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

public class UserCheckoutCounter() {

    var dateMap = HashMap<String, UserCheckoutCounterProduct>()
}

class UserCheckoutCounterProduct() {
    var productMap = HashMap<String,CheckoutCounter>()
}


