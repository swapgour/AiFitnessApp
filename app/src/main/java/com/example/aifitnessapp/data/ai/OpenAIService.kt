package com.example.aifitnessapp.data.ai

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun generateReply(
        @Body request: OpenAIRequest
    ): OpenAIResponse

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun generateDiet(@Body body: OpenAIRequest): OpenAIResponse
}
