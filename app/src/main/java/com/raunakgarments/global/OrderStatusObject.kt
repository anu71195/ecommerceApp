package com.raunakgarments.global

import android.graphics.Color


object OrderStatusObject {
    enum class orderStatus {
        paymentDone, refunded, paymentPending, error
    }
    enum class deliveryStatus {
        paymentDone, delivered, returned, cancelled, error
    }

    fun getOrderStringFromString(orderString: String): String {
        if(getOrderEnumStatus(orderString) == orderStatus.paymentDone) {
            return getOrderString(orderStatus.paymentDone)
        } else if(getOrderEnumStatus(orderString) == orderStatus.refunded) {
            return getOrderString(orderStatus.refunded)
        } else if(getOrderEnumStatus(orderString) == orderStatus.paymentPending)  {
            return getOrderString(orderStatus.paymentPending)
        }
        return getOrderString(orderStatus.error)
    }

    fun getDeliveryStringFromString(deliveryString: String): String {
        if(getDeliveryEnumStatus(deliveryString) == deliveryStatus.paymentDone) {
            return getDeliveryString(deliveryStatus.paymentDone)
        } else if(getDeliveryEnumStatus(deliveryString) == deliveryStatus.delivered) {
            return getDeliveryString(deliveryStatus.delivered)
        } else if(getDeliveryEnumStatus(deliveryString) == deliveryStatus.returned)  {
            return getDeliveryString(deliveryStatus.returned)
        } else if(getDeliveryEnumStatus(deliveryString) == deliveryStatus.cancelled) {
            return getDeliveryString(deliveryStatus.cancelled)
        }
        return getDeliveryString(deliveryStatus.error)
    }

    fun getOrderEnumStatus(orderString: String): orderStatus {
        if(orderString == getOrderString(orderStatus.paymentDone)) {
            return orderStatus.paymentDone
        } else if(orderString == getOrderString(orderStatus.refunded)) {
            return orderStatus.refunded
        } else if(orderString == getOrderString(orderStatus.paymentPending)) {
            return orderStatus.paymentPending
        }
        return orderStatus.error
    }

    fun getDeliveryEnumStatus(deliveryString: String): deliveryStatus {
        if(deliveryString == getDeliveryString(deliveryStatus.paymentDone)) {
            return deliveryStatus.paymentDone
        } else if(deliveryString == getDeliveryString(deliveryStatus.delivered)) {
            return deliveryStatus.delivered
        } else if(deliveryString == getDeliveryString(deliveryStatus.returned)) {
            return deliveryStatus.returned
        } else if(deliveryString == getDeliveryString(deliveryStatus.cancelled)) {
            return deliveryStatus.cancelled
        }
        return deliveryStatus.error
    }

    fun getOrderColor(ordered: orderStatus): Int {
        if(ordered == orderStatus.paymentDone) {
            return Color.parseColor("#008000")
        } else if(ordered == orderStatus.refunded) {
            return Color.parseColor("#000000")
        } else if(ordered == orderStatus.paymentPending) {
            return Color.parseColor("#A10000")
        }
        return Color.parseColor("#FF0000")
    }

    fun getDeliveryColor(delivered: deliveryStatus): Int {
        if(delivered == deliveryStatus.paymentDone) {
            return Color.parseColor("#0411A4")
        } else if(delivered == deliveryStatus.delivered) {
            return Color.parseColor("#008000")
        } else if(delivered == deliveryStatus.returned) {
            return Color.parseColor("#000000")
        } else if(delivered == deliveryStatus.cancelled) {
            return Color.parseColor("#A10000")
        }
        return Color.parseColor("#FF0000")
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