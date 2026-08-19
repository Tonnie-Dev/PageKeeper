package com.tonyxlab.pagekeeper.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toFormattedDate(): String {
    val pattern = "HH:mm MMM d, yyyy"
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return date.format(DateTimeFormatter.ofPattern(pattern))
}
