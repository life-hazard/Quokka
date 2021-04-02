package com.app.quokka.util

data class User(
    var name: String,
    var surname: String,
    var email: String,
    var password: String,
    var address: String,
    var rating: Map<String, Float>,
    var points: Int
)