package com.griffindfung.ballerstatsnow.helpers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Class for retrieving date related information
class DateHelper {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"

        // Returns a string for today's date formatted in DATE_FORMAT
        fun getCurrentDateFormattedString(): String {
            val todaysDate = LocalDateTime.now()
            val dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT)
            return todaysDate.format(dateFormat)
        }
    }
}