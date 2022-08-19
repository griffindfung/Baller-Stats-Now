package com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("ast")
    val ast: Double,
    @SerializedName("blk")
    val blk: Double,
    @SerializedName("dreb")
    val dreb: Double,
    @SerializedName("fg3_pct")
    val fg3Pct: Double,
    @SerializedName("fg3a")
    val fg3a: Double,
    @SerializedName("fg3m")
    val fg3m: Double,
    @SerializedName("fg_pct")
    val fgPct: Double,
    @SerializedName("fga")
    val fga: Double,
    @SerializedName("fgm")
    val fgm: Double,
    @SerializedName("ft_pct")
    val ftPct: Double,
    @SerializedName("fta")
    val fta: Double,
    @SerializedName("ftm")
    val ftm: Double,
    @SerializedName("games_played")
    val gamesPlayed: Int,
    @SerializedName("min")
    val min: String,
    @SerializedName("oreb")
    val oreb: Double,
    @SerializedName("pf")
    val pf: Double,
    @SerializedName("player_id")
    val playerId: Int,
    @SerializedName("pts")
    val pts: Double,
    @SerializedName("reb")
    val reb: Double,
    @SerializedName("season")
    val season: Int,
    @SerializedName("stl")
    val stl: Double,
    @SerializedName("turnover")
    val turnover: Double
)