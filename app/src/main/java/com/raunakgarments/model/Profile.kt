package com.raunakgarments.model

class Profile {
    lateinit var userName: String
    lateinit var number: String
    lateinit var email: String
    lateinit var address: String
    lateinit var pinCode: String

    constructor(userName: String, number: String, email: String, address: String, pinCode: String) {
        this.userName = userName
        this.number = number
        this.email = email
        this.address = address
        this.pinCode = pinCode
    }
}