package com.griffindfung.ballerstatsnow.network

import com.griffindfung.ballerstatsnow.apiobjects.gamedata.Game
import com.griffindfung.ballerstatsnow.apiobjects.gamedata.GameResponse
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.Player
import com.griffindfung.ballerstatsnow.apiobjects.playerdata.PlayerResponse
import com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata.SeasonAveragesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BallDontLieService {

    @GET("games/{id}")
    suspend fun getGameById(@Path("id") id: Int): Response<Game>

    @GET("games")
    suspend fun getGamesByStartEndDate(@Query("start_date") startDate: String,
                                       @Query("end_date") endDate: String) : Response<GameResponse>

    @GET("games")
    suspend fun getGamesByDates(@Query("dates[]") dates: List<String>) : Response<GameResponse>

    @GET("players")
    suspend fun getPlayersByName(@Query("search") search: String): Response<PlayerResponse>

    @GET("players/{id}")
    suspend fun getPlayerById(@Path("id") id: Int): Response<Player>

    @GET("season_averages")
    suspend fun getSeasonAverages(@Query("season") season: Int?,
                                  @Query("player_ids[]") playerIds: List<Int>): Response<SeasonAveragesResponse>
}