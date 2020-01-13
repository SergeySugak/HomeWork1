package com.app.home1.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseCatBreed(@SerializedName("id") open val id: String,
                        @SerializedName("name") open val name: String,
                        @SerializedName("description") open val description: String): Serializable