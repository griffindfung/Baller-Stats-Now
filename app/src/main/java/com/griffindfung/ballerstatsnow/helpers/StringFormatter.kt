package com.griffindfung.ballerstatsnow.helpers

import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player

// Class for converting input into more friendly formatted strings to be used for other functions
class StringFormatter {
    companion object {

        // Returns player first and last name concatenated
        fun formatPlayerFullName(player: Player): String {
            return player.firstName + (if (player.lastName.isNotEmpty()) " "  else "") + player.lastName
        }

        // Format date passed by individual values (year/month/day) and format to YYYY-MM-DD
        // Prefixes 0's for months and days < 10
        fun formatDateToStringQuery(year: Int, month: Int, day: Int): String {
            return "$year-" + (if (month < 10) "0" else "") + "$month-" + (if (day < 10) "0" else "") + "$day"
        }

        // Game date formatted as YYYY-MM-DDT00:00:00.000Z
        // but we only want to grab YYYY-MM-DD that is prefixed to the string
        fun formatGameDateToItemDate(date: String): String {
            return date.substring(0, 10)
        }

        // Game date formatted as YYYY-MM-DDT00:00:00.000Z
        // but we only want to grab hours and minutes (00:00)
        fun formatGameDateToItemTime(date: String): String {
            return date.substring(11,16)
        }
    }
}