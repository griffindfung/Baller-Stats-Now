package com.griffindfung.ballerstatsnow.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    const val BASE_URL = "https://www.balldontlie.io/api/v1/"

    var ballDontLieService: BallDontLieService? = null

    fun getInstance() : BallDontLieService {
        if (ballDontLieService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            ballDontLieService = retrofit.create(BallDontLieService::class.java)
        }
        return ballDontLieService!!
    }
}