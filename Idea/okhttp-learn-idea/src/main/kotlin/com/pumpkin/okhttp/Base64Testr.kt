package com.pumpkin.okhttp

import com.pumpkin.okhttp.util.log
import java.util.*

fun main() {
    val authString = "epointyanfa:epoint_yanfa"
    val bytesBase64 = Base64.getEncoder().encode(authString.encodeToByteArray())
    val realRequestAuth = String(bytesBase64).also { log(it) }
}