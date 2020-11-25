package com.raunakgarments.global


object OrderStatusObject {
    enum class orderStatus {
        paymentDone, refunded, paymentPending
    }
    enum class deliveryStatus {
        paymentDone, delivered, returned
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