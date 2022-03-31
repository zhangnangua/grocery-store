package com.pumpkin.okhttp.util

import java.text.SimpleDateFormat

fun log(vararg msg: Any?) {
    val nowTime = SimpleDateFormat("HH:mm:ss:SSS").format(System.currentTimeMillis())
    println("$nowTime [${Thread.currentThread().name}] ${msg.joinToString(" ")}")
}