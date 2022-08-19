package com.griffindfung.ballerstatsnow.apiobjects.playerdata


import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("height_feet")
    val heightFeet: Int,
    @SerializedName("height_inches")
    val heightInches: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("team")
    val team: Team,
    @SerializedName("weight_pounds")
    val weightPounds: Int
)