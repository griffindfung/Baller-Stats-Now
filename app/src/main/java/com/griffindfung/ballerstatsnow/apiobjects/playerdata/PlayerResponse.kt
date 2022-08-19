package com.griffindfung.ballerstatsnow.apiobjects.playerdata

import com.google.gson.annotations.SerializedName
import com.griffindfung.ballerstatsnow.apiobjects.Meta

data class PlayerResponse(
    @SerializedName("data")
    val data: List<Player>,

    @SerializedName("meta")
    val meta: Meta
)