package com.example.timetravel.ui.models

data class Friend(
    var uuid: String,
    val name: String,
    val lastMessage: String,
    val image: String,
    val timestamp: Long,
) {
    constructor(): this ("", "", "", "", 0)
}
