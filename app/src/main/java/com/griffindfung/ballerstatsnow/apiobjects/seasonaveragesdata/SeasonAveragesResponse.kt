package com.griffindfung.ballerstatsnow.apiobjects.seasonaveragesdata


import com.google.gson.annotations.SerializedName

data class SeasonAveragesResponse(
    @SerializedName("data")
    val `data`: List<Data>
)