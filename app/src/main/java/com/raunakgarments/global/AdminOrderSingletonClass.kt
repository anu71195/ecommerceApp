package com.raunakgarments.global

import androidx.room.FtsOptions
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.UserOrderProfile
import com.raunakgarments.model.UserOrders
import java.util.*
import kotlin.collections.ArrayList


object AdminOrderSingletonClass {
    var userOrderProfile = UserOrderProfile()

    var userOrders = UserOrders()

    var orderStatusList: MutableList<Pair<String,OrderStatusObject.orderStatus>> = ArrayList()
    var deliveryStatusList: MutableList<Pair<String,OrderStatusObject.deliveryStatus>> = ArrayList()

    enum class OrderEnumerationType {
        title, customer, time, clean, error
    }

    fun getItemList(): MutableList<String> {
        var itemList: MutableList<String> = ArrayList()
        itemList.add(getOrderEnumerationTypeString(OrderEnumerationType.title))
        itemList.add(getOrderEnumerationTypeString(OrderEnumerationType.customer))
        itemList.add(getOrderEnumerationTypeString(OrderEnumerationType.time))
        itemList.add(getOrderEnumerationTypeString(OrderEnumerationType.clean))

        return itemList
    }

    fun getOrderEnumerationTypeString(orderEnumerationType: OrderEnumerationType): String {
        when(orderEnumerationType){
            OrderEnumerationType.title -> {return "Choose Enumeration Type"}
            OrderEnumerationType.customer -> {return "By Customer"}
            OrderEnumerationType.time -> {return "By Time"}
            OrderEnumerationType.clean -> {return "Clean"}
            else -> {return "error"}
        }
    }
}