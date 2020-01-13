package com.app.home1.network.api

import com.app.home1.models.CatBreed
import com.app.home1.models.CatBreedImageInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatsApi{

    companion object {
        val BaseURL = "https://api.thecatapi.com"
    }

    @GET("/v1/breeds")
    suspend fun listBreeds(@Header("x-api-key") apiKey: String = "DEMO_API_KEY"): Response<List<CatBreed>?>

    @GET("/v1/images/search?limit=1")
    suspend fun getBreedImageInfo(@Header("x-api-key") apiKey: String = "DEMO_API_KEY",
                                  @Query("breed_ids") breedId: String): Response<List<CatBreedImageInfo>?>
}