package com.example.mobilecomputing.util

import java.text.SimpleDateFormat

fun getDateAsString(date: Long): String {
    return SimpleDateFormat("MMM dd, yyyy").format(date)
}