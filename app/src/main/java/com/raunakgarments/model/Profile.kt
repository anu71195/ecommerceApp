package com.raunakgarments.model

class Profile {
    var userName: String = ""
    var number: String = ""
    var email: String = ""
    var address: String = ""
    var pinCode: String = ""
    var orderNumber = 1
    var areaPhoneCode = "91"
    var deliverable = false

    constructor() {}
    constructor(userName: String, number: String, email: String, address: String, pinCode: String) {
        this.userName = userName
        this.number = number
        this.email = email
        this.address = address
        this.pinCode = pinCode
    }
}