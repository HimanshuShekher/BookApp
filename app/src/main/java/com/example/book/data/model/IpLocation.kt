package com.example.book.data.model

import com.google.gson.annotations.SerializedName

data class IpLocation(
    @SerializedName("status") val status: String,
    @SerializedName("country") val country: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("region") val region: String,
    @SerializedName("regionName") val regionName: String,
    @SerializedName("city") val city: String,
    @SerializedName("zip") val zip: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("isp") val isp: String,
    @SerializedName("org") val org: String,
    @SerializedName("as") val asInfo: String,
    @SerializedName("query") val query: String
)
