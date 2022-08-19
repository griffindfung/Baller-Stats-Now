package com.griffindfung.ballerstatsnow.network
/************* INCOMPLETE ******************/
/************* DO NOT USE ******************/
/*
This class helps organize all keys and creation of querymaps. A generalized class for helping create
queries with multiple query parameters for specific request types e.g. games, players.
Use with generalized service call for specific type (game, player, etc.) that takes a querymap.
 */
class QueryMapBuilder {
    companion object {
        const val datesKey = "dates"
        const val startDateKey = "start_date"
        const val endDateKey = "end_date"
        const val pageKey = "page"
        const val perPageKey = "per_page"
        const val seasonsKey = "seasons"
        const val playerIdsKey = "player_ids"
        const val gameIdsKey = "game_ids"
        const val postSeasonKey = "postseason"
    }

    private var gameQueryMap: MutableMap<String, String> = mutableMapOf()

    // 'YYYY-MM-DD' formatted string
    fun addGamesWithSpecificDate(date: String) {
        gameQueryMap.put(datesKey, date)
    }

    fun setGamesDateRange(startDate: String, endDate: String) {
        gameQueryMap.put(startDateKey,startDate)
        gameQueryMap.put(endDateKey,endDate)
    }

    fun getGameQueryMap(): MutableMap<String, String> {
        return gameQueryMap
    }

    fun clearGameQueryMap() {
        gameQueryMap.clear()
    }
}