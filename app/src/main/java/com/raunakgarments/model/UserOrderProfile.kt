package com.raunakgarments.model

class UserOrderProfile {
    var id = ""
    var userOrderList: HashMap<String, UserOrders> = HashMap<String, UserOrders>()
    var userCurrentProfile = Profile()
}