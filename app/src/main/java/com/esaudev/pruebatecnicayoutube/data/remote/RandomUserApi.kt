package com.esaudev.pruebatecnicayoutube.data.remote

import com.esaudev.pruebatecnicayoutube.data.remote.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface RandomUserApi {

    @GET("/api/")
    suspend fun fetchRandomUser(): ResponseDto

}