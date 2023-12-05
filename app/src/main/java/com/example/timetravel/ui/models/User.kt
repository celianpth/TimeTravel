package com.example.timetravel.ui.models

data class User(
    var uuid: String,
    val email: String,
    val fullname: String,
    val image: String?
) {
    constructor(): this("", "", "", "")
}
