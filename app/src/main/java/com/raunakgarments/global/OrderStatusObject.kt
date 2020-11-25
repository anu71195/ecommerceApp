package com.raunakgarments.global

import android.graphics.Color


object OrderStatusObject {
    enum class orderStatus {
        paymentDone, refunded, paymentPending
    }
    enum class deliveryStatus {
        paymentDone, delivered, returned
    }

    fun getOrderColor(ordered: orderStatus): Int {
        if(ordered == orderStatus.paymentDone) {
            return Color.parseColor("#008000")
        } else if(ordered == orderStatus.refunded) {
            return Color.parseColor("#FF0000")
        } else if(ordered == orderStatus.paymentPending) {
            return Color.parseColor("#000000")
        }
        return Color.parseColor("#000000")
    }

    fun getOrderString(ordered: orderStatus): String {
        if(ordered == orderStatus.paymentDone) {
            return "Payment Done"
        } else if(ordered == orderStatus.refunded) {
            return "Refunded"
        } else if(ordered == orderStatus.paymentPending) {
            return "Payment Pending"
        }
        return "error"
    }

    fun getDeliveryString(delivered: deliveryStatus): String {
        if(delivered == deliveryStatus.delivered) {
            return "Delivered"
        } else if(delivered == deliveryStatus.paymentDone) {
            return getOrderString(orderStatus.paymentDone)
        } else if(delivered == deliveryStatus.returned) {
            return "Returned"
        }
        return "error"
    }
}