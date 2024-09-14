package com.example.book.data.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String
)
