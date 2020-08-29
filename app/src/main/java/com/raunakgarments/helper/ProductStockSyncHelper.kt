package com.raunakgarments.helper

import com.raunakgarments.FirebaseUtil
import com.raunakgarments.model.ProductStockSync
class ProductStockSyncHelper {
    fun setValueInChild(childId: String, productStockSync: ProductStockSync) {
        var productStockFirebaseUtil = FirebaseUtil()
        productStockFirebaseUtil.openFbReference("productStockSync")
        productStockFirebaseUtil.mDatabaseReference.child(childId)
            .setValue(productStockSync)
    }
}