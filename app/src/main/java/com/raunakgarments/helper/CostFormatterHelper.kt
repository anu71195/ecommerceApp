package com.raunakgarments.helper

import kotlin.math.ceil

class CostFormatterHelper {
    fun formatCost(cost :Double): Double {
        return (ceil(cost * 100)) / 100
    }
}