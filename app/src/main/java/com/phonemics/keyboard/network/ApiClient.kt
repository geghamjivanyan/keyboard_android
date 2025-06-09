package com.phonemics.keyboard.network

import com.phonemics.keyboard.model.ApiResponse
import com.phonemics.keyboard.model.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("search/")
    suspend fun searchSuggestions(@Body request: SearchRequest): ApiResponse
}

class ApiClient {
    private val apiService: ApiService
    
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://your-backend-url.com/") // Replace with your backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
    }
    
    suspend fun searchSuggestions(
        text: String,
        keyboardVersion: Int,
        rhythms: List<String> = emptyList()
    ): ApiResponse? {
        return try {
            val request = SearchRequest(
                text = text,
                rhythms = rhythms,
                keyboardVersion = keyboardVersion,
                keyboardChange = false
            )
            apiService.searchSuggestions(request)
        } catch (e: Exception) {
            null
        }
    }
}