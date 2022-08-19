package com.griffindfung.ballerstatsnow.apiobjects.gamedata


import com.google.gson.annotations.SerializedName
import com.griffindfung.ballerstatsnow.apiobjects.Meta

data class GameResponse(
    @SerializedName("data")
    val gameData: List<Game>,

    @SerializedName("meta")
    val meta: Meta
)