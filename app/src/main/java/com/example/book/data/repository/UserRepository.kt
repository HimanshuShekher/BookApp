package com.example.book.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.book.data.model.Country
import com.example.book.data.model.IpLocation
import com.example.book.data.network.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @Named("CountryApiService") private val countryApiService: ApiService,
    @Named("IpLocationApiService") private val ipLocationApiService: ApiService,
    @ApplicationContext private val context: Context
) {


    private val masterKeyAlias: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()


    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    suspend fun getCountryList(): List<Country> {
        return withContext(Dispatchers.IO) {
            countryApiService.getCountryList("b/IU1K")
        }
    }


    suspend fun getIpLocation(): IpLocation {
        return withContext(Dispatchers.IO) {
            ipLocationApiService.getIpLocation("json")
        }
    }


    fun login(email: String, password: String): Boolean {
        val savedEmail = prefs.getString("user_email", null)
        val savedPassword = prefs.getString("user_password", null)
        return email == savedEmail && password == savedPassword
    }


    fun saveCredentials(email: String, password: String) {
        with(prefs.edit()) {
            putString("user_email", email)
            putString("user_password", password)
            apply()
        }
    }


    fun isEmailRegistered(email: String): Boolean {
        val savedEmail = prefs.getString("user_email", null)
        return email == savedEmail
    }
}
