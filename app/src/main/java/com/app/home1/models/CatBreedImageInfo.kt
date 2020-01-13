package com.app.home1.models

import com.google.gson.annotations.SerializedName

data class CatBreedImageInfo(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?)