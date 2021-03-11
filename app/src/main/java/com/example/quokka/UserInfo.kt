package com.example.quokka

import android.provider.ContactsContract
import java.text.DateFormat

data class UserInfo(var name: String = "", var surname: String = "", var email: String = "",
                    var password: String = "", var password2:String = "", var address: String = "")

data class TaskInfo(var name: String = "", var description: String = "", var points: Int = 0)