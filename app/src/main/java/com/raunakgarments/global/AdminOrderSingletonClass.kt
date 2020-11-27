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

    var orderStatusList: MutableList<OrderStatusObject.orderStatus> = ArrayList()
    var deliveryStatusList: MutableList<OrderStatusObject.deliveryStatus> = ArrayList()
}