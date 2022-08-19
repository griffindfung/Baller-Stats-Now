package com.griffindfung.ballerstatsnow.network

import com.griffindfung.ballerstatsnow.apiobjects.gamedata.Game
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.PlayerResponse
import com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata.SeasonAveragesResponse

// Repo interface for network requests. All string dates are formatted to match 'YYYY-MM-DD'
class BallDontLieRepository(private val ballDontLieService: BallDontLieService) {

    // Gets all players matching "searchQuery" passed - search criteria handled by api
    suspend fun getPlayersByName(searchQuery: String): PlayerResponse? {
        val result = ballDontLieService.getPlayersByName(searchQuery)
        return if (result.isSuccessful) result.body() else null
    }

    // Gets game data for one game defined by "id"
    suspend fun getGameById(id: Int): Game? {
        val result = ballDontLieService.getGameById(id)
        return if (result.isSuccessful) result.body() else null
    }

    // Gets basic player data for one player defined by "id"
    suspend fun getPlayerById(id: Int): Player? {
        val result = ballDontLieService.getPlayerById(id)
        return if (result.isSuccessful) result.body() else null
    }

    // Gets all games in a date range defined by "startDate" and "endDate", both inclusive
    suspend fun getGamesByStartEndDate(startDate: String, endDate: String): GameResponse? {
        val result = ballDontLieService.getGamesByStartEndDate(startDate, endDate)
        return if (result.isSuccessful) result.body() else null
    }

    // Gets all games occurring on the dates listed in "dates"
    suspend fun getGamesByDates(dates: List<String>): GameResponse? {
        val result = ballDontLieService.getGamesByDates(dates)
        return if (result.isSuccessful) result.body() else null
    }

    // gets season averages for all players defined by "playerIds" for the "season" specified
    // season defaults to current season if null passed
    suspend fun getSeasonAverages(season: Int?, playerIds: List<Int>): SeasonAveragesResponse? {
        val result = ballDontLieService.getSeasonAverages(season, playerIds)
        return if (result.isSuccessful) result.body() else null
    }
}