package com.raunakgarments.global

import com.raunakgarments.model.ConfirmationCartProduct
import java.util.*
import kotlin.collections.ArrayList

object UserCartSingletonClass {
        var confirmationCartProductArray: MutableList<ConfirmationCartProduct> = ArrayList()
        var productLockAcquiredTimeStamp = Date().time/1000
}