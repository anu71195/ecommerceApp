package com.raunakgarments.global

import android.graphics.Color


object OrderStatusObject {
    enum class orderStatus {
        paymentDone, refunded, paymentPending
    }
    enum class deliveryStatus {
        paymentDone, delivered, returned, cancelled
    }

    fun getOrderColor(ordered: orderStatus): Int {
        if(ordered == orderStatus.paymentDone) {
            return Color.parseColor("#008000")
        } else if(ordered == orderStatus.refunded) {
            return Color.parseColor("#000000")
        } else if(ordered == orderStatus.paymentPending) {
            return Color.parseColor("#FF0000")
        }
        return Color.parseColor("#000000")
    }

    fun getDeliveryColor(delivered: deliveryStatus): Int {
        if(delivered == deliveryStatus.paymentDone) {
            return Color.parseColor("#0411A4")
        } else if(delivered == deliveryStatus.delivered) {
            return Color.parseColor("#008000")
        } else if(delivered == deliveryStatus.returned) {
            return Color.parseColor("#000000")
        } else if(delivered == deliveryStatus.cancelled) {
            return Color.parseColor("#FF0000")
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
        } else if(delivered == deliveryStatus.cancelled) {
            return "Cancelled"
        }
        return "error"
    }
}