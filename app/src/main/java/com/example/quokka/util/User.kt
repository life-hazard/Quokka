package com.example.quokka.util

import android.provider.ContactsContract

data class User(
    var name: String,
    var surname: String,
    var email: String,
    var password: String,
    var address: String,
    var rating: Int,
    var points: Int
)