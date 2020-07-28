package com.raunakgarments.model

class ContactUs {
    var phoneNumber = "Not Available"
    var emailAddress = "Not Available"

    constructor()
    constructor(phoneNumber: String, emailAddress: String) {
        this.phoneNumber = phoneNumber
        this.emailAddress = emailAddress
    }
}