package com.pumpkin.okhttp

import com.pumpkin.okhttp.util.log
import com.sun.xml.internal.messaging.saaj.util.Base64

fun main() {
    val authString = "epointyanfa:epoint_yanfa"
    val bytesBase64 = Base64.encode(authString.encodeToByteArray())
    val realRequestAuth = String(bytesBase64).also { log(it) }
}