package com.example.book.data.network

import com.example.book.data.model.Book
import com.example.book.data.model.Country
import com.example.book.data.model.IpLocation
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiService {

    @GET
    suspend fun getCountryList(@Url url: String): List<Country>

    @GET
    suspend fun getIpLocation(@Url url: String): IpLocation

    @GET
    suspend fun getBooks(@Url url: String): List<Book>
}
