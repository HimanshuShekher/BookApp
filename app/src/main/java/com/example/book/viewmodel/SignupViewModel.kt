
package com.example.book.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.data.model.Country
import com.example.book.data.model.IpLocation
import com.example.book.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> get() = _countries

    private val _defaultCountry = MutableStateFlow("")
    val defaultCountry: StateFlow<String> get() = _defaultCountry

    private val _signupState = MutableStateFlow(false)
    val signupState: StateFlow<Boolean> get() = _signupState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchCountries()
        fetchDefaultCountry()
    }

    fun fetchCountries() {
        viewModelScope.launch {
            try {
                val countryList = userRepository.getCountryList()
                _countries.value = countryList

            } catch (e: Exception) {
                _errorMessage.value = "Error fetching countries: ${e.message}"

            }
        }
    }

    fun fetchDefaultCountry() {
        viewModelScope.launch {
            try {
                val ipLocation = userRepository.getIpLocation()
                _defaultCountry.value = "${ipLocation.country} (${ipLocation.region})"
             //   Log.d("SignupViewModel", "Fetched default country: ${ipLocation.country}")
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching IP location: ${e.message}"

            }
        }
    }

    fun signup(email: String, password: String, confirmPassword: String, country: String) {
        // Basic validation
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || country.isEmpty()) {
            _errorMessage.value = "All fields are required"

            return
        }

        if (password != confirmPassword) {
            _errorMessage.value = "Passwords do not match"

            return
        }

        if (!isValidPassword(password)) {
            _errorMessage.value = "Password must be at least 8 characters long and include a number, special character, lowercase letter, and uppercase letter."

            return
        }

        if (userRepository.isEmailRegistered(email)) {
            _errorMessage.value = "Email is already registered"

            return
        }


        userRepository.saveCredentials(email, password)

        _signupState.value = true

    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { it in "!@#$%^&*()," } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isUpperCase() }
    }
}

